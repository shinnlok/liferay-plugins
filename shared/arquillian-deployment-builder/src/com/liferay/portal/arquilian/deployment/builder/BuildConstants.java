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

/**
 * @author Manuel de la Peña
 * @author Cristina González
 */
public interface BuildConstants {

	public final String BUILD_XML_FILE_NAME = "build.xml";

	public final String EXTENSION_JAR = ".jar";

	public final String EXTENSION_WAR = ".war";

	public final String PROPERTY_ANT_FILE = "ant.file";

	public final String PROPERTY_ANT_PROJECTHELPER = "ant.projectHelper";

	public final String PROPERTY_APP_SERVER_DEPLOY_DIR =
		"app.server.deploy.dir";

	public final String PROPERTY_AUTO_DEPLOY_DIR = "auto.deploy.dir";

	public final String PROPERTY_AUTO_DEPLOY_UNPACK_WAR =
		"auto.deploy.unpack.war";

	public final String PROPERTY_PLUGIN_FULL_VERSION = "plugin.full.version";

	public final String PROPERTY_PLUGIN_NAME = "plugin.name";

	public final String TARGET_DEPLOY = "deploy";

	public final String TARGET_DIRECT_DEPLOY = "direct-deploy";

}