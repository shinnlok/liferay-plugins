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

package com.liferay.chat.video.poller;

import com.liferay.chat.video.WebRTCClient;
import com.liferay.chat.video.WebRTCMail;
import com.liferay.chat.video.WebRTCMailbox;
import com.liferay.chat.video.WebRTCManager;
import com.liferay.chat.video.WebRTCManagerFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.poller.BasePollerProcessor;
import com.liferay.portal.kernel.poller.PollerRequest;
import com.liferay.portal.kernel.poller.PollerResponse;

import java.util.List;

/**
 * @author Philippe Proulx
 */
public class ChatVideoPollerProcessor extends BasePollerProcessor {

	@Override
	protected void doReceive(
			PollerRequest pollerRequest, PollerResponse pollerResponse)
		throws Exception {

		JSONObject webRTCResponseJSONObject =
			JSONFactoryUtil.createJSONObject();

		JSONArray webRTCClientsJSONArray = JSONFactoryUtil.createJSONArray();

		for (Long userId : _webRTCManager.getAvailableWebRTCClientIds()) {
			if (userId != pollerRequest.getUserId()) {
				webRTCClientsJSONArray.put(userId);
			}
		}

		webRTCResponseJSONObject.put("clients", webRTCClientsJSONArray);

		WebRTCClient webRTCClient = _webRTCManager.getWebRTCClient(
			pollerRequest.getUserId());

		JSONArray webRTCMailsJSONArray = JSONFactoryUtil.createJSONArray();

		if (webRTCClient != null) {
			WebRTCMailbox webRTCMailbox =
				webRTCClient.getOutgoingWebRTCMailbox();

			List<WebRTCMail> webRTCMails = webRTCMailbox.popWebRTCMails();

			for (WebRTCMail webRTCMail : webRTCMails) {
				JSONObject mailJSONObject = JSONFactoryUtil.createJSONObject();

				mailJSONObject.put(
					"message", webRTCMail.getMessageJSONObject());
				mailJSONObject.put(
					"sourceUserId", webRTCMail.getSourceUserId());
				mailJSONObject.put("type", webRTCMail.getMessageType());

				webRTCMailsJSONArray.put(mailJSONObject);
			}
		}

		webRTCResponseJSONObject.put("mails", webRTCMailsJSONArray);

		pollerResponse.setParameter("webRTCResponse", webRTCResponseJSONObject);
	}

	@Override
	protected void doSend(PollerRequest pollerRequest) throws Exception {
	}

	private WebRTCManager _webRTCManager =
		WebRTCManagerFactory.createWebRTCManager();

}