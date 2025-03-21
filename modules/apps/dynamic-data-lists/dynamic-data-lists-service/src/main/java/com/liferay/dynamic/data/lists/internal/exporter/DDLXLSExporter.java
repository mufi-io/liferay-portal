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

package com.liferay.dynamic.data.lists.internal.exporter;

import com.liferay.dynamic.data.lists.exporter.DDLExporter;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordVersion;
import com.liferay.dynamic.data.lists.service.DDLRecordLocalService;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.lists.service.DDLRecordSetVersionService;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldValueRendererRegistry;
import com.liferay.dynamic.data.mapping.storage.StorageEngine;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.ByteArrayOutputStream;

import java.time.format.DateTimeFormatter;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(service = DDLExporter.class)
public class DDLXLSExporter extends BaseDDLExporter {

	@Override
	public String getFormat() {
		return "xls";
	}

	@Override
	protected byte[] doExport(
			long recordSetId, int status, int start, int end,
			OrderByComparator<DDLRecord> orderByComparator)
		throws Exception {

		DDLRecordSet recordSet = _ddlRecordSetService.getRecordSet(recordSetId);

		Map<String, DDMFormField> ddmFormFields = getDistinctFields(
			recordSetId);

		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();

		try (ByteArrayOutputStream byteArrayOutputStream =
				new ByteArrayOutputStream();
			Workbook workbook = new HSSFWorkbook()) {

			Sheet sheet = workbook.createSheet();

			_createHeaderRow(ddmFormFields.values(), sheet, workbook);

			List<DDLRecord> records = _ddlRecordLocalService.getRecords(
				recordSetId, status, start, end, orderByComparator);

			Iterator<DDLRecord> iterator = records.iterator();

			int rowIndex = 1;

			CellStyle cellStyle = _createCellStyle(
				workbook, false, "Courier New", (short)12);

			while (iterator.hasNext()) {
				DDLRecord record = iterator.next();

				DDLRecordVersion recordVersion = record.getRecordVersion();

				Map<String, DDMFormFieldRenderedValue> values =
					getRenderedValues(
						recordSet.getScope(), ddmFormFields.values(),
						_storageEngine.getDDMFormValues(
							recordVersion.getDDMStorageId()),
						_htmlParser);

				_createDataRow(
					rowIndex++, sheet, dateTimeFormatter,
					recordVersion.getUserName(),
					getStatusMessage(recordVersion.getStatus()),
					recordVersion.getStatusDate(), cellStyle, ddmFormFields,
					values);
			}

			workbook.write(byteArrayOutputStream);

			return byteArrayOutputStream.toByteArray();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return new byte[0];
		}
	}

	@Override
	protected DDLRecordSetVersionService getDDLRecordSetVersionService() {
		return _ddlRecordSetVersionService;
	}

	@Override
	protected DDMFormFieldTypeServicesTracker
		getDDMFormFieldTypeServicesTracker() {

		return _ddmFormFieldTypeServicesTracker;
	}

	@Override
	protected DDMFormFieldValueRendererRegistry
		getDDMFormFieldValueRendererRegistry() {

		return _ddmFormFieldValueRendererRegistry;
	}

	private CellStyle _createCellStyle(
		Workbook workbook, boolean bold, String fontName,
		short heightInPoints) {

		Font font = workbook.createFont();

		font.setBold(bold);
		font.setFontHeightInPoints(heightInPoints);
		font.setFontName(fontName);

		CellStyle style = workbook.createCellStyle();

		style.setFont(font);

		return style;
	}

	private void _createDataRow(
		int rowIndex, Sheet sheet, DateTimeFormatter dateTimeFormatter,
		String author, String status, Date statusDate, CellStyle style,
		Map<String, DDMFormField> ddmFormFields,
		Map<String, DDMFormFieldRenderedValue> values) {

		Row row = sheet.createRow(rowIndex);

		int cellIndex = 0;

		Cell cell;

		for (Map.Entry<String, DDMFormField> entry : ddmFormFields.entrySet()) {
			cell = row.createCell(cellIndex++, CellType.STRING);

			cell.setCellStyle(style);

			if (values.containsKey(entry.getKey())) {
				DDMFormFieldRenderedValue ddmFormFieldRenderedValue =
					values.get(entry.getKey());

				cell.setCellValue(
					GetterUtil.getString(ddmFormFieldRenderedValue.getValue()));
			}
			else {
				cell.setCellValue(StringPool.BLANK);
			}
		}

		cell = row.createCell(cellIndex++, CellType.STRING);

		cell.setCellStyle(style);
		cell.setCellValue(status);

		cell = row.createCell(cellIndex++, CellType.STRING);

		cell.setCellStyle(style);
		cell.setCellValue(formatDate(statusDate, dateTimeFormatter));

		cell = row.createCell(cellIndex++, CellType.STRING);

		cell.setCellStyle(style);
		cell.setCellValue(author);
	}

	private void _createHeaderRow(
		Collection<DDMFormField> ddmFormFields, Sheet sheet,
		Workbook workbook) {

		Row row = sheet.createRow(0);

		CellStyle cellStyle = _createCellStyle(
			workbook, true, "Courier New", (short)14);

		int cellIndex = 0;

		Cell cell;

		Locale locale = getLocale();

		for (DDMFormField ddmFormField : ddmFormFields) {
			LocalizedValue label = ddmFormField.getLabel();

			cell = row.createCell(cellIndex++, CellType.STRING);

			cell.setCellStyle(cellStyle);
			cell.setCellValue(label.getString(locale));
		}

		cell = row.createCell(cellIndex++, CellType.STRING);

		cell.setCellStyle(cellStyle);
		cell.setCellValue(_language.get(locale, "status"));

		cell = row.createCell(cellIndex++, CellType.STRING);

		cell.setCellStyle(cellStyle);
		cell.setCellValue(_language.get(locale, "modified-date"));

		cell = row.createCell(cellIndex++, CellType.STRING);

		cell.setCellStyle(cellStyle);
		cell.setCellValue(_language.get(locale, "author"));
	}

	private static final Log _log = LogFactoryUtil.getLog(DDLXLSExporter.class);

	@Reference
	private DDLRecordLocalService _ddlRecordLocalService;

	@Reference
	private DDLRecordSetService _ddlRecordSetService;

	@Reference
	private DDLRecordSetVersionService _ddlRecordSetVersionService;

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private DDMFormFieldValueRendererRegistry
		_ddmFormFieldValueRendererRegistry;

	@Reference
	private HtmlParser _htmlParser;

	@Reference
	private Language _language;

	@Reference
	private StorageEngine _storageEngine;

}