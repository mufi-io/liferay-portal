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

package com.liferay.commerce.pricing.internal.security.permission.resource;

import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionLogic;
import com.liferay.portal.kernel.security.permission.resource.definition.PortletResourcePermissionDefinition;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true, service = PortletResourcePermissionDefinition.class
)
public class CommerceDiscountPortletResourcePermissionDefinition
	implements PortletResourcePermissionDefinition {

	@Override
	public PortletResourcePermissionLogic[]
		getPortletResourcePermissionLogics() {

		return new PortletResourcePermissionLogic[] {
			new PricingPortletResourcePermissionLogic()
		};
	}

	@Override
	public String getResourceName() {
		return CommerceDiscountConstants.RESOURCE_NAME;
	}

}