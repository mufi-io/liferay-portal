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

package com.liferay.object.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ObjectFieldLocalService}.
 *
 * @author Marco Leo
 * @see ObjectFieldLocalService
 * @generated
 */
public class ObjectFieldLocalServiceWrapper
	implements ObjectFieldLocalService,
			   ServiceWrapper<ObjectFieldLocalService> {

	public ObjectFieldLocalServiceWrapper() {
		this(null);
	}

	public ObjectFieldLocalServiceWrapper(
		ObjectFieldLocalService objectFieldLocalService) {

		_objectFieldLocalService = objectFieldLocalService;
	}

	@Override
	public com.liferay.object.model.ObjectField addCustomObjectField(
			String externalReferenceCode, long userId,
			long listTypeDefinitionId, long objectDefinitionId,
			String businessType, String dbType, String defaultValue,
			boolean indexed, boolean indexedAsKeyword, String indexedLanguageId,
			java.util.Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state,
			java.util.List<com.liferay.object.model.ObjectFieldSetting>
				objectFieldSettings)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.addCustomObjectField(
			externalReferenceCode, userId, listTypeDefinitionId,
			objectDefinitionId, businessType, dbType, defaultValue, indexed,
			indexedAsKeyword, indexedLanguageId, labelMap, name, required,
			state, objectFieldSettings);
	}

	/**
	 * Adds the object field to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectFieldLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectField the object field
	 * @return the object field that was added
	 */
	@Override
	public com.liferay.object.model.ObjectField addObjectField(
		com.liferay.object.model.ObjectField objectField) {

		return _objectFieldLocalService.addObjectField(objectField);
	}

	@Override
	public com.liferay.object.model.ObjectField addOrUpdateCustomObjectField(
			String externalReferenceCode, long userId, long objectDefinitionId,
			long listTypeDefinitionId, String businessType, String dbType,
			String defaultValue, boolean indexed, boolean indexedAsKeyword,
			String indexedLanguageId,
			java.util.Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state,
			java.util.List<com.liferay.object.model.ObjectFieldSetting>
				objectFieldSettings)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.addOrUpdateCustomObjectField(
			externalReferenceCode, userId, objectDefinitionId,
			listTypeDefinitionId, businessType, dbType, defaultValue, indexed,
			indexedAsKeyword, indexedLanguageId, labelMap, name, required,
			state, objectFieldSettings);
	}

	@Override
	public com.liferay.object.model.ObjectField addOrUpdateSystemObjectField(
			long userId, long objectDefinitionId, String businessType,
			String dbColumnName, String dbTableName, String dbType,
			String defaultValue, boolean indexed, boolean indexedAsKeyword,
			String indexedLanguageId,
			java.util.Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.addOrUpdateSystemObjectField(
			userId, objectDefinitionId, businessType, dbColumnName, dbTableName,
			dbType, defaultValue, indexed, indexedAsKeyword, indexedLanguageId,
			labelMap, name, required, state);
	}

	@Override
	public com.liferay.object.model.ObjectField addSystemObjectField(
			long userId, long objectDefinitionId, String businessType,
			String dbColumnName, String dbTableName, String dbType,
			String defaultValue, boolean indexed, boolean indexedAsKeyword,
			String indexedLanguageId,
			java.util.Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.addSystemObjectField(
			userId, objectDefinitionId, businessType, dbColumnName, dbTableName,
			dbType, defaultValue, indexed, indexedAsKeyword, indexedLanguageId,
			labelMap, name, required, state);
	}

	/**
	 * Creates a new object field with the primary key. Does not add the object field to the database.
	 *
	 * @param objectFieldId the primary key for the new object field
	 * @return the new object field
	 */
	@Override
	public com.liferay.object.model.ObjectField createObjectField(
		long objectFieldId) {

		return _objectFieldLocalService.createObjectField(objectFieldId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the object field with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectFieldLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectFieldId the primary key of the object field
	 * @return the object field that was removed
	 * @throws PortalException if a object field with the primary key could not be found
	 */
	@Override
	public com.liferay.object.model.ObjectField deleteObjectField(
			long objectFieldId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.deleteObjectField(objectFieldId);
	}

	/**
	 * Deletes the object field from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectFieldLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectField the object field
	 * @return the object field that was removed
	 * @throws PortalException
	 */
	@Override
	public com.liferay.object.model.ObjectField deleteObjectField(
			com.liferay.object.model.ObjectField objectField)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.deleteObjectField(objectField);
	}

	@Override
	public void deleteObjectFieldByObjectDefinitionId(Long objectDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_objectFieldLocalService.deleteObjectFieldByObjectDefinitionId(
			objectDefinitionId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.object.model.ObjectField
			deleteRelationshipTypeObjectField(long objectFieldId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.deleteRelationshipTypeObjectField(
			objectFieldId);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _objectFieldLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _objectFieldLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _objectFieldLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _objectFieldLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _objectFieldLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _objectFieldLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _objectFieldLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _objectFieldLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.object.model.ObjectField fetchObjectField(
		long objectFieldId) {

		return _objectFieldLocalService.fetchObjectField(objectFieldId);
	}

	@Override
	public com.liferay.object.model.ObjectField fetchObjectField(
		long objectDefinitionId, String name) {

		return _objectFieldLocalService.fetchObjectField(
			objectDefinitionId, name);
	}

	/**
	 * Returns the object field with the matching UUID and company.
	 *
	 * @param uuid the object field's UUID
	 * @param companyId the primary key of the company
	 * @return the matching object field, or <code>null</code> if a matching object field could not be found
	 */
	@Override
	public com.liferay.object.model.ObjectField
		fetchObjectFieldByUuidAndCompanyId(String uuid, long companyId) {

		return _objectFieldLocalService.fetchObjectFieldByUuidAndCompanyId(
			uuid, companyId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _objectFieldLocalService.getActionableDynamicQuery();
	}

	@Override
	public java.util.List<com.liferay.object.model.ObjectField>
			getActiveObjectFields(
				java.util.List<com.liferay.object.model.ObjectField>
					objectFields)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.getActiveObjectFields(objectFields);
	}

	@Override
	public com.liferay.petra.sql.dsl.Column<?, ?> getColumn(
		long objectDefinitionId, String name) {

		return _objectFieldLocalService.getColumn(objectDefinitionId, name);
	}

	@Override
	public java.util.List<com.liferay.object.model.ObjectField>
		getCustomObjectFields(long objectFieldId) {

		return _objectFieldLocalService.getCustomObjectFields(objectFieldId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _objectFieldLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _objectFieldLocalService.getIndexableActionableDynamicQuery();
	}

	@Override
	public java.util.List<com.liferay.object.model.ObjectField>
		getListTypeDefinitionObjectFields(
			long listTypeDefinitionId, boolean state) {

		return _objectFieldLocalService.getListTypeDefinitionObjectFields(
			listTypeDefinitionId, state);
	}

	/**
	 * Returns the object field with the primary key.
	 *
	 * @param objectFieldId the primary key of the object field
	 * @return the object field
	 * @throws PortalException if a object field with the primary key could not be found
	 */
	@Override
	public com.liferay.object.model.ObjectField getObjectField(
			long objectFieldId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.getObjectField(objectFieldId);
	}

	@Override
	public com.liferay.object.model.ObjectField getObjectField(
			long objectDefinitionId, String name)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.getObjectField(
			objectDefinitionId, name);
	}

	/**
	 * Returns the object field with the matching UUID and company.
	 *
	 * @param uuid the object field's UUID
	 * @param companyId the primary key of the company
	 * @return the matching object field
	 * @throws PortalException if a matching object field could not be found
	 */
	@Override
	public com.liferay.object.model.ObjectField
			getObjectFieldByUuidAndCompanyId(String uuid, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.getObjectFieldByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of all the object fields.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectFieldModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object fields
	 * @param end the upper bound of the range of object fields (not inclusive)
	 * @return the range of object fields
	 */
	@Override
	public java.util.List<com.liferay.object.model.ObjectField> getObjectFields(
		int start, int end) {

		return _objectFieldLocalService.getObjectFields(start, end);
	}

	@Override
	public java.util.List<com.liferay.object.model.ObjectField> getObjectFields(
		long objectDefinitionId) {

		return _objectFieldLocalService.getObjectFields(objectDefinitionId);
	}

	@Override
	public java.util.List<com.liferay.object.model.ObjectField> getObjectFields(
		long objectDefinitionId, boolean system) {

		return _objectFieldLocalService.getObjectFields(
			objectDefinitionId, system);
	}

	@Override
	public java.util.List<com.liferay.object.model.ObjectField> getObjectFields(
		long objectDefinitionId, String dbTableName) {

		return _objectFieldLocalService.getObjectFields(
			objectDefinitionId, dbTableName);
	}

	/**
	 * Returns the number of object fields.
	 *
	 * @return the number of object fields
	 */
	@Override
	public int getObjectFieldsCount() {
		return _objectFieldLocalService.getObjectFieldsCount();
	}

	@Override
	public int getObjectFieldsCount(long objectDefinitionId) {
		return _objectFieldLocalService.getObjectFieldsCount(
			objectDefinitionId);
	}

	@Override
	public int getObjectFieldsCountByListTypeDefinitionId(
		long listTypeDefinitionId) {

		return _objectFieldLocalService.
			getObjectFieldsCountByListTypeDefinitionId(listTypeDefinitionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _objectFieldLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public com.liferay.petra.sql.dsl.Table getTable(
			long objectDefinitionId, String name)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.getTable(objectDefinitionId, name);
	}

	@Override
	public com.liferay.object.model.ObjectField updateCustomObjectField(
			String externalReferenceCode, long objectFieldId,
			long listTypeDefinitionId, String businessType, String dbType,
			String defaultValue, boolean indexed, boolean indexedAsKeyword,
			String indexedLanguageId,
			java.util.Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state,
			java.util.List<com.liferay.object.model.ObjectFieldSetting>
				objectFieldSettings)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.updateCustomObjectField(
			externalReferenceCode, objectFieldId, listTypeDefinitionId,
			businessType, dbType, defaultValue, indexed, indexedAsKeyword,
			indexedLanguageId, labelMap, name, required, state,
			objectFieldSettings);
	}

	/**
	 * Updates the object field in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectFieldLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectField the object field
	 * @return the object field that was updated
	 */
	@Override
	public com.liferay.object.model.ObjectField updateObjectField(
		com.liferay.object.model.ObjectField objectField) {

		return _objectFieldLocalService.updateObjectField(objectField);
	}

	@Override
	public com.liferay.object.model.ObjectField updateObjectField(
			String externalReferenceCode, long userId, long objectDefinitionId,
			long objectFieldId, long listTypeDefinitionId, String businessType,
			String dbColumnName, String dbTableName, String dbType,
			String defaultValue, boolean indexed, boolean indexedAsKeyword,
			String indexedLanguageId,
			java.util.Map<java.util.Locale, String> labelMap, String name,
			boolean required, boolean state, boolean system,
			java.util.List<com.liferay.object.model.ObjectFieldSetting>
				objectFieldSettings)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.updateObjectField(
			externalReferenceCode, userId, objectDefinitionId, objectFieldId,
			listTypeDefinitionId, businessType, dbColumnName, dbTableName,
			dbType, defaultValue, indexed, indexedAsKeyword, indexedLanguageId,
			labelMap, name, required, state, system, objectFieldSettings);
	}

	@Override
	public com.liferay.object.model.ObjectField updateRequired(
			long objectFieldId, boolean required)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _objectFieldLocalService.updateRequired(objectFieldId, required);
	}

	@Override
	public ObjectFieldLocalService getWrappedService() {
		return _objectFieldLocalService;
	}

	@Override
	public void setWrappedService(
		ObjectFieldLocalService objectFieldLocalService) {

		_objectFieldLocalService = objectFieldLocalService;
	}

	private ObjectFieldLocalService _objectFieldLocalService;

}