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

package com.liferay.sync.engine.documentlibrary.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.sync.engine.documentlibrary.event.Event;
import com.liferay.sync.engine.documentlibrary.event.GetFileEntrySyncDLObjectEvent;
import com.liferay.sync.engine.model.SyncAccount;
import com.liferay.sync.engine.model.SyncFile;
import com.liferay.sync.engine.service.SyncAccountService;
import com.liferay.sync.engine.service.SyncFileService;
import com.liferay.sync.engine.util.FilePathNameUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shinn Lok
 */
public class BaseJSONHandler extends BaseHandler {

	public BaseJSONHandler(Event event) {
		super(event);
	}

	@Override
	protected void doHandleResponse(HttpResponse httpResponse)
		throws Exception {

		HttpEntity httpEntity = httpResponse.getEntity();

		String response = EntityUtils.toString(httpEntity);

		if (!handlePortalException(response)) {
			processResponse(response);
		}
	}

	protected boolean handlePortalException(String response) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode responseJsonNode = null;

		try {
			responseJsonNode = objectMapper.readTree(response);
		}
		catch (Exception e) {
			return false;
		}

		JsonNode exceptionJsonNode = responseJsonNode.get("exception");

		if (exceptionJsonNode == null) {
			return false;
		}

		String exception = exceptionJsonNode.asText();

		if (_logger.isDebugEnabled()) {
			_logger.debug(exception);
		}

		if (exception.equals(
				"com.liferay.portal.kernel.upload.UploadException")) {

			SyncFile syncFile = (SyncFile)getParameterValue("syncFile");

			syncFile.setState(SyncFile.STATE_ERROR);
			syncFile.setUiEvent(SyncFile.UI_EVENT_EXCEEDED_SIZE_LIMIT);

			SyncFileService.update(syncFile);
		}
		else if (exception.equals(
					"com.liferay.portal.security.auth.PrincipalException")) {

			SyncFile syncFile = (SyncFile)getParameterValue("syncFile");

			syncFile.setState(SyncFile.STATE_ERROR);
			syncFile.setUiEvent(SyncFile.UI_EVENT_INVALID_PERMISSIONS);

			SyncFileService.update(syncFile);
		}
		else if (exception.equals(
					"com.liferay.portlet.documentlibrary." +
						"DuplicateFileException")) {

			SyncFile syncFile = (SyncFile)getParameterValue("syncFile");

			Path filePath = Paths.get(syncFile.getFilePathName());

			String parentFilePathName = FilePathNameUtil.getFilePathName(
				filePath.getParent());

			SyncFile parentSyncFile = SyncFileService.fetchSyncFile(
				parentFilePathName, getSyncAccountId());

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("folderId", parentSyncFile.getTypePK());
			parameters.put("groupId", parentSyncFile.getRepositoryId());
			parameters.put("syncFile", syncFile);
			parameters.put("title", syncFile.getName());

			GetFileEntrySyncDLObjectEvent getFileEntrySyncDLObjectEvent =
				new GetFileEntrySyncDLObjectEvent(
					getSyncAccountId(), parameters);

			getFileEntrySyncDLObjectEvent.run();

			SyncFileService.updateFileSyncFile(
				Paths.get(syncFile.getFilePathName()), getSyncAccountId(),
				syncFile, true);
		}
		else if (exception.equals(
					"com.liferay.portlet.documentlibrary." +
						"NoSuchFileEntryException")) {

			SyncFile syncFile = (SyncFile)getParameterValue("syncFile");

			Path filePath = Paths.get(syncFile.getFilePathName());

			Files.deleteIfExists(filePath);

			SyncFileService.deleteSyncFile(syncFile);
		}
		else if (exception.equals("java.lang.RuntimeException")) {
			SyncAccount syncAccount = SyncAccountService.fetchSyncAccount(
				getSyncAccountId());

			syncAccount.setActive(false);
			syncAccount.setState(SyncAccount.STATE_DISCONNECTED);
			syncAccount.setUiEvent(SyncAccount.UI_EVENT_SYNC_WEB_MISSING);

			SyncAccountService.update(syncAccount);
		}
		else if (exception.equals("java.lang.SecurityException")) {
			JsonNode messageJsonNode = responseJsonNode.get("message");

			String message = messageJsonNode.asText();

			if (message.equals("Authenticated access required")) {
				throw new HttpResponseException(
					HttpServletResponse.SC_UNAUTHORIZED, message);
			}
		}

		return true;
	}

	protected void processResponse(String response) throws Exception {
	}

	private static Logger _logger = LoggerFactory.getLogger(
		BaseJSONHandler.class);

}