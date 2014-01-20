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

package com.liferay.sync.engine.documentlibrary.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.sync.engine.model.File;

import java.util.List;
import java.util.Map;

/**
 * @author Shinn Lok
 */
public class GetFileEntrySyncDLObjectsEvent extends BaseEvent {

	public GetFileEntrySyncDLObjectsEvent(
		long accountId, Map<String, Object> parameters) {

		super(accountId, _URL_PATH, parameters);
	}

	@Override
	protected void processResponse(String response) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		List<File> files = objectMapper.readValue(
			response, new TypeReference<List<File>>() {});

		for (File file : files) {
			System.out.println(file);
		}
	}

	private static final String _URL_PATH =
		"/sync-web.syncdlobject/get-file-entry-sync-dl-objects";

}