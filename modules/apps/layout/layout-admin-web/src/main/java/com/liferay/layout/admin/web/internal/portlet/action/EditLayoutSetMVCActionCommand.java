/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.client.extension.model.ClientExtensionEntryRel;
import com.liferay.client.extension.service.ClientExtensionEntryRelLocalService;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.LayoutSetService;
import com.liferay.portal.kernel.service.permission.GroupPermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout_admin/edit_layout_set"
	},
	service = MVCActionCommand.class
)
public class EditLayoutSetMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long layoutSetId = ParamUtil.getLong(actionRequest, "layoutSetId");

		long liveGroupId = ParamUtil.getLong(actionRequest, "liveGroupId");
		long stagingGroupId = ParamUtil.getLong(
			actionRequest, "stagingGroupId");
		boolean privateLayout = ParamUtil.getBoolean(
			actionRequest, "privateLayout");

		LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(layoutSetId);

		_updateClientExtensions(actionRequest, layoutSet, themeDisplay);

		_updateLogo(actionRequest, liveGroupId, stagingGroupId, privateLayout);

		updateLookAndFeel(
			actionRequest, themeDisplay.getCompanyId(), liveGroupId,
			stagingGroupId, privateLayout, layoutSet.getSettingsProperties());

		_updateMergePages(actionRequest, liveGroupId);

		_updateSettings(
			actionRequest, liveGroupId, stagingGroupId, privateLayout,
			layoutSet.getSettingsProperties());
	}

	protected void updateLookAndFeel(
			ActionRequest actionRequest, long companyId, long liveGroupId,
			long stagingGroupId, boolean privateLayout,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws Exception {

		long groupId = liveGroupId;

		if (stagingGroupId > 0) {
			groupId = stagingGroupId;
		}

		_updateLookAndFeel(
			actionRequest, companyId, groupId, privateLayout,
			typeSettingsUnicodeProperties);

		if (privateLayout) {
			return;
		}

		Group group = _groupLocalService.getGroup(groupId);

		if (!group.hasPrivateLayouts()) {
			_updateLookAndFeel(
				actionRequest, companyId, groupId, true,
				typeSettingsUnicodeProperties);
		}
	}

	private void _addClientExtensionEntryRel(
			String cetExternalReferenceCode, LayoutSet layoutSet, String type,
			long userId)
		throws Exception {

		if (Validator.isNotNull(cetExternalReferenceCode)) {
			ClientExtensionEntryRel clientExtensionEntryRel =
				_clientExtensionEntryRelLocalService.
					fetchClientExtensionEntryRelByExternalReferenceCode(
						layoutSet.getCompanyId(), cetExternalReferenceCode);

			if (clientExtensionEntryRel == null) {
				_clientExtensionEntryRelLocalService.
					deleteClientExtensionEntryRels(
						_portal.getClassNameId(LayoutSet.class),
						layoutSet.getLayoutSetId(), type);

				_clientExtensionEntryRelLocalService.addClientExtensionEntryRel(
					userId, layoutSet.getGroupId(),
					_portal.getClassNameId(LayoutSet.class),
					layoutSet.getLayoutSetId(), cetExternalReferenceCode, type,
					StringPool.BLANK);
			}
		}
		else {
			_clientExtensionEntryRelLocalService.deleteClientExtensionEntryRels(
				_portal.getClassNameId(LayoutSet.class),
				layoutSet.getLayoutSetId(), type);
		}
	}

	private void _updateClientExtensions(
			ActionRequest actionRequest, LayoutSet layoutSet,
			ThemeDisplay themeDisplay)
		throws Exception {

		_groupPermission.check(
			themeDisplay.getPermissionChecker(), layoutSet.getGroupId(),
			ActionKeys.MANAGE_LAYOUTS);

		String themeFaviconCETExternalReferenceCode = ParamUtil.getString(
			actionRequest, "themeFaviconCETExternalReferenceCode");

		_addClientExtensionEntryRel(
			themeFaviconCETExternalReferenceCode, layoutSet,
			ClientExtensionEntryConstants.TYPE_THEME_FAVICON,
			themeDisplay.getUserId());

		_clientExtensionEntryRelLocalService.deleteClientExtensionEntryRels(
			_portal.getClassNameId(LayoutSet.class), layoutSet.getLayoutSetId(),
			ClientExtensionEntryConstants.TYPE_GLOBAL_CSS);

		String[] globalCSSCETExternalReferenceCodes = ParamUtil.getStringValues(
			actionRequest, "globalCSSCETExternalReferenceCodes");

		for (String globalCSSCETExternalReferenceCode :
				globalCSSCETExternalReferenceCodes) {

			_clientExtensionEntryRelLocalService.addClientExtensionEntryRel(
				themeDisplay.getUserId(), layoutSet.getGroupId(),
				_portal.getClassNameId(LayoutSet.class),
				layoutSet.getLayoutSetId(), globalCSSCETExternalReferenceCode,
				ClientExtensionEntryConstants.TYPE_GLOBAL_CSS,
				StringPool.BLANK);
		}

		_clientExtensionEntryRelLocalService.deleteClientExtensionEntryRels(
			_portal.getClassNameId(LayoutSet.class), layoutSet.getLayoutSetId(),
			ClientExtensionEntryConstants.TYPE_GLOBAL_JS);

		String[] globalJSCETExternalReferenceCodes = ParamUtil.getStringValues(
			actionRequest, "globalJSCETExternalReferenceCodes");

		for (String globalJSCETExternalReferenceCode :
				globalJSCETExternalReferenceCodes) {

			String[] typeSettings = StringUtil.split(
				globalJSCETExternalReferenceCode, StringPool.UNDERLINE);

			UnicodeProperties typeSettingsUnicodeProperties =
				UnicodePropertiesBuilder.create(
					true
				).put(
					"loadType", typeSettings[1]
				).put(
					"scriptLocation", typeSettings[2]
				).build();

			_clientExtensionEntryRelLocalService.addClientExtensionEntryRel(
				themeDisplay.getUserId(), layoutSet.getGroupId(),
				_portal.getClassNameId(LayoutSet.class),
				layoutSet.getLayoutSetId(), typeSettings[0],
				ClientExtensionEntryConstants.TYPE_GLOBAL_JS,
				typeSettingsUnicodeProperties.toString());
		}

		String themeCSSCETExternalReferenceCode = ParamUtil.getString(
			actionRequest, "themeCSSCETExternalReferenceCode");

		_addClientExtensionEntryRel(
			themeCSSCETExternalReferenceCode, layoutSet,
			ClientExtensionEntryConstants.TYPE_THEME_CSS,
			themeDisplay.getUserId());
	}

	private void _updateLogo(
			ActionRequest actionRequest, long liveGroupId, long stagingGroupId,
			boolean privateLayout)
		throws Exception {

		boolean deleteLogo = ParamUtil.getBoolean(actionRequest, "deleteLogo");

		byte[] logoBytes = null;

		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		if (fileEntryId > 0) {
			FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

			logoBytes = FileUtil.getBytes(fileEntry.getContentStream());
		}

		long groupId = liveGroupId;

		if (stagingGroupId > 0) {
			groupId = stagingGroupId;
		}

		_layoutSetService.updateLogo(
			groupId, privateLayout, !deleteLogo, logoBytes);
	}

	private void _updateLookAndFeel(
			ActionRequest actionRequest, long companyId, long groupId,
			boolean privateLayout,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws Exception {

		String[] devices = StringUtil.split(
			ParamUtil.getString(actionRequest, "devices"));

		for (String device : devices) {
			String deviceThemeId = ParamUtil.getString(
				actionRequest, device + "ThemeId");
			String deviceColorSchemeId = ParamUtil.getString(
				actionRequest, device + "ColorSchemeId");
			String deviceCss = ParamUtil.getString(
				actionRequest, device + "Css");

			if (Validator.isNotNull(deviceThemeId)) {
				deviceColorSchemeId = ActionUtil.getColorSchemeId(
					companyId, deviceThemeId, deviceColorSchemeId);

				ActionUtil.updateThemeSettingsProperties(
					actionRequest, companyId, typeSettingsUnicodeProperties,
					device, deviceThemeId, false);
			}

			_layoutSetService.updateLookAndFeel(
				groupId, privateLayout, deviceThemeId, deviceColorSchemeId,
				deviceCss);
		}

		long faviconFileEntryId = ParamUtil.getLong(
			actionRequest, "faviconFileEntryId");

		_layoutSetService.updateFaviconFileEntryId(
			groupId, privateLayout, faviconFileEntryId);
	}

	private void _updateMergePages(
			ActionRequest actionRequest, long liveGroupId)
		throws Exception {

		boolean mergeGuestPublicPages = ParamUtil.getBoolean(
			actionRequest, "mergeGuestPublicPages");

		Group liveGroup = _groupLocalService.getGroup(liveGroupId);

		UnicodeProperties typeSettingsUnicodeProperties =
			liveGroup.getTypeSettingsProperties();

		typeSettingsUnicodeProperties.setProperty(
			"mergeGuestPublicPages", String.valueOf(mergeGuestPublicPages));

		_groupService.updateGroup(liveGroupId, liveGroup.getTypeSettings());
	}

	private void _updateSettings(
			ActionRequest actionRequest, long liveGroupId, long stagingGroupId,
			boolean privateLayout, UnicodeProperties settingsUnicodeProperties)
		throws Exception {

		UnicodeProperties typeSettingsUnicodeProperties =
			PropertiesParamUtil.getProperties(
				actionRequest, "TypeSettingsProperties--");

		settingsUnicodeProperties.putAll(typeSettingsUnicodeProperties);

		long groupId = liveGroupId;

		if (stagingGroupId > 0) {
			groupId = stagingGroupId;
		}

		_layoutSetService.updateSettings(
			groupId, privateLayout, settingsUnicodeProperties.toString());
	}

	@Reference
	private ClientExtensionEntryRelLocalService
		_clientExtensionEntryRelLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private GroupPermission _groupPermission;

	@Reference
	private GroupService _groupService;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private LayoutSetService _layoutSetService;

	@Reference
	private Portal _portal;

}