/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.wsrp.servlet;

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.HotDeployMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.util.BasePortalLifecycle;
import com.liferay.wsrp.service.ClpSerializer;
import com.liferay.wsrp.service.WSRPConsumerPortletLocalServiceUtil;
import com.liferay.wsrp.util.ExtensionHelperUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Brian Wing Shun Chan
 */
public class WSRPServletContextListener
	extends BasePortalLifecycle implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		portalDestroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		registerPortalLifecycle();
	}

	@Override
	protected void doPortalDestroy() throws Exception {
		MessageBusUtil.unregisterMessageListener(
			DestinationNames.HOT_DEPLOY, _messageListener);
	}

	@Override
	protected void doPortalInit() {
		_messageListener = new HotDeployMessageListener(
			ClpSerializer.getServletContextName()) {

			@Override
			protected void onDeploy(Message message) throws Exception {
				ExtensionHelperUtil.initialize();

				WSRPConsumerPortletLocalServiceUtil.
					destroyWSRPConsumerPortlets();

				WSRPConsumerPortletLocalServiceUtil.initWSRPConsumerPortlets();
			}

			@Override
			protected void onUndeploy(Message message) throws Exception {
				WSRPConsumerPortletLocalServiceUtil.
					destroyWSRPConsumerPortlets();
			}

		};

		MessageBusUtil.registerMessageListener(
			DestinationNames.HOT_DEPLOY, _messageListener);
	}

	private MessageListener _messageListener;

}