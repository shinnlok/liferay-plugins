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

package com.liferay.pushnotifications.sender.android;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.Sender;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.pushnotifications.sender.PushNotificationsSender;
import com.liferay.pushnotifications.util.PortletPropsValues;

import java.util.List;

/**
 * @author Silvio Santos
 * @author Bruno Farache
 */
public class AndroidPushNotificationsSender implements PushNotificationsSender {

	public AndroidPushNotificationsSender() {
		_sender = new Sender(PortletPropsValues.ANDROID_API_KEY);
	}

	@Override
	public void send(List<String> tokens, JSONObject jsonObject)
		throws Exception {

		Message message = buildMessage(jsonObject);

		_sender.send(message, tokens, PortletPropsValues.ANDROID_RETRIES);
	}

	protected Message buildMessage(JSONObject jsonObject) {
		Builder builder = new Builder();

		String entryTitle = jsonObject.getString("entryTitle");

		if (entryTitle != null) {
			builder.addData("data", entryTitle);
		}

		return builder.build();
	}

	private Sender _sender;

}