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

package com.liferay.sync.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.sync.model.SyncDLFileVersionDiff;
import com.liferay.sync.service.base.SyncDLFileVersionDiffLocalServiceBaseImpl;
import com.liferay.sync.util.PortletPropsValues;

import java.io.File;

import java.util.Date;
import java.util.List;

/**
 * @author Dennis Ju
 */
public class SyncDLFileVersionDiffLocalServiceImpl
	extends SyncDLFileVersionDiffLocalServiceBaseImpl {

	@Override
	public SyncDLFileVersionDiff addSyncDLFileVersionDiff(
			long fileEntryId, long sourceFileVersionId,
			long destinationFileVersionId, File file)
		throws PortalException, SystemException {

		long syncDLFileVersionDiffId = counterLocalService.increment();

		SyncDLFileVersionDiff syncDLFileVersionDiff =
			syncDLFileVersionDiffPersistence.create(syncDLFileVersionDiffId);

		syncDLFileVersionDiff.setFileEntryId(fileEntryId);
		syncDLFileVersionDiff.setSourceFileVersionId(sourceFileVersionId);
		syncDLFileVersionDiff.setDestinationFileVersionId(
			destinationFileVersionId);

		FileEntry fileEntry = dlAppService.getFileEntry(fileEntryId);

		String dataFileName = getDataFileName(
			fileEntryId, sourceFileVersionId, destinationFileVersionId);

		FileEntry dataFileEntry = PortletFileRepositoryUtil.addPortletFileEntry(
			fileEntry.getGroupId(), fileEntry.getUserId(),
			SyncDLFileVersionDiff.class.getName(),
			syncDLFileVersionDiff.getSyncDLFileVersionDiffId(),
			PortletKeys.DOCUMENT_LIBRARY,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, file, dataFileName,
			fileEntry.getMimeType(), false);

		syncDLFileVersionDiff.setDataFileEntryId(
			dataFileEntry.getFileEntryId());

		syncDLFileVersionDiff.setSize(file.length());

		Date expirationDate = new Date();

		expirationDate.setTime(
			expirationDate.getTime() +
				PortletPropsValues.SYNC_FILE_DIFF_CACHE_EXPIRATION_TIME *
					3600000);

		syncDLFileVersionDiff.setExpirationDate(expirationDate);

		syncDLFileVersionDiffPersistence.update(syncDLFileVersionDiff);

		return syncDLFileVersionDiff;
	}

	@Override
	public void deleteExpiredSyncDLFileVersionDiffs()
		throws PortalException, SystemException {

		List<SyncDLFileVersionDiff> syncDLFileVersionDiffs =
			syncDLFileVersionDiffPersistence.findByExpirationDate(new Date());

		for (SyncDLFileVersionDiff syncDLFileVersionDiff :
				syncDLFileVersionDiffs) {

			PortletFileRepositoryUtil.deletePortletFileEntry(
				syncDLFileVersionDiff.getDataFileEntryId());

			syncDLFileVersionDiffPersistence.remove(
				syncDLFileVersionDiff.getSyncDLFileVersionDiffId());
		}
	}

	@Override
	public SyncDLFileVersionDiff fetchSyncDLFileVersionDiff(
			long fileEntryId, long sourceFileVersionId,
			long destinationFileVersionId)
		throws SystemException {

		return syncDLFileVersionDiffPersistence.fetchByS_S_D(
			fileEntryId, sourceFileVersionId, destinationFileVersionId);
	}

	@Override
	public void refreshExpirationDate(long syncDLFileVersionDiffId)
		throws PortalException, SystemException {

		SyncDLFileVersionDiff syncDLFileVersionDiff =
			syncDLFileVersionDiffPersistence.findByPrimaryKey(
				syncDLFileVersionDiffId);

		Date expirationDate = new Date();

		expirationDate.setTime(
			expirationDate.getTime() +
				PortletPropsValues.SYNC_FILE_DIFF_CACHE_EXPIRATION_TIME *
					3600000);

		syncDLFileVersionDiff.setExpirationDate(expirationDate);

		updateSyncDLFileVersionDiff(syncDLFileVersionDiff);
	}

	protected String getDataFileName(
		long fileEntryId, long sourceFileVersionId,
		long destinationFileVersionId) {

		StringBundler sb = new StringBundler(5);

		sb.append(fileEntryId);
		sb.append(StringPool.UNDERLINE);
		sb.append(sourceFileVersionId);
		sb.append(StringPool.UNDERLINE);
		sb.append(destinationFileVersionId);

		return sb.toString();
	}

}