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
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.util.Locale;
import java.util.Set;

/**
 * @author Eric Chin
 */
public class TemplateImporter extends FileSystemImporter {

	@Override
	public void importResources() throws Exception {
		doImportResources();
	}

	@Override
	protected void addApplicationDisplayTemplate(
			String parentDirName, String dirName, long classNameId)
		throws IOException, PortalException, SystemException {

		StringBundler sb = new StringBundler(4);

		sb.append(resourcesDir);
		sb.append(parentDirName);
		sb.append("/");
		sb.append(dirName);

		Set<String> resourcePaths = servletContext.getResourcePaths(
			sb.toString());

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			File file = new File(resourcePath);

			String script = StringUtil.read(getInputStream(resourcePath));

			if (Validator.isNull(script)) {
				continue;
			}

			addApplicationDisplayTemplate(script, file, classNameId);
		}
	}

	@Override
	protected void addDDLDisplayTemplates(
			String ddmStructureKey, String displayTemplateDir)
		throws Exception {

		DDMStructure ddmStructure = DDMStructureLocalServiceUtil.getStructure(
			groupId, PortalUtil.getClassNameId(DDLRecordSet.class),
			ddmStructureKey);

		long ddmStructureId = ddmStructure.getStructureId();

		StringBundler sb = new StringBundler(6);

		sb.append(resourcesDir);
		sb.append(displayTemplateDir);
		sb.append("/");
		sb.append(ddmStructure.getName(Locale.getDefault()));

		Set<String> resourcePaths = servletContext.getResourcePaths(
			sb.toString());

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			String extension = FileUtil.getExtension(resourcePath);

			String language = null;

			if (extension.equals(TemplateConstants.LANG_TYPE_VM)) {
				language = TemplateConstants.LANG_TYPE_VM;
			}
			else if (extension.equals(TemplateConstants.LANG_TYPE_FTL)) {
				language = TemplateConstants.LANG_TYPE_FTL;
			}
			else {
				continue;
			}

			String script = StringUtil.read(getInputStream(resourcePath));

			if (Validator.isNull(script)) {
				return;
			}

			addDDMTemplate(
				groupId, ddmStructureId, FileUtil.stripExtension(resourcePath),
				language, script, DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY,
				StringPool.BLANK);
		}
	}

	@Override
	protected void addDDLFormTemplates(
			String ddmStructureKey, String formTemplateDir)
		throws Exception {

		DDMStructure ddmStructure = DDMStructureLocalServiceUtil.getStructure(
			groupId, PortalUtil.getClassNameId(DDLRecordSet.class),
			ddmStructureKey);

		long ddmStructureId = ddmStructure.getStructureId();

		StringBundler sb = new StringBundler(6);

		sb.append(resourcesDir);
		sb.append(formTemplateDir);
		sb.append("/");
		sb.append(ddmStructure.getName(Locale.getDefault()));

		Set<String> resourcePaths = servletContext.getResourcePaths(
			sb.toString());

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			String script = StringUtil.read(getInputStream(resourcePath));

			if (Validator.isNull(script)) {
				return;
			}

			addDDMTemplate(
				groupId, ddmStructureId, resourcePath, "xsd", script,
				DDMTemplateConstants.TEMPLATE_TYPE_FORM,
				DDMTemplateConstants.TEMPLATE_MODE_CREATE);
		}
	}

	@Override
	protected void addDDLStructures(String dirName) throws Exception {
		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(dirName));

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			File file = new File(resourcePath);

			String fileName = FileUtil.stripExtension(file.getName());

			addDDMStructures(fileName, getInputStream(resourcePath));
		}
	}

	@Override
	protected void addPageTemplate(String dirName) throws Exception {
		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(dirName));

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			String extension = FileUtil.getExtension(resourcePath);

			if (!extension.equals("json")) {
				return;
			}

			addPageTemplate(getInputStream(resourcePath));
		}
	}

	@Override
	protected InputStream getInputStream(String resourcePath)
		throws IOException {

		URL url = servletContext.getResource(resourcePath);

		if (url == null) {
			return null;
		}

		URLConnection urlConnection = url.openConnection();

		return urlConnection.getInputStream();
	}

}