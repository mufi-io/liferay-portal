<%--
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
--%>

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.AssetCategoriesSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.AssetCategoriesSearchFacetTermDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.AssetEntriesSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.AssetEntriesSearchFacetTermDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.AssetTagsSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.AssetTagsSearchFacetTermDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.BucketDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.FolderSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.FolderSearcher" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.FolderTitleLookupImpl" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.ScopeSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.ScopeSearchFacetTermDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.UserSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.UserSearchFacetTermDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.AssetCategoriesSearchFacetDisplayContextBuilder" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.AssetCategoryPermissionCheckerImpl" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.AssetEntriesSearchFacetDisplayContextBuilder" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.AssetTagsSearchFacetDisplayContextBuilder" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.FolderSearchFacetDisplayContextBuilder" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.ScopeSearchFacetDisplayContextBuilder" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.builder.UserSearchFacetDisplayContextBuilder" %>

<%
String randomNamespace = PortalUtil.generateRandomKey(request, _RANDOM_KEY_INPUT) + StringPool.UNDERLINE;

Facet facet = (Facet)request.getAttribute("search.jsp-facet");

String fieldParam = ParamUtil.getString(request, facet.getFieldId());

FacetConfiguration facetConfiguration = facet.getFacetConfiguration();

JSONObject dataJSONObject = facetConfiguration.getData();

FacetCollector facetCollector = facet.getFacetCollector();

String cssClass = "search-facet search-";
%>

<%!
private static final String _RANDOM_KEY_INPUT = "portlet_search_facets_" + StringUtil.randomString();
%>