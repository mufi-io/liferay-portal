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

package com.liferay.portal.search.web.internal.facet;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.FacetFactory;
import com.liferay.portal.search.facet.user.UserFacetFactory;
import com.liferay.portal.search.web.facet.BaseJSPSearchFacet;
import com.liferay.portal.search.web.facet.SearchFacet;

import javax.portlet.ActionRequest;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(immediate = true, service = SearchFacet.class)
public class UserSearchFacet extends BaseJSPSearchFacet {

	@Override
	public String getConfigurationJspPath() {
		return "/facets/configuration/users.jsp";
	}

	@Override
	public FacetConfiguration getDefaultConfiguration(long companyId) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setClassName(getFacetClassName());

		facetConfiguration.setDataJSONObject(
			JSONUtil.put(
				"frequencyThreshold", 1
			).put(
				"maxTerms", 10
			).put(
				"showAssetCount", true
			));

		facetConfiguration.setFieldName(getFieldName());
		facetConfiguration.setLabel(getLabel());
		facetConfiguration.setOrder(getOrder());
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.1);

		return facetConfiguration;
	}

	@Override
	public String getDisplayJspPath() {
		return "/facets/view/users.jsp";
	}

	@Override
	public String getFacetClassName() {
		return userFacetFactory.getFacetClassName();
	}

	@Override
	public String getFieldName() {
		Facet facet = userFacetFactory.newInstance(null);

		return facet.getFieldName();
	}

	@Override
	public JSONObject getJSONData(ActionRequest actionRequest) {
		return JSONUtil.put(
			"frequencyThreshold",
			ParamUtil.getInteger(
				actionRequest, getClassName() + "frequencyThreshold", 1)
		).put(
			"maxTerms",
			ParamUtil.getInteger(actionRequest, getClassName() + "maxTerms", 10)
		).put(
			"showAssetCount",
			ParamUtil.getBoolean(
				actionRequest, getClassName() + "showAssetCount", true)
		);
	}

	@Override
	public String getLabel() {
		return "any-user";
	}

	@Override
	public String getTitle() {
		return "user";
	}

	@Override
	protected FacetFactory getFacetFactory() {
		return userFacetFactory;
	}

	@Override
	protected ServletContext getServletContext() {
		return _servletContext;
	}

	@Reference
	protected UserFacetFactory userFacetFactory;

	@Reference(target = "(osgi.web.symbolicname=com.liferay.portal.search.web)")
	private ServletContext _servletContext;

}