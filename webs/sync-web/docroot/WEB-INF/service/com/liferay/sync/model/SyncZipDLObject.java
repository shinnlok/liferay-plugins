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

package com.liferay.sync.model;

import com.liferay.portal.kernel.json.JSON;

/**
 * @author Dennis Ju
 */
@JSON
public class SyncZipDLObject extends SyncDLObjectWrapper {

	public SyncZipDLObject(SyncDLObject syncDLObject) {
		super(syncDLObject);
	}

	public String getException() {
		return _exception;
	}

	public long getZipId() {
		return _zipId;
	}

	public void setException(String exception) {
		_exception = exception;
	}

	public void setZipId(long zipId) {
		_zipId = zipId;
	}

	private String _exception;
	private long _zipId;

}