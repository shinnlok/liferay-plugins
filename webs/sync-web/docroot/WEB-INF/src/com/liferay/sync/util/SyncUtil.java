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

package com.liferay.sync.util;

import com.liferay.io.delta.ByteChannelReader;
import com.liferay.io.delta.ByteChannelWriter;
import com.liferay.io.delta.DeltaUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Lock;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntryConstants;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileVersionLocalServiceUtil;
import com.liferay.sync.model.SyncConstants;
import com.liferay.sync.model.SyncDLObject;
import com.liferay.sync.model.impl.SyncDLObjectImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import java.util.Date;

/**
 * @author Dennis Ju
 */
public class SyncUtil {

	public static String getChecksum(DLFileVersion dlFileVersion)
		throws PortalException {

		return getChecksum(dlFileVersion.getContentStream(false));
	}

	public static String getChecksum(File file) throws PortalException {
		FileInputStream fileInputStream = null;

		try {
			fileInputStream = new FileInputStream(file);

			return getChecksum(fileInputStream);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			StreamUtil.cleanUp(fileInputStream);
		}
	}

	public static String getChecksum(InputStream inputStream) {
		return DigesterUtil.digestBase64(Digester.MD5, inputStream);
	}

	public static File getFileDelta(File sourceFile, File targetFile)
		throws PortalException {

		File deltaFile = null;

		FileInputStream sourceFileInputStream = null;
		FileChannel sourceFileChannel = null;
		File checksumsFile = FileUtil.createTempFile();
		OutputStream checksumsOutputStream = null;
		WritableByteChannel checksumsWritableByteChannel = null;

		try {
			sourceFileInputStream = new FileInputStream(sourceFile);

			sourceFileChannel = sourceFileInputStream.getChannel();

			checksumsOutputStream = new FileOutputStream(checksumsFile);

			checksumsWritableByteChannel = Channels.newChannel(
				checksumsOutputStream);

			ByteChannelWriter checksumsByteChannelWriter =
				new ByteChannelWriter(checksumsWritableByteChannel);

			DeltaUtil.checksums(sourceFileChannel, checksumsByteChannelWriter);

			checksumsByteChannelWriter.finish();
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			StreamUtil.cleanUp(sourceFileInputStream);
			StreamUtil.cleanUp(sourceFileChannel);
			StreamUtil.cleanUp(checksumsOutputStream);
			StreamUtil.cleanUp(checksumsWritableByteChannel);
		}

		FileInputStream targetFileInputStream = null;
		ReadableByteChannel targetReadableByteChannel = null;
		InputStream checksumsInputStream = null;
		ReadableByteChannel checksumsReadableByteChannel = null;
		OutputStream deltaOutputStream = null;
		WritableByteChannel deltaOutputStreamWritableByteChannel = null;

		try {
			targetFileInputStream = new FileInputStream(targetFile);

			targetReadableByteChannel = targetFileInputStream.getChannel();

			checksumsInputStream = new FileInputStream(checksumsFile);

			checksumsReadableByteChannel = Channels.newChannel(
				checksumsInputStream);

			ByteChannelReader checksumsByteChannelReader =
				new ByteChannelReader(checksumsReadableByteChannel);

			deltaFile = FileUtil.createTempFile();

			deltaOutputStream = new FileOutputStream(deltaFile);

			deltaOutputStreamWritableByteChannel = Channels.newChannel(
				deltaOutputStream);

			ByteChannelWriter deltaByteChannelWriter = new ByteChannelWriter(
				deltaOutputStreamWritableByteChannel);

			DeltaUtil.delta(
				targetReadableByteChannel, checksumsByteChannelReader,
				deltaByteChannelWriter);

			deltaByteChannelWriter.finish();
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			StreamUtil.cleanUp(targetFileInputStream);
			StreamUtil.cleanUp(targetReadableByteChannel);
			StreamUtil.cleanUp(checksumsInputStream);
			StreamUtil.cleanUp(checksumsReadableByteChannel);
			StreamUtil.cleanUp(deltaOutputStream);
			StreamUtil.cleanUp(deltaOutputStreamWritableByteChannel);

			FileUtil.delete(checksumsFile);
		}

		return deltaFile;
	}

	public static boolean isSupportedFolder(DLFolder dlFolder) {
		if (dlFolder.isHidden() || dlFolder.isMountPoint()) {
			return false;
		}

		return true;
	}

	public static boolean isSupportedFolder(Folder folder) {
		if (!(folder.getModel() instanceof DLFolder)) {
			return false;
		}

		DLFolder dlFolder = (DLFolder)folder.getModel();

		return isSupportedFolder(dlFolder);
	}

	public static void patchFile(
			File originalFile, File deltaFile, File patchedFile)
		throws PortalException {

		FileInputStream originalFileInputStream = null;
		FileChannel originalFileChannel = null;
		FileOutputStream patchedFileOutputStream = null;
		WritableByteChannel patchedWritableByteChannel = null;
		ReadableByteChannel deltaReadableByteChannel = null;

		try {
			originalFileInputStream = new FileInputStream(originalFile);

			originalFileChannel = originalFileInputStream.getChannel();

			patchedFileOutputStream = new FileOutputStream(patchedFile);

			patchedWritableByteChannel = Channels.newChannel(
				patchedFileOutputStream);

			FileInputStream deltaInputStream = new FileInputStream(deltaFile);

			deltaReadableByteChannel = Channels.newChannel(deltaInputStream);

			ByteChannelReader deltaByteChannelReader = new ByteChannelReader(
				deltaReadableByteChannel);

			DeltaUtil.patch(
				originalFileChannel, patchedWritableByteChannel,
				deltaByteChannelReader);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			StreamUtil.cleanUp(originalFileInputStream);
			StreamUtil.cleanUp(originalFileChannel);
			StreamUtil.cleanUp(patchedFileOutputStream);
			StreamUtil.cleanUp(patchedWritableByteChannel);
			StreamUtil.cleanUp(deltaReadableByteChannel);
		}
	}

	public static SyncDLObject toSyncDLObject(
			DLFileEntry dlFileEntry, String event)
		throws PortalException {

		return toSyncDLObject(dlFileEntry, event, false);
	}

	public static SyncDLObject toSyncDLObject(
			DLFileEntry dlFileEntry, String event, boolean excludeWorkingCopy)
		throws PortalException {

		DLFileVersion dlFileVersion = null;

		Date lockExpirationDate = null;
		long lockUserId = 0;
		String lockUserName = StringPool.BLANK;
		String type = null;

		Lock lock = dlFileEntry.getLock();

		if ((lock == null) || excludeWorkingCopy) {
			dlFileVersion = DLFileVersionLocalServiceUtil.getFileVersion(
				dlFileEntry.getFileEntryId(), dlFileEntry.getVersion());

			type = SyncConstants.TYPE_FILE;
		}
		else {
			dlFileVersion = DLFileVersionLocalServiceUtil.getFileVersion(
				dlFileEntry.getFileEntryId(),
				DLFileEntryConstants.PRIVATE_WORKING_COPY_VERSION);

			lockExpirationDate = lock.getExpirationDate();
			lockUserId = lock.getUserId();
			lockUserName = lock.getUserName();
			type = SyncConstants.TYPE_PRIVATE_WORKING_COPY;
		}

		SyncDLObject syncDLObject = new SyncDLObjectImpl();

		syncDLObject.setCompanyId(dlFileVersion.getCompanyId());
		syncDLObject.setCreateDate(dlFileVersion.getCreateDate());
		syncDLObject.setModifiedDate(dlFileVersion.getModifiedDate());
		syncDLObject.setRepositoryId(dlFileVersion.getRepositoryId());
		syncDLObject.setParentFolderId(dlFileVersion.getFolderId());
		syncDLObject.setName(dlFileVersion.getTitle());
		syncDLObject.setExtension(dlFileVersion.getExtension());
		syncDLObject.setMimeType(dlFileVersion.getMimeType());
		syncDLObject.setDescription(dlFileVersion.getDescription());
		syncDLObject.setChangeLog(dlFileVersion.getChangeLog());
		syncDLObject.setExtraSettings(dlFileVersion.getExtraSettings());
		syncDLObject.setVersion(dlFileVersion.getVersion());
		syncDLObject.setSize(dlFileVersion.getSize());
		syncDLObject.setChecksum(getChecksum(dlFileVersion));
		syncDLObject.setEvent(event);
		syncDLObject.setLockExpirationDate(lockExpirationDate);
		syncDLObject.setLockUserId(lockUserId);
		syncDLObject.setLockUserName(lockUserName);
		syncDLObject.setType(type);
		syncDLObject.setTypePK(dlFileEntry.getFileEntryId());
		syncDLObject.setTypeUuid(dlFileEntry.getUuid());

		return syncDLObject;
	}

	public static SyncDLObject toSyncDLObject(DLFolder dlFolder, String event) {
		SyncDLObject syncDLObject = new SyncDLObjectImpl();

		syncDLObject.setCompanyId(dlFolder.getCompanyId());
		syncDLObject.setCreateDate(dlFolder.getCreateDate());
		syncDLObject.setModifiedDate(dlFolder.getModifiedDate());
		syncDLObject.setRepositoryId(dlFolder.getRepositoryId());
		syncDLObject.setParentFolderId(dlFolder.getParentFolderId());
		syncDLObject.setName(dlFolder.getName());
		syncDLObject.setExtension(StringPool.BLANK);
		syncDLObject.setMimeType(StringPool.BLANK);
		syncDLObject.setDescription(dlFolder.getDescription());
		syncDLObject.setChangeLog(StringPool.BLANK);
		syncDLObject.setExtraSettings(StringPool.BLANK);
		syncDLObject.setVersion(StringPool.BLANK);
		syncDLObject.setSize(0);
		syncDLObject.setChecksum(StringPool.BLANK);
		syncDLObject.setEvent(event);
		syncDLObject.setLockExpirationDate(null);
		syncDLObject.setLockUserId(0);
		syncDLObject.setLockUserName(StringPool.BLANK);
		syncDLObject.setType(SyncConstants.TYPE_FOLDER);
		syncDLObject.setTypePK(dlFolder.getFolderId());
		syncDLObject.setTypeUuid(dlFolder.getUuid());

		return syncDLObject;
	}

	public static SyncDLObject toSyncDLObject(FileEntry fileEntry, String event)
		throws PortalException {

		if (fileEntry.getModel() instanceof DLFileEntry) {
			DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();

			return toSyncDLObject(dlFileEntry, event);
		}

		throw new PortalException(
			"FileEntry must be an instance of DLFileEntry");
	}

	public static SyncDLObject toSyncDLObject(Folder folder, String event)
		throws PortalException {

		if (folder.getModel() instanceof DLFolder) {
			DLFolder dlFolder = (DLFolder)folder.getModel();

			return toSyncDLObject(dlFolder, event);
		}

		throw new PortalException("Folder must be an instance of DLFolder");
	}

}