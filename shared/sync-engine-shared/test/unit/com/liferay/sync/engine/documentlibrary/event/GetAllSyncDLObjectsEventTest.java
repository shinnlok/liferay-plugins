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

import com.liferay.sync.engine.BaseTestCase;
import com.liferay.sync.engine.model.SyncFile;
import com.liferay.sync.engine.model.SyncSite;
import com.liferay.sync.engine.service.SyncFileService;
import com.liferay.sync.engine.service.SyncSiteService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Shinn Lok
 */
@RunWith(PowerMockRunner.class)
public class GetAllSyncDLObjectsEventTest extends BaseTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_syncSite = SyncSiteService.addSyncSite(
			10158, filePathName + "/test-site", 10185,
			syncAccount.getSyncAccountId());
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

		SyncSiteService.deleteSyncSite(_syncSite.getSyncSiteId());

		for (SyncFile syncFile : _syncFiles) {
			SyncFileService.deleteSyncFile(syncFile.getSyncFileId());
		}
	}

	@Test
	public void testRun() throws Exception {
		setGetResponse("dependencies/get_all_sync_dl_objects.json");
		setPostResponse("dependencies/get_all_sync_dl_objects.json");

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("folderId", 0);
		parameters.put("repositoryId", _syncSite.getGroupId());

		GetAllSyncDLObjectsEvent getAllSyncDLObjectsEvent =
			new GetAllSyncDLObjectsEvent(
				syncAccount.getSyncAccountId(), parameters);

		getAllSyncDLObjectsEvent.run();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Assert.assertTrue(Files.exists(filePath));
	}

	private List<SyncFile> _syncFiles;
	private SyncSite _syncSite;

}