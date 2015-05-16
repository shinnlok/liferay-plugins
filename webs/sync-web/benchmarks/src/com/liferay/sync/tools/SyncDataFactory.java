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

package com.liferay.sync.tools;

import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.tools.sample.sql.builder.DataFactory;
import com.liferay.portal.util.DigesterImpl;
import com.liferay.portlet.documentlibrary.model.DLFileEntryModel;
import com.liferay.portlet.documentlibrary.model.DLFolderModel;
import com.liferay.portlet.documentlibrary.model.DLSyncConstants;
import com.liferay.sync.model.SyncDLObjectModel;
import com.liferay.sync.model.impl.SyncDLObjectModelImpl;
import com.liferay.util.SimpleCounter;

import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Tina Tian
 */
public class SyncDataFactory extends DataFactory {

	public SyncDataFactory(Properties properties) throws Exception {
		super(properties);
	}

	public List<SyncDLObjectModel> getSyncDLObjectModels() {
		return _syncDLObjectModels;
	}

	@Override
	public DLFileEntryModel newDlFileEntryModel(
		DLFolderModel dlFolerModel, int index) {

		DLFileEntryModel dlFileEntryModel = super.newDlFileEntryModel(
			dlFolerModel, index);

		long size = dlFileEntryModel.getSize();

		if (size < 8) {
			size = 8;
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate((int)size);

		byteBuffer.putLong(dlFileEntryModel.getFileEntryId());
		byteBuffer.put(new byte[(int)size - 8]);

		byteBuffer.flip();

		String checksum = _DIGESTER.digestBase64(byteBuffer);

		SyncDLObjectModel syncDLObjectModel = newSyncDLObjectModel(
			dlFileEntryModel.getCreateDate(),
			dlFileEntryModel.getCreateDate(),
			dlFileEntryModel.getRepositoryId(), dlFileEntryModel.getFolderId(),
			dlFileEntryModel.getTitle(), checksum, size,
			DLSyncConstants.TYPE_FILE, dlFileEntryModel.getFileEntryId(),
			dlFileEntryModel.getUuid(), dlFileEntryModel.getVersion());

		_syncDLObjectModels.add(syncDLObjectModel);

		return dlFileEntryModel;
	}

	@Override
	public DLFolderModel newDLFolderModel(
		long groupId, long parentFolderId, int index) {

		DLFolderModel dlFolderModel = super.newDLFolderModel(
			groupId, parentFolderId, index);

		SyncDLObjectModel syncDLObjectModel = newSyncDLObjectModel(
			dlFolderModel.getCreateDate(), dlFolderModel.getCreateDate(),
			dlFolderModel.getRepositoryId(), dlFolderModel.getParentFolderId(),
			dlFolderModel.getName(), StringPool.BLANK, 0,
			DLSyncConstants.TYPE_FOLDER, dlFolderModel.getFolderId(),
			dlFolderModel.getUuid(), "-1");

		_syncDLObjectModels.add(syncDLObjectModel);

		return dlFolderModel;
	}

	private SyncDLObjectModel newSyncDLObjectModel(
		Date createDate, Date modifiedDate, long repositoryId,
		long parentFolderId, String name, String checksum, long size,
		String type, long typePK, String typeUuid, String version) {

		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		PortletClassLoaderUtil.setServletContextName(
			ClassLoaderPool.getContextName(classLoader));

		SyncDLObjectModel syncDLObjectModel = new SyncDLObjectModelImpl();

		SimpleCounter counter = getCounter();
		CompanyModel company = getCompanyModel();

		syncDLObjectModel.setSyncDLObjectId(counter.get());
		syncDLObjectModel.setCompanyId(company.getCompanyId());
		syncDLObjectModel.setCreateTime(createDate.getTime());
		syncDLObjectModel.setModifiedTime(modifiedDate.getTime());
		syncDLObjectModel.setRepositoryId(repositoryId);
		syncDLObjectModel.setParentFolderId(parentFolderId);
		syncDLObjectModel.setName(name);
		syncDLObjectModel.setVersion(version);
		syncDLObjectModel.setSize(size);
		syncDLObjectModel.setChecksum(checksum);
		syncDLObjectModel.setEvent(DLSyncConstants.EVENT_ADD);
		syncDLObjectModel.setType(type);
		syncDLObjectModel.setTypePK(typePK);
		syncDLObjectModel.setTypeUuid(typeUuid);

		return syncDLObjectModel;
	}

	private static final Digester _DIGESTER = new DigesterImpl();

	private List<SyncDLObjectModel> _syncDLObjectModels =
		new ArrayList<SyncDLObjectModel>();

}