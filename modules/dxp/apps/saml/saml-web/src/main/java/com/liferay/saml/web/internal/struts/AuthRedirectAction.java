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

package com.liferay.saml.web.internal.struts;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.saml.runtime.configuration.SamlProviderConfigurationHelper;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true, property = "path=/portal/saml/auth_redirect",
	service = StrutsAction.class
)
public class AuthRedirectAction extends BaseSamlStrutsAction {

	@Override
	public boolean isEnabled() {
		if (_samlProviderConfigurationHelper.isRoleSp()) {
			return _samlProviderConfigurationHelper.isEnabled();
		}

		return false;
	}

	@Override
	protected String doExecute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		String redirect = ParamUtil.getString(httpServletRequest, "redirect");

		redirect = _portal.escapeRedirect(redirect);

		if (Validator.isNull(redirect)) {
			redirect = _portal.getHomeURL(httpServletRequest);
		}

		try {
			httpServletResponse.sendRedirect(redirect);
		}
		catch (IOException ioException) {
			throw new SystemException(ioException);
		}

		return null;
	}

	@Reference
	private Portal _portal;

	@Reference
	private SamlProviderConfigurationHelper _samlProviderConfigurationHelper;

}