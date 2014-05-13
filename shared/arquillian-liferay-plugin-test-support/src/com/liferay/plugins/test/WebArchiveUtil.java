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

package com.liferay.plugins.test;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

/**
 * @author Manuel de la Peña
 * @author Cristina González
 */
public class WebArchiveUtil {

	public static WebArchive createJarWebArchive() throws IOException {
		try {
			_temporaryFolder = new TemporaryFolder();

			_temporaryFolder.create();

			_temporaryFolderRoot = _temporaryFolder.getRoot();

			Project antProject = configureAntProjectForJar();

			antProject.executeTarget(
				LiferayPluginsBuildConstants.TARGET_DEPLOY);

			String pluginName =
				antProject.getProperty(
					LiferayPluginsBuildConstants.PROPERTY_PLUGIN_NAME);

			String pluginFullVersion =
				antProject.getProperty(
					LiferayPluginsBuildConstants.PROPERTY_PLUGIN_FULL_VERSION);

			pluginName +=
				"-" + pluginFullVersion +
					LiferayPluginsBuildConstants.EXTENSION_JAR;

			File jarFile = new File(
				_temporaryFolderRoot.getAbsolutePath(), pluginName);

			WebArchive webArchive = ShrinkWrap.createFromZipFile(
				WebArchive.class, jarFile);

			return webArchive;
		}
		finally {
			_temporaryFolder.delete();
		}
	}

	public static WebArchive createWebArchive() throws IOException {
		try {
			_temporaryFolder = new TemporaryFolder();

			_temporaryFolder.create();

			_temporaryFolderRoot = _temporaryFolder.getRoot();

			Project antProject = configureAntProjectForWar();

			antProject.executeTarget(
				LiferayPluginsBuildConstants.TARGET_DIRECT_DEPLOY);

			String pluginName =
				antProject.getProperty(
					LiferayPluginsBuildConstants.PROPERTY_PLUGIN_NAME);

			pluginName += LiferayPluginsBuildConstants.EXTENSION_WAR;

			File warFile = new File(
				_temporaryFolderRoot.getAbsolutePath(), pluginName);

			WebArchive webArchive = ShrinkWrap.createFromZipFile(
				WebArchive.class, warFile);

			return webArchive;
		}
		finally {
			_temporaryFolder.delete();
		}
	}

	protected static Project configureAntProject() {
		File buildFile = new File(
			LiferayPluginsBuildConstants.BUILD_XML_FILE_NAME);

		Project project = new Project();

		project.setUserProperty(
			LiferayPluginsBuildConstants.PROPERTY_ANT_FILE,
			buildFile.getAbsolutePath());

		project.init();

		ProjectHelper projectHelper = ProjectHelper.getProjectHelper();

		project.addReference(
			LiferayPluginsBuildConstants.PROPERTY_ANT_PROJECTHELPER,
			projectHelper);

		projectHelper.parse(project, buildFile);

		return project;
	}

	protected static Project configureAntProjectForJar() {
		Project project = configureAntProject();

		project.setProperty(
			LiferayPluginsBuildConstants.PROPERTY_AUTO_DEPLOY_DIR,
			_temporaryFolderRoot.getAbsolutePath());

		return project;
	}

	protected static Project configureAntProjectForWar() {
		Project project = configureAntProject();

		project.setProperty(
			LiferayPluginsBuildConstants.PROPERTY_APP_SERVER_DEPLOY_DIR,
			_temporaryFolderRoot.getAbsolutePath());

		project.setProperty(
			LiferayPluginsBuildConstants.PROPERTY_AUTO_DEPLOY_UNPACK_WAR,
			"false");

		return project;
	}

	private static TemporaryFolder _temporaryFolder;
	private static File _temporaryFolderRoot;

}