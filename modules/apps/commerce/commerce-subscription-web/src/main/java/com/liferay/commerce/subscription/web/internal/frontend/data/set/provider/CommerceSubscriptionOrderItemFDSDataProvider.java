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

package com.liferay.commerce.subscription.web.internal.frontend.data.set.provider;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPSubscriptionInfo;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.commerce.product.util.CPSubscriptionTypeRegistry;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceSubscriptionEntryService;
import com.liferay.commerce.subscription.web.internal.constants.CommerceSubscriptionFDSNames;
import com.liferay.commerce.subscription.web.internal.model.OrderItem;
import com.liferay.frontend.data.set.provider.FDSDataProvider;
import com.liferay.frontend.data.set.provider.search.FDSKeywords;
import com.liferay.frontend.data.set.provider.search.FDSPagination;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "fds.data.provider.key=" + CommerceSubscriptionFDSNames.SUBSCRIPTION_ORDER_ITEMS,
	service = FDSDataProvider.class
)
public class CommerceSubscriptionOrderItemFDSDataProvider
	implements FDSDataProvider<OrderItem> {

	@Override
	public List<OrderItem> getItems(
			FDSKeywords fdsKeywords, FDSPagination fdsPagination,
			HttpServletRequest httpServletRequest, Sort sort)
		throws PortalException {

		List<OrderItem> orderItems = new ArrayList<>();

		Locale locale = _portal.getLocale(httpServletRequest);

		String price = StringPool.BLANK;
		String total = StringPool.BLANK;
		String discount = StringPool.BLANK;

		long commerceSubscriptionEntryId = ParamUtil.getLong(
			httpServletRequest, "commerceSubscriptionEntryId");

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			_commerceSubscriptionEntryService.fetchCommerceSubscriptionEntry(
				commerceSubscriptionEntryId);

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		CommerceContext commerceContext = _commerceContextFactory.create(
			_portal.getCompanyId(httpServletRequest),
			commerceSubscriptionEntry.getGroupId(),
			_portal.getUserId(httpServletRequest), 0, 0);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				commerceOrderItem.getCPInstanceId(),
				commerceOrderItem.getQuantity(), commerceContext);

		if (commerceProductPrice != null) {
			CommerceMoney unitPriceCommerceMoney =
				commerceProductPrice.getUnitPrice();
			CommerceMoney finalPriceCommerceMoney =
				commerceProductPrice.getFinalPrice();

			price = HtmlUtil.escape(unitPriceCommerceMoney.format(locale));
			total = HtmlUtil.escape(finalPriceCommerceMoney.format(locale));

			CommerceDiscountValue discountValue =
				commerceProductPrice.getDiscountValue();

			if (discountValue != null) {
				CommerceMoney discountAmountCommerceMoney =
					discountValue.getDiscountAmount();

				discount = HtmlUtil.escape(
					discountAmountCommerceMoney.format(locale));
			}
		}

		orderItems.add(
			new OrderItem(
				commerceOrderItem.getCommerceOrderItemId(),
				commerceOrderItem.getCommerceOrderId(),
				commerceOrderItem.getSku(), commerceOrderItem.getName(locale),
				price,
				_getSubscriptionDuration(commerceOrderItem, httpServletRequest),
				_getSubscriptionPeriod(
					commerceOrderItem, locale, httpServletRequest),
				discount, commerceOrderItem.getQuantity(), total));

		return orderItems;
	}

	@Override
	public int getItemsCount(
			FDSKeywords fdsKeywords, HttpServletRequest httpServletRequest)
		throws PortalException {

		return 1;
	}

	private String _getPeriodKey(
		String period, boolean plural, HttpServletRequest httpServletRequest) {

		if (plural) {
			return _language.get(
				httpServletRequest,
				StringUtil.toLowerCase(period + CharPool.LOWER_CASE_S));
		}

		return _language.get(httpServletRequest, period);
	}

	private String _getSubscriptionDuration(
			CommerceOrderItem commerceOrderItem,
			HttpServletRequest httpServletRequest)
		throws PortalException {

		String subscriptionDuration = StringPool.BLANK;

		if (commerceOrderItem.isSubscription()) {
			CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

			if (commerceOrder.isOpen()) {
				CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

				if ((cpInstance == null) ||
					(cpInstance.getCPSubscriptionInfo() == null)) {

					return subscriptionDuration;
				}

				CPSubscriptionInfo cpSubscriptionInfo =
					cpInstance.getCPSubscriptionInfo();

				String period = StringPool.BLANK;

				CPSubscriptionType cpSubscriptionType =
					_cpSubscriptionTypeRegistry.getCPSubscriptionType(
						cpSubscriptionInfo.getSubscriptionType());

				if (cpSubscriptionType != null) {
					period = cpSubscriptionType.getLabel(LocaleUtil.US);
				}

				long duration = cpSubscriptionInfo.getMaxSubscriptionCycles();

				if (duration > 0) {
					subscriptionDuration = _language.format(
						httpServletRequest, "duration-x-x",
						new Object[] {
							duration,
							_getPeriodKey(
								period, duration != 1, httpServletRequest)
						});
				}
			}
			else {
				CommerceSubscriptionEntry commerceSubscriptionEntry =
					_commerceSubscriptionEntryService.
						fetchCommerceSubscriptionEntry(
							commerceOrderItem.getCommerceOrderItemId());

				if (commerceSubscriptionEntry == null) {
					return subscriptionDuration;
				}

				String period = StringPool.BLANK;

				CPSubscriptionType cpSubscriptionType =
					_cpSubscriptionTypeRegistry.getCPSubscriptionType(
						commerceSubscriptionEntry.getSubscriptionType());

				if (cpSubscriptionType != null) {
					period = cpSubscriptionType.getLabel(LocaleUtil.US);
				}

				long duration =
					commerceSubscriptionEntry.getMaxSubscriptionCycles();

				subscriptionDuration = _language.format(
					httpServletRequest, "duration-x-x",
					new Object[] {
						duration,
						_getPeriodKey(period, duration != 1, httpServletRequest)
					});
			}
		}

		return subscriptionDuration;
	}

	private String _getSubscriptionPeriod(
			CommerceOrderItem commerceOrderItem, Locale locale,
			HttpServletRequest httpServletRequest)
		throws PortalException {

		String subscriptionPeriod = StringPool.BLANK;

		if (commerceOrderItem.isSubscription()) {
			CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

			if (commerceOrder.isOpen()) {
				CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

				if ((cpInstance == null) ||
					(cpInstance.getCPSubscriptionInfo() == null)) {

					return subscriptionPeriod;
				}

				CPSubscriptionInfo cpSubscriptionInfo =
					cpInstance.getCPSubscriptionInfo();

				String period = StringPool.BLANK;

				CPSubscriptionType cpSubscriptionType =
					_cpSubscriptionTypeRegistry.getCPSubscriptionType(
						cpSubscriptionInfo.getSubscriptionType());

				if (cpSubscriptionType != null) {
					period = cpSubscriptionType.getLabel(locale);
				}

				int subscriptionLength =
					cpSubscriptionInfo.getSubscriptionLength();

				subscriptionPeriod = _language.format(
					httpServletRequest, "every-x-x",
					new Object[] {
						subscriptionLength,
						_getPeriodKey(
							period, subscriptionLength != 1, httpServletRequest)
					});
			}
			else {
				CommerceSubscriptionEntry commerceSubscriptionEntry =
					_commerceSubscriptionEntryService.
						fetchCommerceSubscriptionEntry(
							commerceOrderItem.getCommerceOrderItemId());

				if (commerceSubscriptionEntry == null) {
					return subscriptionPeriod;
				}

				String period = StringPool.BLANK;

				int subscriptionLength =
					commerceSubscriptionEntry.getSubscriptionLength();

				subscriptionPeriod = _language.format(
					httpServletRequest, "every-x-x",
					new Object[] {
						subscriptionLength,
						_getPeriodKey(
							period, subscriptionLength != 1, httpServletRequest)
					});
			}
		}

		return subscriptionPeriod;
	}

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Reference
	private CommerceSubscriptionEntryService _commerceSubscriptionEntryService;

	@Reference
	private CPSubscriptionTypeRegistry _cpSubscriptionTypeRegistry;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

}