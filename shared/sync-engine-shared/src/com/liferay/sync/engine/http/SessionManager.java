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

package com.liferay.sync.engine.http;

import com.liferay.sync.engine.model.SyncAccount;
import com.liferay.sync.engine.service.SyncAccountService;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dennis Ju
 */
public class SessionManager {

	public static Session getSession(long syncAccountId) {
		Session session = _sessions.get(syncAccountId);

		if (session == null) {
			SyncAccount syncAccount = SyncAccountService.fetchSyncAccount(
				syncAccountId);

			try {
				URL url = new URL(syncAccount.getUrl());

				session = new Session(
					url, syncAccount.getLogin(), syncAccount.getPassword(),
					syncAccount.getSsoEnabled());
			}
			catch (MalformedURLException e) {
				_logger.error(e.getMessage(), e);

				return null;
			}

			_sessions.put(syncAccountId, session);
		}

		return session;
	}

	private static Logger _logger = LoggerFactory.getLogger(
		SessionManager.class);

	private static Map<Long, Session> _sessions = new HashMap<Long, Session>();

}