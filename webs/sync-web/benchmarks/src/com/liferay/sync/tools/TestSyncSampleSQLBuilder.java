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

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.tools.DBLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author Tina Tian
 */
public class TestSyncSampleSQLBuilder {

	public static void main(String[] args) {
		try {
			SyncSampleSQLBuilder.main(args);

			String outputDir = args[args.length - 1];

			loadHypersonic(outputDir);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void loadHypersonic(String outputDir) throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(
				"jdbc:hsqldb:mem:testSampleSQLBuilderDB;shutdown=true", "sa",
				"");

			DBLoader.loadHypersonic(
				connection, outputDir + "/tables-hypersonic.sql");
			DBLoader.loadHypersonic(
				connection, outputDir + "/indexes-hypersonic.sql");
			DBLoader.loadHypersonic(
				connection, outputDir + "/sample-hypersonic.sql");

			statement = connection.createStatement();

			statement.execute("SHUTDOWN COMPACT");
		}
		finally {
			DataAccess.cleanUp(connection, statement);
		}
	}

}