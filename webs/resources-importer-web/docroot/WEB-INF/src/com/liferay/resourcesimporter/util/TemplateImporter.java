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
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.wiki.model.WikiPage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Eric Chin
 */
public class TemplateImporter extends FileSystemImporter {

	@Override
	public void importResources() throws Exception {
		doImportResources();
	}

	protected void addApplicationDisplayTemplate(
			String parentDirName, String dirName)
		throws IOException, PortalException, SystemException {

		StringBundler sb = new StringBundler(resourcesDir);

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

			String fileName = FileUtil.stripExtension(file.getName());

			String extension = FileUtil.getExtension(resourcePath);

			long classNameId = getClassNameId(dirName);

			Map<Locale, String> nameMap = new HashMap<Locale, String>();

			nameMap.put(Locale.getDefault(), fileName);

			Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

			descriptionMap.put(Locale.getDefault(), fileName);

			String language = null;

			if (extension.equals(TemplateConstants.LANG_TYPE_VM)) {
				language = TemplateConstants.LANG_TYPE_VM;
			}
			else if (extension.equals(TemplateConstants.LANG_TYPE_FTL)) {
				language = TemplateConstants.LANG_TYPE_FTL;
			}
			else {
				if (_log.isErrorEnabled()) {
					_log.error(
						"Script file type is not of type vm or ftl " +
							"for resource path " + resourcePath);
				}

				continue;
			}

			String script = getFileContent(resourcePath);

			if (Validator.isNull(script)) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Skipping " + resourcePath +
							" because the file is empty.");
				}

				continue;
			}

			boolean cacheable = false;

			boolean smallImage = false;

			String smallImageURL = "";

			DDMTemplateLocalServiceUtil.addTemplate(
				userId, groupId, classNameId, 0, null, nameMap, descriptionMap,
				DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, "", language,
				script, cacheable, smallImage, smallImageURL, null,
				serviceContext);
		}
	}

	protected void addApplicationDisplayTemplates(String parentDirName)
		throws IOException, PortalException, SystemException {

		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_ASSET_CATEGORY);
		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_ASSET_ENTRY);
		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_ASSET_TAG);
		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_BLOGS_ENTRY);
		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_DOCUMENT_LIBRARY);
		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_SITE_MAP);
		addApplicationDisplayTemplate(
			parentDirName, _APPLICATION_DISPLAY_WIKI_PAGE);
	}

	protected void addDDLDisplayTemplates(DDMStructure ddmStructure)
		throws IOException, PortalException, SystemException {

		long ddmStructureId = ddmStructure.getStructureId();

		StringBundler sb = new StringBundler(resourcesDir);

		sb.append(_DDL_TEMPLATE_DIR_NAME);
		sb.append("/");
		sb.append(_DDL_STRUCTURE_DISPLAY_TEMPLATE);
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
				if (_log.isErrorEnabled()) {
					_log.error(
						"Script file type is not of type vm or ftl " +
							"for resource path " + resourcePath);
				}

				continue;
			}

			addDDMTemplate(
				groupId, ddmStructureId, resourcePath, language,
				DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, "");
		}
	}

	protected void addDDLFormTemplates(DDMStructure ddmStructure)
		throws IOException, PortalException, SystemException {

		long ddmStructureId = ddmStructure.getStructureId();

		StringBundler sb = new StringBundler(resourcesDir);

		sb.append(_DDL_TEMPLATE_DIR_NAME);
		sb.append("/");
		sb.append(_DDL_STRUCTURE_FORM_TEMPLATE);
		sb.append("/");
		sb.append(ddmStructure.getName(Locale.getDefault()));

		Set<String> resourcePaths = servletContext.getResourcePaths(
			sb.toString());

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			addDDMTemplate(
				groupId, ddmStructureId, resourcePath, "xsd",
				DDMTemplateConstants.TEMPLATE_TYPE_FORM,
				DDMTemplateConstants.TEMPLATE_MODE_CREATE);
		}
	}

	protected void addDDLStructure(String dirName)
		throws IOException, PortalException, SystemException {

		Set<String> resourcePaths = servletContext.getResourcePaths(dirName);

		if (resourcePaths == null) {
			return;
		}

		for (String resourcePath : resourcePaths) {
			String extension = FileUtil.getExtension(resourcePath);

			File file = new File(resourcePath);

			String fileName = FileUtil.stripExtension(file.getName());

			if (!extension.equals("xml")) {
				continue;
			}

			Map<Locale, String> nameMap = new HashMap<Locale, String>();

			nameMap.put(Locale.getDefault(), fileName);

			Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

			descriptionMap.put(Locale.getDefault(), fileName);

			InputStream inputStream = getFileInputStream(resourcePath);

			String xsd = StringUtil.read(inputStream);

			long classNameId = PortalUtil.getClassNameId(
				DDLRecordSet.class.getName());

			String storageType = PropsUtil.get(
				PropsKeys.DYNAMIC_DATA_LISTS_STORAGE_TYPE);

			if (!storageType.equals("xml") && !storageType.equals("expando")) {
				storageType = "xml";
			}

			DDMStructure ddmStructure =
				DDMStructureLocalServiceUtil.addStructure(
					userId, groupId,
					DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
					classNameId, null, nameMap, descriptionMap, xsd,
					storageType, DDMStructureConstants.TYPE_DEFAULT,
					serviceContext);

			addDDLDisplayTemplates(ddmStructure);
			addDDLFormTemplates(ddmStructure);
		}
	}

	protected void addDDLTemplate(String dirName)
		throws IOException, PortalException, SystemException {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(dirName));

		if (resourcePaths == null) {
			return;
		}

		StringBundler sb = new StringBundler(resourcesDir);

		sb.append(dirName);
		sb.append("/");
		sb.append(_DDL_STRUCTURE);

		addDDLStructure(sb.toString());
	}

	protected void addDDMTemplate(
			long templateGroupId, long ddmStructureId, String resourcePath,
			String language, String type, String mode)
		throws IOException, PortalException, SystemException {

		File file = new File(resourcePath);

		String fileName = FileUtil.stripExtension(file.getName());

		long classNameId = PortalUtil.getClassNameId(
			DDMStructure.class.getName());

		Map<Locale, String> nameMap = new HashMap<Locale, String>();

		nameMap.put(Locale.getDefault(), fileName);

		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

		descriptionMap.put(Locale.getDefault(), fileName);

		String script = getFileContent(resourcePath);

		if (Validator.isNull(script)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Skipping " + resourcePath +
						" because the file is empty.");
			}

			return;
		}

		boolean cacheable = false;

		boolean smallImage = false;

		String smallImageURL = "";

		DDMTemplateLocalServiceUtil.addTemplate(
			userId, templateGroupId, classNameId, ddmStructureId, null, nameMap,
			descriptionMap, type, mode, language, script, cacheable, smallImage,
			smallImageURL, null, serviceContext);
	}

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

			String content = getFileContent(resourcePath);

			if (Validator.isNull(content)) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Skipping " + resourcePath +
							" because the file is empty.");
				}

				continue;
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(content);

			JSONObject pageTemplateJsonObject = jsonObject.getJSONObject(
				"page_template");

			String name = pageTemplateJsonObject.getString(
				"name", "New Page Template");

			Map<Locale, String> nameMap = new HashMap<Locale, String>();
			nameMap.put(Locale.getDefault(), name);

			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.addLayoutPrototype(
					userId, companyId, nameMap, name, true, serviceContext);

			JSONArray columnsJSONArray = pageTemplateJsonObject.getJSONArray(
				"columns");

			Layout layout = layoutPrototype.getLayout();

			addLayoutColumns(
				layout, LayoutTypePortletConstants.COLUMN_PREFIX,
				columnsJSONArray);

			LayoutLocalServiceUtil.updateLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId(), layout.getTypeSettings());
		}
	}

	protected void addTemplates(String fileEntriesDirName) throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug("Adding templates");
		}

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcesDir.concat(fileEntriesDirName));

		if (resourcePaths == null) {
			return;
		}

		addApplicationDisplayTemplates(_APPLICATION_DISPLAY_TEMPLATE_DIR_NAME);

		addDDLTemplate(_DDL_TEMPLATE_DIR_NAME);

		addPageTemplate(_PAGE_TEMPLATE_DIR_NAME);
	}

	protected long getClassNameId(String dirName) {
		long classNameId = 0;

		if (dirName.equals(_APPLICATION_DISPLAY_ASSET_CATEGORY)) {
			classNameId = PortalUtil.getClassNameId(
				AssetCategory.class.getName());
		}
		else if (dirName.equals(_APPLICATION_DISPLAY_ASSET_ENTRY)) {
			classNameId = PortalUtil.getClassNameId(AssetEntry.class.getName());
		}
		else if (dirName.equals(_APPLICATION_DISPLAY_ASSET_TAG)) {
			classNameId = PortalUtil.getClassNameId(AssetTag.class.getName());
		}
		else if (dirName.equals(_APPLICATION_DISPLAY_BLOGS_ENTRY)) {
			classNameId = PortalUtil.getClassNameId(BlogsEntry.class.getName());
		}
		else if (dirName.equals(_APPLICATION_DISPLAY_DOCUMENT_LIBRARY)) {
			classNameId = PortalUtil.getClassNameId(FileEntry.class.getName());
		}
		else if (dirName.equals(_APPLICATION_DISPLAY_SITE_MAP)) {
			classNameId = PortalUtil.getClassNameId(LayoutSet.class.getName());
		}
		else if (dirName.equals(_APPLICATION_DISPLAY_WIKI_PAGE)) {
			classNameId = PortalUtil.getClassNameId(WikiPage.class.getName());
		}

		return classNameId;
	}

	protected String getFileContent(String resourcePath) throws IOException {
		InputStream inputStream = getFileInputStream(resourcePath);

		byte[] bytes = FileUtil.getBytes(inputStream);

		return new String(bytes);
	}

	protected InputStream getFileInputStream(String resourcePath)
		throws IOException {

		URL url = servletContext.getResource(resourcePath);

		URLConnection urlConnection = url.openConnection();

		return urlConnection.getInputStream();
	}

	@Override
	protected void setupAssets(String fileName) throws Exception {
		addDDMStructures(StringPool.BLANK, _JOURNAL_DDM_STRUCTURES_DIR_NAME);

		addDDMTemplates(StringPool.BLANK, _JOURNAL_DDM_TEMPLATES_DIR_NAME);

		addTemplates(_APPLICATION_DISPLAY_TEMPLATE_DIR_NAME);
	}

	private static final String _APPLICATION_DISPLAY_ASSET_CATEGORY =
		"asset_category";

	private static final String _APPLICATION_DISPLAY_ASSET_ENTRY =
		"asset_entry";

	private static final String _APPLICATION_DISPLAY_ASSET_TAG = "asset_tag";

	private static final String _APPLICATION_DISPLAY_BLOGS_ENTRY =
		"blogs_entry";

	private static final String _APPLICATION_DISPLAY_DOCUMENT_LIBRARY =
		"document_library";

	private static final String _APPLICATION_DISPLAY_SITE_MAP = "site_map";

	private static final String _APPLICATION_DISPLAY_TEMPLATE_DIR_NAME =
		"/template/application_display";

	private static final String _APPLICATION_DISPLAY_WIKI_PAGE = "wiki_page";

	private static final String _DDL_STRUCTURE = "structure";

	private static final String _DDL_STRUCTURE_DISPLAY_TEMPLATE =
		"display_template";

	private static final String _DDL_STRUCTURE_FORM_TEMPLATE = "form_template";

	private static final String _DDL_TEMPLATE_DIR_NAME =
		"/template/dynamic_data_list";

	private static final String _JOURNAL_DDM_STRUCTURES_DIR_NAME =
		"/journal/structures/";

	private static final String _JOURNAL_DDM_TEMPLATES_DIR_NAME =
		"/journal/templates/";

	private static final String _PAGE_TEMPLATE_DIR_NAME = "/template/page";

	private static Log _log = LogFactoryUtil.getLog(TemplateImporter.class);

}