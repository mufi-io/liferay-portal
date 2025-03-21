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

package com.liferay.commerce.product.internal.security.permission.resource;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.permission.CommerceCatalogPermission;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.commerce.product.model.CPDefinition",
	service = ModelResourcePermission.class
)
public class CPDefinitionModelResourcePermission
	implements ModelResourcePermission<CPDefinition> {

	@Override
	public void check(
			PermissionChecker permissionChecker, CPDefinition cpDefinition,
			String actionId)
		throws PortalException {

		commerceCatalogPermission.check(
			permissionChecker, cpDefinition.getCommerceCatalog(), actionId);
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, long cpDefinitionId,
			String actionId)
		throws PortalException {

		CPDefinition cpDefinition = cpDefinitionLocalService.getCPDefinition(
			cpDefinitionId);

		commerceCatalogPermission.check(
			permissionChecker, cpDefinition.getCommerceCatalog(), actionId);
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, CPDefinition cpDefinition,
			String actionId)
		throws PortalException {

		return commerceCatalogPermission.contains(
			permissionChecker, cpDefinition.getCommerceCatalog(), actionId);
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, long cpDefinitionId,
			String actionId)
		throws PortalException {

		CPDefinition cpDefinition = cpDefinitionLocalService.getCPDefinition(
			cpDefinitionId);

		return commerceCatalogPermission.contains(
			permissionChecker, cpDefinition.getCommerceCatalog(), actionId);
	}

	@Override
	public String getModelName() {
		return CPDefinition.class.getName();
	}

	@Override
	public PortletResourcePermission getPortletResourcePermission() {
		return null;
	}

	@Reference
	protected CommerceCatalogPermission commerceCatalogPermission;

	@Reference
	protected CPDefinitionLocalService cpDefinitionLocalService;

}