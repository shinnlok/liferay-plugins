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

package com.liferay.portal.arquilian.deployment.builder;

import com.liferay.portal.arquilian.deployment.builder.util.AntLogger;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.rules.TemporaryFolder;

/**
 * @author Manuel de la Peña
 * @author Cristina González
 */
public class WebArchiveUtil implements BuildConstants {

	public static WebArchive createJarWebArchive() throws IOException {
		try {
			_temporaryFolder = new TemporaryFolder();

			_temporaryFolder.create();

			_temporaryFolderRoot = _temporaryFolder.getRoot();

			Project antProject = configureAntProjectForJar();

			antProject.executeTarget(TARGET_DEPLOY);

			String pluginFullVersion = antProject.getProperty(
				PROPERTY_PLUGIN_FULL_VERSION);

			StringBuilder sb = new StringBuilder(4);

			sb.append(antProject.getProperty(PROPERTY_PLUGIN_NAME));
			sb.append("-");
			sb.append(pluginFullVersion);
			sb.append(EXTENSION_JAR);

			File jarFile = new File(
				_temporaryFolderRoot.getAbsolutePath(), sb.toString());

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

			antProject.executeTarget(TARGET_DIRECT_DEPLOY);

			String pluginName = antProject.getProperty(PROPERTY_PLUGIN_NAME);

			pluginName += EXTENSION_WAR;

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

	protected static AntLogger configureAntLogger() {
		AntLogger antLogger = new AntLogger();

		antLogger.setErrorPrintStream(System.err);
		antLogger.setOutputPrintStream(System.out);
		antLogger.setMessageOutputLevel(Project.MSG_INFO);

		return antLogger;
	}

	protected static Project configureAntProject() {
		File buildFile = new File(BUILD_XML_FILE_NAME);

		Project project = new Project();

		project.setUserProperty(PROPERTY_ANT_FILE, buildFile.getAbsolutePath());

		project.addBuildListener(configureAntLogger());

		project.init();

		ProjectHelper projectHelper = ProjectHelper.getProjectHelper();

		project.addReference(PROPERTY_ANT_PROJECTHELPER, projectHelper);

		projectHelper.parse(project, buildFile);

		return project;
	}

	protected static Project configureAntProjectForJar() {
		Project project = configureAntProject();

		project.setProperty(
			PROPERTY_AUTO_DEPLOY_DIR, _temporaryFolderRoot.getAbsolutePath());

		return project;
	}

	protected static Project configureAntProjectForWar() {
		Project project = configureAntProject();

		project.setProperty(
			PROPERTY_APP_SERVER_DEPLOY_DIR,
			_temporaryFolderRoot.getAbsolutePath());

		project.setProperty(PROPERTY_AUTO_DEPLOY_UNPACK_WAR, "false");

		return project;
	}

	private static TemporaryFolder _temporaryFolder;
	private static File _temporaryFolderRoot;

}