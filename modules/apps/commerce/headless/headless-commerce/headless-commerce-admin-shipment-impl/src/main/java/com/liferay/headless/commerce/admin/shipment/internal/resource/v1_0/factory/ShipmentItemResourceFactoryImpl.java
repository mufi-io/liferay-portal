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

package com.liferay.headless.commerce.admin.shipment.internal.resource.v1_0.factory;

import com.liferay.headless.commerce.admin.shipment.internal.security.permission.LiberalPermissionChecker;
import com.liferay.headless.commerce.admin.shipment.resource.v1_0.ShipmentItemResource;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.sort.SortParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Component(
	property = "resource.locator.key=/headless-commerce-admin-shipment/v1.0/ShipmentItem",
	service = ShipmentItemResource.Factory.class
)
@Generated("")
public class ShipmentItemResourceFactoryImpl
	implements ShipmentItemResource.Factory {

	@Override
	public ShipmentItemResource.Builder create() {
		return new ShipmentItemResource.Builder() {

			@Override
			public ShipmentItemResource build() {
				if (_user == null) {
					throw new IllegalArgumentException("User is not set");
				}

				return _shipmentItemResourceProxyProviderFunction.apply(
					(proxy, method, arguments) -> _invoke(
						method, arguments, _checkPermissions,
						_httpServletRequest, _httpServletResponse,
						_preferredLocale, _user));
			}

			@Override
			public ShipmentItemResource.Builder checkPermissions(
				boolean checkPermissions) {

				_checkPermissions = checkPermissions;

				return this;
			}

			@Override
			public ShipmentItemResource.Builder httpServletRequest(
				HttpServletRequest httpServletRequest) {

				_httpServletRequest = httpServletRequest;

				return this;
			}

			@Override
			public ShipmentItemResource.Builder httpServletResponse(
				HttpServletResponse httpServletResponse) {

				_httpServletResponse = httpServletResponse;

				return this;
			}

			@Override
			public ShipmentItemResource.Builder preferredLocale(
				Locale preferredLocale) {

				_preferredLocale = preferredLocale;

				return this;
			}

			@Override
			public ShipmentItemResource.Builder user(User user) {
				_user = user;

				return this;
			}

			private boolean _checkPermissions = true;
			private HttpServletRequest _httpServletRequest;
			private HttpServletResponse _httpServletResponse;
			private Locale _preferredLocale;
			private User _user;

		};
	}

	private static Function<InvocationHandler, ShipmentItemResource>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			ShipmentItemResource.class.getClassLoader(),
			ShipmentItemResource.class);

		try {
			Constructor<ShipmentItemResource> constructor =
				(Constructor<ShipmentItemResource>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException
							reflectiveOperationException) {

					throw new InternalError(reflectiveOperationException);
				}
			};
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new InternalError(noSuchMethodException);
		}
	}

	private Object _invoke(
			Method method, Object[] arguments, boolean checkPermissions,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Locale preferredLocale,
			User user)
		throws Throwable {

		String name = PrincipalThreadLocal.getName();

		PrincipalThreadLocal.setName(user.getUserId());

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (checkPermissions) {
			PermissionThreadLocal.setPermissionChecker(
				_defaultPermissionCheckerFactory.create(user));
		}
		else {
			PermissionThreadLocal.setPermissionChecker(
				new LiberalPermissionChecker(user));
		}

		ShipmentItemResource shipmentItemResource =
			_componentServiceObjects.getService();

		shipmentItemResource.setContextAcceptLanguage(
			new AcceptLanguageImpl(httpServletRequest, preferredLocale, user));

		Company company = _companyLocalService.getCompany(user.getCompanyId());

		shipmentItemResource.setContextCompany(company);

		shipmentItemResource.setContextHttpServletRequest(httpServletRequest);
		shipmentItemResource.setContextHttpServletResponse(httpServletResponse);
		shipmentItemResource.setContextUser(user);
		shipmentItemResource.setExpressionConvert(_expressionConvert);
		shipmentItemResource.setFilterParserProvider(_filterParserProvider);
		shipmentItemResource.setGroupLocalService(_groupLocalService);
		shipmentItemResource.setResourceActionLocalService(
			_resourceActionLocalService);
		shipmentItemResource.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		shipmentItemResource.setRoleLocalService(_roleLocalService);
		shipmentItemResource.setSortParserProvider(_sortParserProvider);

		try {
			return method.invoke(shipmentItemResource, arguments);
		}
		catch (InvocationTargetException invocationTargetException) {
			throw invocationTargetException.getTargetException();
		}
		finally {
			_componentServiceObjects.ungetService(shipmentItemResource);

			PrincipalThreadLocal.setName(name);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
	}

	private static final Function<InvocationHandler, ShipmentItemResource>
		_shipmentItemResourceProxyProviderFunction =
			_getProxyProviderFunction();

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ShipmentItemResource>
		_componentServiceObjects;

	@Reference
	private PermissionCheckerFactory _defaultPermissionCheckerFactory;

	@Reference(
		target = "(result.class.name=com.liferay.portal.kernel.search.filter.Filter)"
	)
	private ExpressionConvert<Filter> _expressionConvert;

	@Reference
	private FilterParserProvider _filterParserProvider;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private ResourceActionLocalService _resourceActionLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private SortParserProvider _sortParserProvider;

	@Reference
	private UserLocalService _userLocalService;

	private class AcceptLanguageImpl implements AcceptLanguage {

		public AcceptLanguageImpl(
			HttpServletRequest httpServletRequest, Locale preferredLocale,
			User user) {

			_httpServletRequest = httpServletRequest;
			_preferredLocale = preferredLocale;
			_user = user;
		}

		@Override
		public List<Locale> getLocales() {
			return Arrays.asList(getPreferredLocale());
		}

		@Override
		public String getPreferredLanguageId() {
			return LocaleUtil.toLanguageId(getPreferredLocale());
		}

		@Override
		public Locale getPreferredLocale() {
			if (_preferredLocale != null) {
				return _preferredLocale;
			}

			if (_httpServletRequest != null) {
				Locale locale = (Locale)_httpServletRequest.getAttribute(
					WebKeys.LOCALE);

				if (locale != null) {
					return locale;
				}
			}

			return _user.getLocale();
		}

		@Override
		public boolean isAcceptAllLanguages() {
			return false;
		}

		private final HttpServletRequest _httpServletRequest;
		private final Locale _preferredLocale;
		private final User _user;

	}

}