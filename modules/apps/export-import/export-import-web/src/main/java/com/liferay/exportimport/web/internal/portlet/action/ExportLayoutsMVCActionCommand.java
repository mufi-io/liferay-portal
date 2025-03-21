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

package com.liferay.exportimport.web.internal.portlet.action;

import com.liferay.exportimport.constants.ExportImportPortletKeys;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactory;
import com.liferay.exportimport.kernel.configuration.constants.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.exception.LARFileNameException;
import com.liferay.exportimport.kernel.lar.ExportImportHelper;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.kernel.service.ExportImportService;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.SessionTreeJSClicks;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.Serializable;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Daniel Kocsis
 * @author Máté Thurzó
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ExportImportPortletKeys.EXPORT,
		"mvc.command.name=/export_import/export_layouts"
	},
	service = MVCActionCommand.class
)
public class ExportLayoutsMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		hideDefaultSuccessMessage(actionRequest);

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		if (Validator.isNull(cmd)) {
			SessionMessages.add(
				actionRequest,
				_portal.getPortletId(actionRequest) +
					SessionMessages.KEY_SUFFIX_FORCE_SEND_REDIRECT);

			hideDefaultSuccessMessage(actionRequest);

			return;
		}

		setLayoutIdMap(actionRequest);

		try {
			_exportImportService.exportLayoutsAsFileInBackground(
				getExportImportConfiguration(actionRequest));

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception exception) {
			SessionErrors.add(actionRequest, exception.getClass());

			if (!(exception instanceof LARFileNameException)) {
				_log.error(exception);
			}
		}
	}

	protected ExportImportConfiguration getExportImportConfiguration(
			ActionRequest actionRequest)
		throws Exception {

		Map<String, Serializable> exportLayoutSettingsMap = null;

		long exportImportConfigurationId = ParamUtil.getLong(
			actionRequest, "exportImportConfigurationId");

		if (exportImportConfigurationId > 0) {
			ExportImportConfiguration exportImportConfiguration =
				_exportImportConfigurationLocalService.
					fetchExportImportConfiguration(exportImportConfigurationId);

			if (exportImportConfiguration != null) {
				exportLayoutSettingsMap =
					exportImportConfiguration.getSettingsMap();
			}
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		boolean privateLayout = ParamUtil.getBoolean(
			actionRequest, "privateLayout");

		if (exportLayoutSettingsMap == null) {
			long groupId = ParamUtil.getLong(actionRequest, "liveGroupId");

			exportLayoutSettingsMap =
				_exportImportConfigurationSettingsMapFactory.
					buildExportLayoutSettingsMap(
						themeDisplay.getUserId(), groupId, privateLayout,
						_getLayoutIds(actionRequest),
						actionRequest.getParameterMap(),
						themeDisplay.getLocale(), themeDisplay.getTimeZone());
		}

		String taskName = ParamUtil.getString(actionRequest, "name");

		if (Validator.isNull(taskName)) {
			Group group = themeDisplay.getScopeGroup();

			if (group.isPrivateLayoutsEnabled()) {
				if (privateLayout) {
					taskName = _language.get(
						actionRequest.getLocale(), "private-pages");
				}
				else {
					taskName = _language.get(
						actionRequest.getLocale(), "public-pages");
				}
			}
			else {
				taskName = _language.get(actionRequest.getLocale(), "pages");
			}
		}

		return _exportImportConfigurationLocalService.
			addDraftExportImportConfiguration(
				themeDisplay.getUserId(), taskName,
				ExportImportConfigurationConstants.TYPE_EXPORT_LAYOUT,
				exportLayoutSettingsMap);
	}

	protected void setLayoutIdMap(ActionRequest actionRequest) {
		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		boolean privateLayout = ParamUtil.getBoolean(
			actionRequest, "privateLayout");

		String treeId = ParamUtil.getString(actionRequest, "treeId");

		actionRequest.setAttribute(
			"layoutIdMap",
			_exportImportHelper.getSelectedLayoutsJSON(
				groupId, privateLayout,
				SessionTreeJSClicks.getOpenNodes(
					httpServletRequest, treeId + "SelectedNode")));
	}

	private long[] _getLayoutIds(PortletRequest portletRequest)
		throws Exception {

		return _exportImportHelper.getLayoutIds(portletRequest);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExportLayoutsMVCActionCommand.class);

	@Reference
	private ExportImportConfigurationLocalService
		_exportImportConfigurationLocalService;

	@Reference
	private ExportImportConfigurationSettingsMapFactory
		_exportImportConfigurationSettingsMapFactory;

	@Reference
	private ExportImportHelper _exportImportHelper;

	@Reference
	private ExportImportService _exportImportService;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

}