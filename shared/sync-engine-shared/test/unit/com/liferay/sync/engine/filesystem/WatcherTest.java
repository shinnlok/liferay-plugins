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

package com.liferay.sync.engine.filesystem;

import com.liferay.sync.engine.BaseTestCase;
import com.liferay.sync.engine.model.SyncFile;
import com.liferay.sync.engine.model.SyncSite;
import com.liferay.sync.engine.model.SyncWatchEvent;
import com.liferay.sync.engine.service.SyncAccountService;
import com.liferay.sync.engine.service.SyncFileService;
import com.liferay.sync.engine.service.SyncSiteService;
import com.liferay.sync.engine.service.SyncWatchEventService;
import com.liferay.sync.engine.util.FilePathNameUtil;
import com.liferay.sync.engine.util.FileUtil;
import com.liferay.sync.engine.util.OSDetector;

import java.io.BufferedWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Michael Young
 */
@RunWith(PowerMockRunner.class)
public class WatcherTest extends BaseTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_syncSite = SyncSiteService.addSyncSite(
			10158, filePathName + "/test-site", 10184,
			syncAccount.getSyncAccountId());

		ScheduledExecutorService scheduledExecutorService =
			Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(
			new SyncWatchEventProcessor(), 0, 1, TimeUnit.SECONDS);

		WatchEventListener watchEventListener = new SyncSiteWatchEventListener(
			syncAccount.getSyncAccountId());

		Path filePath = Paths.get(syncAccount.getFilePathName());

		_watcher = new Watcher(filePath, true, watchEventListener);

		Thread thread = new Thread(_watcher);

		thread.start();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		_watcher.close();

		super.tearDown();

		SyncAccountService.deleteSyncAccount(syncAccount.getSyncAccountId());

		SyncSiteService.deleteSyncSite(_syncSite.getSyncSiteId());

		for (SyncFile syncFile : _syncFiles) {
			SyncFileService.deleteSyncFile(syncFile.getSyncFileId());
		}

		for (SyncWatchEvent syncWatchEvent : SyncWatchEventService.findAll()) {
			SyncWatchEventService.deleteSyncWatchEvent(
				syncWatchEvent.getSyncWatchEventId());
		}
	}

	@Test
	public void testRunAddFile() throws Exception {
		setPostResponse("dependencies/watcher_test_add_file.json");

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(filePath);

		sleep();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());
	}

	@Test
	public void testRunAddIgnoredFile() throws Exception {
		setPostResponse("dependencies/watcher_test_add_file.json");

		if (OSDetector.isWindows()) {
			Path hiddenFilePath = Paths.get(
				_syncSite.getFilePathName() + "/hidden_file.txt");

			Files.createFile(hiddenFilePath);

			Files.setAttribute(hiddenFilePath, "dos:hidden", true);

			Path shortcutFilePath = Paths.get(
				_syncSite.getFilePathName() + "/test.txt - Shortcut.lnk");

			Files.createFile(shortcutFilePath);
		}
		else {
			Path ignoredFilePath = Paths.get(
				_syncSite.getFilePathName() + "/.DS_Store");

			Files.createFile(ignoredFilePath);

			Path symbolicLinkFilePath = Paths.get(
				_syncSite.getFilePathName() + "/symbolic_link");

			Files.createSymbolicLink(symbolicLinkFilePath, ignoredFilePath);
		}

		sleep();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(2, _syncFiles.size());
	}

	@Test
	public void testRunDeleteFile() throws Exception {
		setPostResponse("dependencies/watcher_test_delete_file.json");

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(filePath);

		sleep();

		Files.delete(filePath);

		sleep();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(2, _syncFiles.size());
		Assert.assertNull(
			SyncFileService.fetchSyncFile(
				FilePathNameUtil.getFilePathName(filePath),
				syncAccount.getSyncAccountId()));
	}

	@Test
	public void testRunModifyFile() throws Exception {
		setPostResponse("dependencies/watcher_test_modify_file.json");

		Path filePath = Paths.get(_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(filePath);

		sleep();

		BufferedWriter bufferedWriter = Files.newBufferedWriter(
			filePath, StandardCharsets.UTF_8);

		bufferedWriter.write("Hello World");

		bufferedWriter.close();

		sleep();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());

		SyncFile syncFile = SyncFileService.fetchSyncFile(
			FilePathNameUtil.getFilePathName(filePath),
			syncAccount.getSyncAccountId());

		Assert.assertEquals(
			FileUtil.getChecksum(filePath), syncFile.getChecksum());
		Assert.assertEquals(Files.size(filePath), syncFile.getSize());
	}

	@Test
	public void testRunMoveFile() throws Exception {
		setPostResponse("dependencies/watcher_test_move_file.json");

		Path sourceFilePath = Paths.get(
			_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(sourceFilePath);

		Path targetFilePath = Paths.get(_syncSite.getFilePathName() + "/test");

		Files.createDirectory(targetFilePath);

		sleep();

		Files.move(
			sourceFilePath,
			targetFilePath.resolve(sourceFilePath.getFileName()));

		sleep();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(4, _syncFiles.size());
		Assert.assertNotNull(
			SyncFileService.fetchSyncFile(
				FilePathNameUtil.getFilePathName(targetFilePath),
				syncAccount.getSyncAccountId()));
	}

	@Test
	public void testRunRenameFile() throws Exception {
		setPostResponse("dependencies/watcher_test_rename_file.json");

		Path sourceFilePath = Paths.get(
			_syncSite.getFilePathName() + "/test.txt");

		Files.createFile(sourceFilePath);

		sleep();

		Path targetFilePath = Paths.get(
			_syncSite.getFilePathName() + "/test2.txt");

		Files.move(sourceFilePath, targetFilePath);

		sleep();

		_syncFiles = SyncFileService.findSyncFiles(
			syncAccount.getSyncAccountId());

		Assert.assertEquals(3, _syncFiles.size());
		Assert.assertNotNull(
			SyncFileService.fetchSyncFile(
				FilePathNameUtil.getFilePathName(targetFilePath),
				syncAccount.getSyncAccountId()));
	}

	protected void sleep() throws InterruptedException {
		if (OSDetector.isApple()) {
			Thread.sleep(3000);
		}
		else {
			Thread.sleep(1000);
		}
	}

	private List<SyncFile> _syncFiles;
	private SyncSite _syncSite;
	private Watcher _watcher;

}