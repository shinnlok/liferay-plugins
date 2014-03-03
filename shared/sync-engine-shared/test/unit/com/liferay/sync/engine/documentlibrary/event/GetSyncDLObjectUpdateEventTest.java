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
import com.liferay.sync.engine.util.FilePathNameUtil;
import com.liferay.sync.engine.util.SyncFileTestUtil;

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
public class GetSyncDLObjectUpdateEventTest extends BaseTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_syncSite = SyncSiteService.addSyncSite(
			10158, filePathName + "/test-site", 10185,
			syncAccount.getSyncAccountId());

		_syncSite.setLastRemoteSyncTime(System.currentTimeMillis());
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
	public void testRunAddFile() throws Exception {
		setGetResponse("dependencies/get_sync_dl_object_update_event_add.json");
		setPostResponse(
			"dependencies/get_sync_dl_object_update_event_add.json");

		run();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Assert.assertTrue(Files.exists(filePath));
	}

	@Test
	public void testRunMoveFile() throws Exception {
		setPostResponse(
			"dependencies/get_sync_dl_object_update_event_move.json");

		Path sourceFilePath = Paths.get(
			_syncSite.getFilePathName() + "/Document_1.txt");

		Files.createFile(sourceFilePath);

		SyncFileTestUtil.addFileSyncFile(
			FilePathNameUtil.getFilePathName(sourceFilePath), 0,
			_syncSite.getGroupId(), syncAccount.getSyncAccountId(), 27382);

		Path folderFilePath = Paths.get(
			_syncSite.getFilePathName() + "/test-folder");

		Files.createDirectory(folderFilePath);

		SyncFileTestUtil.addFolderSyncFile(
			FilePathNameUtil.getFilePathName(folderFilePath), 0,
			_syncSite.getGroupId(), syncAccount.getSyncAccountId(), 27488);

		run();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(4, _syncFiles.size());

		Path targetFilePath = Paths.get(folderFilePath + "/test.txt");

		Assert.assertTrue(Files.exists(targetFilePath));
	}

	@Test
	public void testRunMoveFileToTrash() throws Exception {
		setPostResponse(
			"dependencies/get_sync_dl_object_update_event_trash.json");

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(filePath);

		SyncFileTestUtil.addFileSyncFile(
			FilePathNameUtil.getFilePathName(filePath), 0,
			_syncSite.getGroupId(), syncAccount.getSyncAccountId(), 27382);

		run();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(2, _syncFiles.size());

		Assert.assertFalse(Files.exists(filePath));
	}

	@Test
	public void testRunRestoreFileFromTrash() throws Exception {
		setGetResponse(
			"dependencies/get_sync_dl_object_update_event_restore.json");
		setPostResponse(
			"dependencies/get_sync_dl_object_update_event_restore.json");

		run();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Assert.assertTrue(Files.exists(filePath));
	}

	@Test
	public void testRunUpdateFile() throws Exception {
		setGetResponse(
			"dependencies/get_sync_dl_object_update_event_update.json");
		setPostResponse(
			"dependencies/get_sync_dl_object_update_event_update.json");

		Path sourceFilePath = Paths.get(
			_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(sourceFilePath);

		SyncFileTestUtil.addFileSyncFile(
			FilePathNameUtil.getFilePathName(sourceFilePath), 0,
			_syncSite.getGroupId(), syncAccount.getSyncAccountId(), 27382);

		run();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());

		Path targetFilePath = Paths.get(
			_syncSite.getFilePathName() + "/test2.txt");

		Assert.assertTrue(Files.exists(targetFilePath));

		SyncFile syncFile = SyncFileService.fetchSyncFile(
			FilePathNameUtil.getFilePathName(targetFilePath),
			syncAccount.getSyncAccountId());

		Assert.assertEquals("Updated Description", syncFile.getDescription());
	}

	protected void run() {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("companyId", _syncSite.getCompanyId());
		parameters.put("repositoryId", _syncSite.getGroupId());
		parameters.put("syncSite", _syncSite);

		GetSyncDLObjectUpdateEvent getSyncDLObjectUpdateEvent =
			new GetSyncDLObjectUpdateEvent(
				syncAccount.getSyncAccountId(), parameters);

		getSyncDLObjectUpdateEvent.run();
	}

	private List<SyncFile> _syncFiles;
	private SyncSite _syncSite;

}