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

package com.liferay.commerce.product.definitions.web.internal.frontend.data.set.view.table;

import com.liferay.commerce.product.definitions.web.internal.constants.CommerceProductFDSNames;
import com.liferay.frontend.data.set.view.FDSView;
import com.liferay.frontend.data.set.view.table.BaseTableFDSView;
import com.liferay.frontend.data.set.view.table.FDSTableSchema;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaBuilder;
import com.liferay.frontend.data.set.view.table.FDSTableSchemaBuilderFactory;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "frontend.data.set.name=" + CommerceProductFDSNames.PRODUCT_DEFINITIONS,
	service = FDSView.class
)
public class CommerceProductDefinitionTableFDSView extends BaseTableFDSView {

	@Override
	public FDSTableSchema getFDSTableSchema(Locale locale) {
		FDSTableSchemaBuilder fdsTableSchemaBuilder =
			_fdsTableSchemaBuilderFactory.create();

		return fdsTableSchemaBuilder.add(
			"thumbnail", "",
			fdsTableSchemaField -> fdsTableSchemaField.setContentRenderer(
				"image")
		).add(
			"name.LANG", "name",
			fdsTableSchemaField -> {
				fdsTableSchemaField.setContentRenderer("actionLink");
				fdsTableSchemaField.setSortable(true);
			}
		).add(
			"catalog.name", "catalog"
		).add(
			"productTypeI18n", "type"
		).add(
			"workflowStatusInfo", "status",
			fdsTableSchemaField -> fdsTableSchemaField.setContentRenderer(
				"status")
		).add(
			"modifiedDate", "modified-date",
			fdsTableSchemaField -> {
				fdsTableSchemaField.setContentRenderer("dateTime");
				fdsTableSchemaField.setSortable(true);
			}
		).build();
	}

	@Reference
	private FDSTableSchemaBuilderFactory _fdsTableSchemaBuilderFactory;

}