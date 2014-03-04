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

package com.liferay.sync.engine;

import com.liferay.sync.engine.documentlibrary.event.GetSyncDLObjectUpdateEvent;
import com.liferay.sync.engine.filesystem.SyncSiteWatchEventListener;
import com.liferay.sync.engine.filesystem.SyncWatchEventProcessor;
import com.liferay.sync.engine.filesystem.WatchEventListener;
import com.liferay.sync.engine.filesystem.Watcher;
import com.liferay.sync.engine.model.SyncAccount;
import com.liferay.sync.engine.model.SyncAccountModelListener;
import com.liferay.sync.engine.model.SyncFileModelListener;
import com.liferay.sync.engine.model.SyncSite;
import com.liferay.sync.engine.model.SyncSiteModelListener;
import com.liferay.sync.engine.service.SyncAccountService;
import com.liferay.sync.engine.service.SyncFileService;
import com.liferay.sync.engine.service.SyncSiteService;
import com.liferay.sync.engine.upgrade.util.UpgradeUtil;
import com.liferay.sync.engine.util.LoggerUtil;
import com.liferay.sync.engine.util.PropsValues;
import com.liferay.sync.engine.util.SyncEngineUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shinn Lok
 */
public class SyncEngine {

	public static void cancelSyncAccountTasks(long syncAccountId)
		throws Exception {

		Object[] syncAccountTasks = _syncAccountTasks.get(syncAccountId);

		ScheduledFuture<?> scheduledFuture =
			(ScheduledFuture<?>)syncAccountTasks[0];

		scheduledFuture.cancel(false);

		Watcher watcher = (Watcher)syncAccountTasks[1];

		watcher.close();
	}

	public static void scheduleSyncAccountTasks(long syncAccountId)
		throws Exception {

		final SyncAccount syncAccount = SyncAccountService.fetchSyncAccount(
			syncAccountId);

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				Set<Long> syncSiteIds = SyncSiteService.getActiveSyncSiteIds(
					syncAccount.getSyncAccountId());

				for (long syncSiteId : syncSiteIds) {
					SyncSite syncSite = SyncSiteService.fetchSyncSite(
						syncSiteId);

					Map<String, Object> parameters =
						new HashMap<String, Object>();

					parameters.put("companyId", syncSite.getCompanyId());
					parameters.put("repositoryId", syncSite.getGroupId());
					parameters.put("syncSite", syncSite);

					GetSyncDLObjectUpdateEvent getSyncDLObjectUpdateEvent =
						new GetSyncDLObjectUpdateEvent(
							syncAccount.getSyncAccountId(), parameters);

					getSyncDLObjectUpdateEvent.run();
				}
			}

		};

		ScheduledFuture<?> scheduledFuture =
			_eventScheduledExecutorService.scheduleAtFixedRate(
				runnable, 0, syncAccount.getInterval(), TimeUnit.SECONDS);

		Path filePath = Paths.get(syncAccount.getFilePathName());

		WatchEventListener watchEventListener = new SyncSiteWatchEventListener(
			syncAccount.getSyncAccountId());

		Watcher watcher = new Watcher(filePath, true, watchEventListener);

		_watcherExecutorService.execute(watcher);

		_syncAccountTasks.put(
			syncAccountId, new Object[] {scheduledFuture, watcher});
	}

	public static void start() {
		try {
			doStart();
		}
		catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}

	protected static void doStart() throws Exception {
		SyncEngineUtil.fireSyncEngineStateChanged(
			SyncEngineUtil.SYNC_ENGINE_STATE_STARTING);

		LoggerUtil.initLogger();

		_logger.info("Starting " + PropsValues.SYNC_PRODUCT_NAME);

		UpgradeUtil.upgrade();

		SyncAccountService.registerModelListener(
			new SyncAccountModelListener());
		SyncFileService.registerModelListener(new SyncFileModelListener());
		SyncSiteService.registerModelListener(new SyncSiteModelListener());

		SyncWatchEventProcessor syncWatchEventProcessor =
			new SyncWatchEventProcessor();

		_syncWatchEventProcessorExecutorService.scheduleAtFixedRate(
			syncWatchEventProcessor, 0, 3, TimeUnit.SECONDS);

		for (long syncAccountId :
				SyncAccountService.getActiveSyncAccountIds()) {

			scheduleSyncAccountTasks(syncAccountId);
		}

		SyncEngineUtil.fireSyncEngineStateChanged(
			SyncEngineUtil.SYNC_ENGINE_STATE_STARTED);
	}

	private static Logger _logger = LoggerFactory.getLogger(SyncEngine.class);

	private static ScheduledExecutorService _eventScheduledExecutorService =
		Executors.newScheduledThreadPool(5);
	private static Map<Long, Object[]> _syncAccountTasks =
		new HashMap<Long, Object[]>();
	private static ScheduledExecutorService
		_syncWatchEventProcessorExecutorService =
			Executors.newSingleThreadScheduledExecutor();
	private static ExecutorService _watcherExecutorService =
		Executors.newCachedThreadPool();

}