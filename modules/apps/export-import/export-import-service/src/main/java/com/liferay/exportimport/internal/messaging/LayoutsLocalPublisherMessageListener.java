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

package com.liferay.exportimport.internal.messaging;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.io.Serializable;

import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Daniel Kocsis
 */
@Component(
	immediate = true,
	property = "destination.name=" + DestinationNames.LAYOUTS_LOCAL_PUBLISHER,
	service = LayoutsLocalPublisherMessageListener.class
)
public class LayoutsLocalPublisherMessageListener
	extends BasePublisherMessageListener {

	@Override
	public void receive(Message message) throws MessageListenerException {
		long exportImportConfigurationId = GetterUtil.getLong(
			message.getPayload());

		ExportImportConfiguration exportImportConfiguration =
			_exportImportConfigurationLocalService.
				fetchExportImportConfiguration(exportImportConfigurationId);

		if (exportImportConfiguration == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to find export import configuration with ID " +
						exportImportConfigurationId);
			}

			return;
		}

		Map<String, Serializable> settingsMap =
			exportImportConfiguration.getSettingsMap();

		long userId = MapUtil.getLong(settingsMap, "userId");
		long sourceGroupId = MapUtil.getLong(settingsMap, "sourceGroupId");
		long targetGroupId = MapUtil.getLong(settingsMap, "targetGroupId");
		boolean privateLayout = MapUtil.getBoolean(
			settingsMap, "privateLayout");
		long[] layoutIds = GetterUtil.getLongValues(
			settingsMap.get("layoutIds"));
		Map<String, String[]> parameterMap =
			(Map<String, String[]>)settingsMap.get("parameterMap");

		try {
			initThreadLocals(userId, parameterMap);

			_staging.publishLayouts(
				userId, sourceGroupId, targetGroupId, privateLayout, layoutIds,
				exportImportConfiguration.getName(), parameterMap);
		}
		catch (PortalException portalException) {
			throw new MessageListenerException(portalException);
		}
		finally {
			resetThreadLocals();
		}
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		initialize(componentContext);
	}

	@Deactivate
	protected void deactivate() {
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutsLocalPublisherMessageListener.class);

	@Reference
	private ExportImportConfigurationLocalService
		_exportImportConfigurationLocalService;

	@Reference(
		target = "(&(release.bundle.symbolic.name=com.liferay.exportimport.service)(release.schema.version=1.0.2))"
	)
	private Release _release;

	@Reference
	private Staging _staging;

}