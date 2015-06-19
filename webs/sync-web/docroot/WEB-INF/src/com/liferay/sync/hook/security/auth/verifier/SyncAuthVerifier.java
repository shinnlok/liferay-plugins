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

package com.liferay.sync.hook.security.auth.verifier;

import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.security.auto.login.AutoLoginException;
import com.liferay.portal.kernel.security.auto.login.BaseAutoLogin;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.AccessControlContext;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.sync.util.PortletPropsValues;
import com.liferay.sync.util.SyncAuthTokenUtil;

import java.lang.reflect.Method;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Michael Young
 * @author Dennis Ju
 */
public class SyncAuthVerifier extends BaseAutoLogin implements AuthVerifier {

	@Override
	public String getAuthType() {
		return SyncAuthVerifier.class.getSimpleName();
	}

	@Override
	public AuthVerifierResult verify(
			AccessControlContext accessControlContext, Properties properties)
		throws AuthException {

		try {
			AuthVerifierResult authVerifierResult = new AuthVerifierResult();

			String[] credentials = login(
				accessControlContext.getRequest(),
				accessControlContext.getResponse());

			if (credentials != null) {
				authVerifierResult.setPassword(credentials[1]);
				authVerifierResult.setState(AuthVerifierResult.State.SUCCESS);
				authVerifierResult.setUserId(
					GetterUtil.getLong(credentials[0]));
			}

			return authVerifierResult;
		}
		catch (AutoLoginException ale) {
			throw new AuthException(ale);
		}
	}

	@Override
	protected String[] doLogin(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String token = request.getHeader(_TOKEN_HEADER);

		if (Validator.isNotNull(token)) {
			String userId = SyncAuthTokenUtil.getUserId(token);

			if (userId != null) {
				return new String[] {userId, null};
			}
		}

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		ClassLoader portalClassLoader = PortalClassLoaderUtil.getClassLoader();

		try {
			currentThread.setContextClassLoader(portalClassLoader);

			for (String autoLoginClassName : _autoLoginClassNames) {
				Class<?> clazz = portalClassLoader.loadClass(
					autoLoginClassName);

				Object object = clazz.newInstance();

				Method method = ReflectionUtil.getDeclaredMethod(
					clazz, "doLogin", HttpServletRequest.class,
					HttpServletResponse.class);

				String[] credentials = (String[])method.invoke(
					object, request, response);

				if (credentials != null) {
					token = SyncAuthTokenUtil.createToken(
						GetterUtil.getLong(credentials[0]));

					if (token != null) {
						response.addHeader(_TOKEN_HEADER, token);
					}

					return credentials;
				}
			}
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}

		return null;
	}

	private static final String _TOKEN_HEADER = "Sync-JWT";

	private static String[] _autoLoginClassNames = StringUtil.split(
		PortletPropsValues.SYNC_AUTH_VERIFIER_PIPELINE);

}