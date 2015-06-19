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

package com.liferay.sync.hook.listeners;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.User;
import com.liferay.sync.util.SyncAuthTokenUtil;

/**
 * @author Shinn Lok
 */
public class UserModelListener extends BaseModelListener<User> {

	@Override
	public void onAfterUpdate(User user) throws ModelListenerException {
		if (user.isPasswordModified()) {
			SyncAuthTokenUtil.expireTokens(user.getUserId());
		}
	}

}