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

package com.liferay.sync.engine.service;

import com.liferay.sync.engine.BaseTestCase;
import com.liferay.sync.engine.model.Account;
import com.liferay.sync.engine.service.persistence.AccountPersistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shinn Lok
 */
@RunWith(PowerMockRunner.class)
public class AccountServiceTest extends BaseTestCase {

	@After
	public void tearDown() {
		AccountPersistence accountPersistence =
			AccountService.getAccountPersistence();

		try {
			accountPersistence.delete(_account);
		}
		catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}

	@Test
	public void testAddAccount() throws Exception {
		Account account = AccountService.addAccount(
			"test@liferay.com", "test", "http://localhost:8080/api/jsonws/");

		_account = AccountService.getAccount(account.getAccountId());

		Assert.assertNotNull(_account);
	}

	private static Logger _logger = LoggerFactory.getLogger(
		AccountServiceTest.class);

	private Account _account;

}