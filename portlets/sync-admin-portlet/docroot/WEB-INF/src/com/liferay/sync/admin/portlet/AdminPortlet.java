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

package com.liferay.sync.admin.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.sync.shared.util.PortletPropsKeys;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

/**
 * @author Shinn Lok
 */
public class AdminPortlet extends MVCPortlet {

	public void configureSite(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long groupId = ParamUtil.getLong(actionRequest, "groupId");

		boolean syncEnabled = ParamUtil.getBoolean(
			actionRequest, "syncEnabled");

		Group group = GroupLocalServiceUtil.fetchGroup(groupId);

		UnicodeProperties typeSettingsProperties =
			group.getTypeSettingsProperties();

		typeSettingsProperties.setProperty(
			"syncEnabled", String.valueOf(syncEnabled));

		group.setTypeSettingsProperties(typeSettingsProperties);

		GroupLocalServiceUtil.updateGroup(group);
	}

	public void submit(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		try {
			updatePreferences(actionRequest, actionResponse);

			addSuccessMessage(actionRequest, actionResponse);

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	protected void updatePreferences(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			CompanyThreadLocal.getCompanyId());

		int maxConnections = ParamUtil.getInteger(
			actionRequest, "maxConnections");

		portletPreferences.setValue(
			PortletPropsKeys.SYNC_CLIENT_MAX_CONNECTIONS,
			String.valueOf(maxConnections));

		int pollInterval = ParamUtil.getInteger(actionRequest, "pollInterval");

		portletPreferences.setValue(
			PortletPropsKeys.SYNC_CLIENT_POLL_INTERVAL,
			String.valueOf(pollInterval));

		boolean enabled = ParamUtil.getBoolean(actionRequest, "enabled");

		portletPreferences.setValue(
			PortletPropsKeys.SYNC_SERVICES_ENABLED, String.valueOf(enabled));

		portletPreferences.store();
	}

}