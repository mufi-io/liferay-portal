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

package com.liferay.site.navigation.menu.item.display.page.internal.portlet.action;

import com.liferay.asset.display.page.util.AssetDisplayPageUtil;
import com.liferay.info.field.InfoField;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemFormVariation;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.info.item.provider.InfoItemDetailsProvider;
import com.liferay.info.item.provider.InfoItemFormVariationsProvider;
import com.liferay.layout.display.page.LayoutDisplayPageInfoItemFieldValuesProvider;
import com.liferay.layout.display.page.LayoutDisplayPageInfoItemFieldValuesProviderTracker;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderTracker;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.navigation.admin.constants.SiteNavigationAdminPortletKeys;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	property = {
		"javax.portlet.name=" + SiteNavigationAdminPortletKeys.SITE_NAVIGATION_ADMIN,
		"mvc.command.name=/navigation_menu/get_item_details"
	},
	service = MVCResourceCommand.class
)
public class GetItemDetailsMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long classNameId = ParamUtil.getLong(resourceRequest, "classNameId");
		long classPK = ParamUtil.getLong(resourceRequest, "classPK");
		long classTypeId = ParamUtil.getLong(resourceRequest, "classTypeId");

		String className = _portal.getClassName(classNameId);

		try {
			JSONObject jsonObject = JSONUtil.put(
				"hasDisplayPage",
				AssetDisplayPageUtil.hasAssetDisplayPage(
					themeDisplay.getScopeGroupId(), classNameId, classPK,
					classTypeId));

			String itemType = _getItemType(className, themeDisplay);

			if (Validator.isNotNull(itemType)) {
				jsonObject.put("itemType", itemType);
			}

			String itemSubtype = _getItemSubtype(
				className, classPK, classTypeId, themeDisplay);

			if (Validator.isNotNull(itemSubtype)) {
				jsonObject.put("itemSubtype", itemSubtype);
			}

			jsonObject.put(
				"data", _getDetailsJSONArray(className, classPK, themeDisplay));

			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse, jsonObject);
		}
		catch (Exception exception) {
			_log.error("Unable to get mapping fields", exception);

			JSONPortletResponseUtil.writeJSON(
				resourceRequest, resourceResponse,
				JSONUtil.put(
					"error",
					_language.get(
						themeDisplay.getRequest(),
						"an-unexpected-error-occurred")));
		}
	}

	private JSONArray _getDetailsJSONArray(
			String className, long classPK, ThemeDisplay themeDisplay)
		throws Exception {

		LayoutDisplayPageInfoItemFieldValuesProvider
			layoutDisplayPageInfoItemFieldValuesProvider =
				_layoutDisplayPageInfoItemFieldValuesProviderTracker.
					getLayoutDisplayPageInfoItemFieldValuesProvider(className);

		if (layoutDisplayPageInfoItemFieldValuesProvider == null) {
			return JSONFactoryUtil.createJSONArray();
		}

		InfoItemFieldValues infoItemFieldValues =
			layoutDisplayPageInfoItemFieldValuesProvider.getInfoItemFieldValues(
				classPK);

		Collection<InfoFieldValue<Object>> infoFieldValues =
			infoItemFieldValues.getInfoFieldValues();

		Stream<InfoFieldValue<Object>> stream = infoFieldValues.stream();

		return JSONUtil.toJSONArray(
			stream.collect(Collectors.toList()),
			infoFieldValue -> JSONUtil.put(
				"title",
				() -> {
					InfoField infoField = infoFieldValue.getInfoField();

					return infoField.getLabel(themeDisplay.getLocale());
				}
			).put(
				"value", infoFieldValue.getValue(themeDisplay.getLocale())
			));
	}

	private String _getItemSubtype(
		String className, long classPK, long classTypeId,
		ThemeDisplay themeDisplay) {

		InfoItemFormVariationsProvider<?> infoItemFormVariationsProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemFormVariationsProvider.class, className);

		if (infoItemFormVariationsProvider == null) {
			return StringPool.BLANK;
		}

		LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
			_layoutDisplayPageProviderTracker.
				getLayoutDisplayPageProviderByClassName(className);

		if (layoutDisplayPageProvider == null) {
			return StringPool.BLANK;
		}

		LayoutDisplayPageObjectProvider<?> layoutDisplayPageObjectProvider =
			layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
				new InfoItemReference(className, classPK));

		if (layoutDisplayPageObjectProvider == null) {
			return StringPool.BLANK;
		}

		InfoItemFormVariation infoItemFormVariation =
			infoItemFormVariationsProvider.getInfoItemFormVariation(
				layoutDisplayPageObjectProvider.getGroupId(),
				String.valueOf(classTypeId));

		if (infoItemFormVariation != null) {
			return infoItemFormVariation.getLabel(themeDisplay.getLocale());
		}

		return StringPool.BLANK;
	}

	private String _getItemType(String className, ThemeDisplay themeDisplay) {
		InfoItemDetailsProvider<?> infoItemDetailsProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemDetailsProvider.class, className);

		if (infoItemDetailsProvider == null) {
			return StringPool.BLANK;
		}

		InfoItemClassDetails infoItemClassDetails =
			infoItemDetailsProvider.getInfoItemClassDetails();

		if (infoItemClassDetails != null) {
			return infoItemClassDetails.getLabel(themeDisplay.getLocale());
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GetItemDetailsMVCResourceCommand.class);

	@Reference
	private InfoItemServiceTracker _infoItemServiceTracker;

	@Reference
	private Language _language;

	@Reference
	private LayoutDisplayPageInfoItemFieldValuesProviderTracker
		_layoutDisplayPageInfoItemFieldValuesProviderTracker;

	@Reference
	private LayoutDisplayPageProviderTracker _layoutDisplayPageProviderTracker;

	@Reference
	private Portal _portal;

}