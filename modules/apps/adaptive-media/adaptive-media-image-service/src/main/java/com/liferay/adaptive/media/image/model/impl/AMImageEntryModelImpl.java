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

package com.liferay.adaptive.media.image.model.impl;

import com.liferay.adaptive.media.image.model.AMImageEntry;
import com.liferay.adaptive.media.image.model.AMImageEntryModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the AMImageEntry service. Represents a row in the &quot;AMImageEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>AMImageEntryModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AMImageEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AMImageEntryImpl
 * @generated
 */
public class AMImageEntryModelImpl
	extends BaseModelImpl<AMImageEntry> implements AMImageEntryModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a am image entry model instance should use the <code>AMImageEntry</code> interface instead.
	 */
	public static final String TABLE_NAME = "AMImageEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"amImageEntryId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"createDate", Types.TIMESTAMP}, {"configurationUuid", Types.VARCHAR},
		{"fileVersionId", Types.BIGINT}, {"mimeType", Types.VARCHAR},
		{"height", Types.INTEGER}, {"width", Types.INTEGER},
		{"size_", Types.BIGINT}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("amImageEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("configurationUuid", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("fileVersionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("mimeType", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("height", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("width", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("size_", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE =
		"create table AMImageEntry (uuid_ VARCHAR(75) null,amImageEntryId LONG not null primary key,groupId LONG,companyId LONG,createDate DATE null,configurationUuid VARCHAR(75) null,fileVersionId LONG,mimeType VARCHAR(75) null,height INTEGER,width INTEGER,size_ LONG)";

	public static final String TABLE_SQL_DROP = "drop table AMImageEntry";

	public static final String ORDER_BY_JPQL =
		" ORDER BY amImageEntry.amImageEntryId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY AMImageEntry.amImageEntryId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long CONFIGURATIONUUID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long FILEVERSIONID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long GROUPID_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long AMIMAGEENTRYID_COLUMN_BITMASK = 32L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	public AMImageEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _amImageEntryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAmImageEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _amImageEntryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AMImageEntry.class;
	}

	@Override
	public String getModelClassName() {
		return AMImageEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AMImageEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AMImageEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AMImageEntry, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((AMImageEntry)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AMImageEntry, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AMImageEntry, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(AMImageEntry)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<AMImageEntry, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AMImageEntry, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<AMImageEntry, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<AMImageEntry, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<AMImageEntry, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<AMImageEntry, Object>>();
		Map<String, BiConsumer<AMImageEntry, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<AMImageEntry, ?>>();

		attributeGetterFunctions.put("uuid", AMImageEntry::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<AMImageEntry, String>)AMImageEntry::setUuid);
		attributeGetterFunctions.put(
			"amImageEntryId", AMImageEntry::getAmImageEntryId);
		attributeSetterBiConsumers.put(
			"amImageEntryId",
			(BiConsumer<AMImageEntry, Long>)AMImageEntry::setAmImageEntryId);
		attributeGetterFunctions.put("groupId", AMImageEntry::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<AMImageEntry, Long>)AMImageEntry::setGroupId);
		attributeGetterFunctions.put("companyId", AMImageEntry::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<AMImageEntry, Long>)AMImageEntry::setCompanyId);
		attributeGetterFunctions.put("createDate", AMImageEntry::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<AMImageEntry, Date>)AMImageEntry::setCreateDate);
		attributeGetterFunctions.put(
			"configurationUuid", AMImageEntry::getConfigurationUuid);
		attributeSetterBiConsumers.put(
			"configurationUuid",
			(BiConsumer<AMImageEntry, String>)
				AMImageEntry::setConfigurationUuid);
		attributeGetterFunctions.put(
			"fileVersionId", AMImageEntry::getFileVersionId);
		attributeSetterBiConsumers.put(
			"fileVersionId",
			(BiConsumer<AMImageEntry, Long>)AMImageEntry::setFileVersionId);
		attributeGetterFunctions.put("mimeType", AMImageEntry::getMimeType);
		attributeSetterBiConsumers.put(
			"mimeType",
			(BiConsumer<AMImageEntry, String>)AMImageEntry::setMimeType);
		attributeGetterFunctions.put("height", AMImageEntry::getHeight);
		attributeSetterBiConsumers.put(
			"height",
			(BiConsumer<AMImageEntry, Integer>)AMImageEntry::setHeight);
		attributeGetterFunctions.put("width", AMImageEntry::getWidth);
		attributeSetterBiConsumers.put(
			"width", (BiConsumer<AMImageEntry, Integer>)AMImageEntry::setWidth);
		attributeGetterFunctions.put("size", AMImageEntry::getSize);
		attributeSetterBiConsumers.put(
			"size", (BiConsumer<AMImageEntry, Long>)AMImageEntry::setSize);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_uuid = uuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalUuid() {
		return getColumnOriginalValue("uuid_");
	}

	@Override
	public long getAmImageEntryId() {
		return _amImageEntryId;
	}

	@Override
	public void setAmImageEntryId(long amImageEntryId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_amImageEntryId = amImageEntryId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_groupId = groupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalGroupId() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("groupId"));
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCompanyId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("companyId"));
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_createDate = createDate;
	}

	@Override
	public String getConfigurationUuid() {
		if (_configurationUuid == null) {
			return "";
		}
		else {
			return _configurationUuid;
		}
	}

	@Override
	public void setConfigurationUuid(String configurationUuid) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_configurationUuid = configurationUuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalConfigurationUuid() {
		return getColumnOriginalValue("configurationUuid");
	}

	@Override
	public long getFileVersionId() {
		return _fileVersionId;
	}

	@Override
	public void setFileVersionId(long fileVersionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_fileVersionId = fileVersionId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalFileVersionId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("fileVersionId"));
	}

	@Override
	public String getMimeType() {
		if (_mimeType == null) {
			return "";
		}
		else {
			return _mimeType;
		}
	}

	@Override
	public void setMimeType(String mimeType) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mimeType = mimeType;
	}

	@Override
	public int getHeight() {
		return _height;
	}

	@Override
	public void setHeight(int height) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_height = height;
	}

	@Override
	public int getWidth() {
		return _width;
	}

	@Override
	public void setWidth(int width) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_width = width;
	}

	@Override
	public long getSize() {
		return _size;
	}

	@Override
	public void setSize(long size) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_size = size;
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (!Objects.equals(
					entry.getValue(), getColumnValue(entry.getKey()))) {

				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), AMImageEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AMImageEntry toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, AMImageEntry>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AMImageEntryImpl amImageEntryImpl = new AMImageEntryImpl();

		amImageEntryImpl.setUuid(getUuid());
		amImageEntryImpl.setAmImageEntryId(getAmImageEntryId());
		amImageEntryImpl.setGroupId(getGroupId());
		amImageEntryImpl.setCompanyId(getCompanyId());
		amImageEntryImpl.setCreateDate(getCreateDate());
		amImageEntryImpl.setConfigurationUuid(getConfigurationUuid());
		amImageEntryImpl.setFileVersionId(getFileVersionId());
		amImageEntryImpl.setMimeType(getMimeType());
		amImageEntryImpl.setHeight(getHeight());
		amImageEntryImpl.setWidth(getWidth());
		amImageEntryImpl.setSize(getSize());

		amImageEntryImpl.resetOriginalValues();

		return amImageEntryImpl;
	}

	@Override
	public AMImageEntry cloneWithOriginalValues() {
		AMImageEntryImpl amImageEntryImpl = new AMImageEntryImpl();

		amImageEntryImpl.setUuid(this.<String>getColumnOriginalValue("uuid_"));
		amImageEntryImpl.setAmImageEntryId(
			this.<Long>getColumnOriginalValue("amImageEntryId"));
		amImageEntryImpl.setGroupId(
			this.<Long>getColumnOriginalValue("groupId"));
		amImageEntryImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		amImageEntryImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		amImageEntryImpl.setConfigurationUuid(
			this.<String>getColumnOriginalValue("configurationUuid"));
		amImageEntryImpl.setFileVersionId(
			this.<Long>getColumnOriginalValue("fileVersionId"));
		amImageEntryImpl.setMimeType(
			this.<String>getColumnOriginalValue("mimeType"));
		amImageEntryImpl.setHeight(
			this.<Integer>getColumnOriginalValue("height"));
		amImageEntryImpl.setWidth(
			this.<Integer>getColumnOriginalValue("width"));
		amImageEntryImpl.setSize(this.<Long>getColumnOriginalValue("size_"));

		return amImageEntryImpl;
	}

	@Override
	public int compareTo(AMImageEntry amImageEntry) {
		long primaryKey = amImageEntry.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AMImageEntry)) {
			return false;
		}

		AMImageEntry amImageEntry = (AMImageEntry)object;

		long primaryKey = amImageEntry.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<AMImageEntry> toCacheModel() {
		AMImageEntryCacheModel amImageEntryCacheModel =
			new AMImageEntryCacheModel();

		amImageEntryCacheModel.uuid = getUuid();

		String uuid = amImageEntryCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			amImageEntryCacheModel.uuid = null;
		}

		amImageEntryCacheModel.amImageEntryId = getAmImageEntryId();

		amImageEntryCacheModel.groupId = getGroupId();

		amImageEntryCacheModel.companyId = getCompanyId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			amImageEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			amImageEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		amImageEntryCacheModel.configurationUuid = getConfigurationUuid();

		String configurationUuid = amImageEntryCacheModel.configurationUuid;

		if ((configurationUuid != null) && (configurationUuid.length() == 0)) {
			amImageEntryCacheModel.configurationUuid = null;
		}

		amImageEntryCacheModel.fileVersionId = getFileVersionId();

		amImageEntryCacheModel.mimeType = getMimeType();

		String mimeType = amImageEntryCacheModel.mimeType;

		if ((mimeType != null) && (mimeType.length() == 0)) {
			amImageEntryCacheModel.mimeType = null;
		}

		amImageEntryCacheModel.height = getHeight();

		amImageEntryCacheModel.width = getWidth();

		amImageEntryCacheModel.size = getSize();

		return amImageEntryCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<AMImageEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<AMImageEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AMImageEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((AMImageEntry)this);

			if (value == null) {
				sb.append("null");
			}
			else if (value instanceof Blob || value instanceof Date ||
					 value instanceof Map || value instanceof String) {

				sb.append(
					"\"" + StringUtil.replace(value.toString(), "\"", "'") +
						"\"");
			}
			else {
				sb.append(value);
			}

			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, AMImageEntry>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					AMImageEntry.class, ModelWrapper.class);

	}

	private String _uuid;
	private long _amImageEntryId;
	private long _groupId;
	private long _companyId;
	private Date _createDate;
	private String _configurationUuid;
	private long _fileVersionId;
	private String _mimeType;
	private int _height;
	private int _width;
	private long _size;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<AMImageEntry, Object> function = _attributeGetterFunctions.get(
			columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((AMImageEntry)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put("amImageEntryId", _amImageEntryId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("configurationUuid", _configurationUuid);
		_columnOriginalValues.put("fileVersionId", _fileVersionId);
		_columnOriginalValues.put("mimeType", _mimeType);
		_columnOriginalValues.put("height", _height);
		_columnOriginalValues.put("width", _width);
		_columnOriginalValues.put("size_", _size);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");
		attributeNames.put("size_", "size");

		_attributeNames = Collections.unmodifiableMap(attributeNames);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("uuid_", 1L);

		columnBitmasks.put("amImageEntryId", 2L);

		columnBitmasks.put("groupId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("createDate", 16L);

		columnBitmasks.put("configurationUuid", 32L);

		columnBitmasks.put("fileVersionId", 64L);

		columnBitmasks.put("mimeType", 128L);

		columnBitmasks.put("height", 256L);

		columnBitmasks.put("width", 512L);

		columnBitmasks.put("size_", 1024L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private AMImageEntry _escapedModel;

}