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

<%
CommerceInventoryWarehousesDisplayContext commerceInventoryWarehousesDisplayContext = (CommerceInventoryWarehousesDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceInventoryWarehouse commerceInventoryWarehouse = commerceInventoryWarehousesDisplayContext.getCommerceInventoryWarehouse();
%>

<portlet:actionURL name="/commerce_inventory_warehouse/edit_commerce_inventory_warehouse_external_reference_code" var="editCommerceInventoryWarehouseExternalReferenceCodeURL" />

<commerce-ui:modal-content>
	<aui:form action="<%= editCommerceInventoryWarehouseExternalReferenceCodeURL %>" cssClass="container-fluid container-fluid-max-xl p-0" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commerceInventoryWarehouseId" type="hidden" value="<%= commerceInventoryWarehouse.getCommerceInventoryWarehouseId() %>" />

		<aui:model-context bean="<%= commerceInventoryWarehouse %>" model="<%= CommerceInventoryWarehouse.class %>" />

		<aui:input name="externalReferenceCode" type="text" value="<%= commerceInventoryWarehouse.getExternalReferenceCode() %>" wrapperCssClass="form-group-item" />
	</aui:form>
</commerce-ui:modal-content>