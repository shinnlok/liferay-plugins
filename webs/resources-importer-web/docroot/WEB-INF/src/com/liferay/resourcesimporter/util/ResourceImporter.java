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

package com.liferay.resourcesimporter.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Raymond Augé
 * @author Ryan Park
 */
public class ResourceImporter extends FileSystemImporter {

	@Override
	public void importResources() throws Exception {
		doImportResources();
	}

	@Override
	protected void addDDMStructures(
			String parentStructureId, String structuresDirName)
		throws Exception {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(structuresDirName));

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			if (resourcePath.endsWith(StringPool.SLASH)) {
				continue;
			}

			String name = FileUtil.getShortFileName(resourcePath);

			URL url = servletContext.getResource(resourcePath);

			URLConnection urlConnection = url.openConnection();

			doAddDDMStructures(
				parentStructureId, name, urlConnection.getInputStream());
		}
	}

	@Override
	protected void addDDMTemplates(
			String ddmStructureKey, String templatesDirName)
		throws Exception {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(templatesDirName));

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			if (resourcePath.endsWith(StringPool.SLASH)) {
				continue;
			}

			String name = FileUtil.getShortFileName(resourcePath);

			URL url = servletContext.getResource(resourcePath);

			URLConnection urlConnection = url.openConnection();

			doAddDDMTemplates(
				ddmStructureKey, name, urlConnection.getInputStream());
		}
	}

	@Override
	protected void addDLFileEntries(String fileEntriesDirName)
		throws Exception {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(fileEntriesDirName));

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			if (resourcePath.endsWith(StringPool.SLASH)) {
				long dlFolderId = addDLFolder(
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, resourcePath);

				recurseDLDirectory(dlFolderId, resourcePath);
			}
			else {
				doAddDLFileEntry(resourcePath);
			}
		}
	}

	@Override
	protected long addDLFolder(long parentFolderId, String resourcePath)
		throws PortalException, SystemException {

		String folderPath = FileUtil.getPath(resourcePath);

		String folderName = folderPath.substring(
			folderPath.lastIndexOf(StringPool.SLASH) + 1);

		long folderId = super.addDLFolder(parentFolderId, folderName);

		_folderIds.put(resourcePath, folderId);

		return folderId;
	}

	@Override
	protected void addJournalArticles(
			String ddmStructureKey, String ddmTemplateKey,
			String articlesDirName)
		throws Exception {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(articlesDirName));

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			if (resourcePath.endsWith(StringPool.SLASH)) {
				continue;
			}

			String name = FileUtil.getShortFileName(resourcePath);

			URL url = servletContext.getResource(resourcePath);

			URLConnection urlConnection = url.openConnection();

			doAddJournalArticles(
				ddmStructureKey, ddmTemplateKey, name,
				urlConnection.getInputStream());
		}
	}

	protected void doAddDLFileEntry(String resourcePath) throws Exception {
		String folderPath = FileUtil.getPath(
			resourcePath).concat(StringPool.SLASH);

		long parentFolderId = _folderIds.get(folderPath);

		String name = FileUtil.getShortFileName(resourcePath);

		URL url = servletContext.getResource(resourcePath);

		URLConnection urlConnection = url.openConnection();

		doAddDLFileEntry(
			parentFolderId, name, urlConnection.getInputStream(),
			urlConnection.getContentLength());
	}

	@Override
	protected InputStream getInputStream(String fileName) throws Exception {
		URL url = servletContext.getResource(resourcesDir.concat(fileName));

		if (url == null) {
			return null;
		}

		URLConnection urlConnection = url.openConnection();

		return urlConnection.getInputStream();
	}

	private void recurseDLDirectory(
			long parentFolderId, String resourcePathRoot)
		throws Exception {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcePathRoot);

		if ((resourcePaths == null) || resourcePaths.isEmpty()) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			if (resourcePath.endsWith(StringPool.SLASH)) {
				long dlFolderId = addDLFolder(parentFolderId, resourcePath);

				recurseDLDirectory(dlFolderId, resourcePath);
			}
			else {
				doAddDLFileEntry(resourcePath);
			}
		}
	}

	private Map<String, Long> _folderIds = new HashMap<String, Long>();

}