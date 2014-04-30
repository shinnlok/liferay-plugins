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

package com.liferay.sync.engine.documentlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.sync.engine.model.SyncSite;

import java.util.List;

/**
 * @author Dennis Ju
 */
@JsonIgnoreProperties(
	{"portalEELicenseDigest", "socialOfficeEELicenseDigest"})
public class SyncContext {

	public String getPluginVersion() {
		return _pluginVersion;
	}

	public int getPortalBuildNumber() {
		return _portalBuildNumber;
	}

	public List<SyncSite> getSyncSites() {
		return _syncSites;
	}

	public long getUserId() {
		return _userId;
	}

	public boolean isSocialOfficeInstalled() {
		return _socialOfficeInstalled;
	}

	public void setPluginVersion(String pluginVersion) {
		_pluginVersion = pluginVersion;
	}

	public void setPortalBuildNumber(int portalBuildNumber) {
		_portalBuildNumber = portalBuildNumber;
	}

	public void setSocialOfficeInstalled(boolean socialOfficeInstalled) {
		_socialOfficeInstalled = socialOfficeInstalled;
	}

	public void setSyncSites(List<SyncSite> syncSites) {
		_syncSites = syncSites;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	private String _pluginVersion;
	private int _portalBuildNumber;
	private boolean _socialOfficeInstalled;

	@JsonProperty("userSitesGroups")
	private List<SyncSite> _syncSites;

	private long _userId;

}