/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.opensaml.integration.internal.resolver;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.saml.opensaml.integration.resolver.AttributeResolver;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Mika Koivisto
 */
@Component(immediate = true, service = AttributeResolverRegistry.class)
public class AttributeResolverRegistry {

	public AttributeResolver getAttributeResolver(String entityId) {
		long companyId = CompanyThreadLocal.getCompanyId();

		AttributeResolver attributeResolver = _serviceTrackerMap.getService(
			companyId + "," + entityId);

		if (attributeResolver == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"No attribute resolver for company ID ", companyId,
						" and entity ID ", entityId));
			}

			attributeResolver = _defaultAttributeResolver;
		}

		return attributeResolver;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, AttributeResolver.class, "(companyId=*)",
			new DefaultServiceReferenceMapper(_log));
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AttributeResolverRegistry.class);

	@Reference(
		policyOption = ReferencePolicyOption.GREEDY, target = "(!(companyId=*))"
	)
	private AttributeResolver _defaultAttributeResolver;

	private ServiceTrackerMap<String, AttributeResolver> _serviceTrackerMap;

}