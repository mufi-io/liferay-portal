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

package com.liferay.commerce.product.content.web.internal.info.item.provider;

import com.liferay.asset.info.item.provider.AssetEntryInfoItemFieldSetProvider;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.product.content.web.internal.info.CPDefinitionInfoItemFields;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.expando.info.item.provider.ExpandoInfoItemFieldSetProvider;
import com.liferay.info.field.InfoFieldSet;
import com.liferay.info.form.InfoForm;
import com.liferay.info.item.field.reader.InfoItemFieldReaderFieldSetProvider;
import com.liferay.info.item.provider.InfoItemFormProvider;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.template.info.item.provider.TemplateInfoItemFieldSetProvider;

import java.util.Locale;
import java.util.Set;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 * @author Alec Sloan
 * @author Allen Ziegenfus
 */
@Component(
	immediate = true, property = Constants.SERVICE_RANKING + ":Integer=10",
	service = InfoItemFormProvider.class
)
public class CPDefinitionInfoItemFormProvider
	implements InfoItemFormProvider<CPDefinition> {

	@Override
	public InfoForm getInfoForm() {
		return _getInfoForm(
			_assetEntryInfoItemFieldSetProvider.getInfoFieldSet(
				CPDefinition.class.getName()));
	}

	@Override
	public InfoForm getInfoForm(CPDefinition cpDefinition) {
		try {
			return _getInfoForm(
				_assetEntryInfoItemFieldSetProvider.getInfoFieldSet(
					_assetEntryLocalService.getEntry(
						CPDefinition.class.getName(),
						cpDefinition.getCPDefinitionId())));
		}
		catch (PortalException portalException) {
			_log.error(
				"Unable to get info form for commerce product definition " +
					cpDefinition.getCPDefinitionId());

			return ReflectionUtil.throwException(portalException);
		}
	}

	@Override
	public InfoForm getInfoForm(String formVariationKey, long groupId) {
		return _getInfoForm(
			_assetEntryInfoItemFieldSetProvider.getInfoFieldSet(
				CPDefinition.class.getName(), 0, groupId));
	}

	private InfoFieldSet _getBasicInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.availabilityStatusInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.descriptionInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.finalPriceInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.inventoryInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.nameInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.productTypeNameInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.shortDescriptionInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.skuInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.userNameInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "basic-information")
		).name(
			"basic-information"
		).build();
	}

	private InfoFieldSet _getCategorizationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.categoriesInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "categorization")
		).name(
			"categorization"
		).build();
	}

	private InfoFieldSet _getDetailedInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.accountGroupFilterEnabledInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.approvedInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.availableIndividuallyInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.channelFilterEnabledInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.companyIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.cpDefinitionIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.cProductIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.createDateInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.ddmStructureKeyInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.defaultLanguageIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.deliveryMaxSubscriptionCyclesInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.deliverySubscriptionEnabledInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.deliverySubscriptionLengthInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.deliverySubscriptionTypeInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.deliverySubscriptionTypeSettingsInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.deniedInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.depthInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.draftInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.expiredInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.freeShippingInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.groupIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.heightInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.ignoreSKUCombinationsInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.inactiveInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.incompleteInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.pendingInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.publishedInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.scheduledInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.shippableInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.shippingExtraPriceInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.shipSeparatelyPriceInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.stagedModelTypeInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.statusInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.statusByUserIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.statusByUserNameInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.statusByUserUuidInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.statusDateInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.subscriptionEnabledInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.subscriptionLengthInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.subscriptionTypeInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.subscriptionTypeSettingsInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.taxExemptInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.telcoOrElectronicsInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.userIdInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.userUuidInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.uuidInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.versionInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.weightInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.widthInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "detailed-information")
		).name(
			"detailed-information"
		).build();
	}

	private InfoFieldSet _getDisplayPageInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.displayPageUrlInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "display-page")
		).name(
			"displayPage"
		).build();
	}

	private InfoForm _getInfoForm(InfoFieldSet assetEntryInfoFieldSet) {
		Set<Locale> availableLocales = _language.getAvailableLocales();

		InfoLocalizedValue.Builder<String> infoLocalizedValueBuilder =
			InfoLocalizedValue.builder();

		for (Locale locale : availableLocales) {
			infoLocalizedValueBuilder.value(
				locale,
				ResourceActionsUtil.getModelResource(
					locale, CPDefinition.class.getName()));
		}

		return InfoForm.builder(
		).infoFieldSetEntry(
			_expandoInfoItemFieldSetProvider.getInfoFieldSet(
				CPDefinition.class.getName())
		).infoFieldSetEntry(
			_templateInfoItemFieldSetProvider.getInfoFieldSet(
				CPDefinition.class.getName())
		).infoFieldSetEntry(
			_infoItemFieldReaderFieldSetProvider.getInfoFieldSet(
				CPDefinition.class.getName())
		).infoFieldSetEntry(
			_getBasicInformationInfoFieldSet()
		).infoFieldSetEntry(
			assetEntryInfoFieldSet
		).infoFieldSetEntry(
			_getCategorizationInfoFieldSet()
		).infoFieldSetEntry(
			_getDetailedInformationInfoFieldSet()
		).infoFieldSetEntry(
			_getDisplayPageInfoFieldSet()
		).infoFieldSetEntry(
			_getScheduleInfoFieldSet()
		).labelInfoLocalizedValue(
			infoLocalizedValueBuilder.build()
		).name(
			CPDefinition.class.getName()
		).build();
	}

	private InfoFieldSet _getScheduleInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.displayDateInfoField
		).infoFieldSetEntry(
			CPDefinitionInfoItemFields.expirationDateInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "schedule")
		).name(
			"schedule"
		).build();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CPDefinitionInfoItemFormProvider.class);

	@Reference
	private AssetEntryInfoItemFieldSetProvider
		_assetEntryInfoItemFieldSetProvider;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private ExpandoInfoItemFieldSetProvider _expandoInfoItemFieldSetProvider;

	@Reference
	private InfoItemFieldReaderFieldSetProvider
		_infoItemFieldReaderFieldSetProvider;

	@Reference
	private Language _language;

	@Reference
	private TemplateInfoItemFieldSetProvider _templateInfoItemFieldSetProvider;

}