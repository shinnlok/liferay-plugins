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

package com.liferay.mail.model.impl;

import com.liferay.mail.model.Attachment;
import com.liferay.mail.service.AttachmentLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class MessageImpl extends MessageBaseImpl {

	public MessageImpl() {
	}

	public long getGroupId() throws PortalException {
		User user = UserLocalServiceUtil.getUser(getUserId());

		Group group = user.getGroup();

		return group.getGroupId();
	}

	public boolean hasAttachments() {
		String contentType = getContentType();

		if ((contentType != null) && contentType.startsWith(_MULTIPART_MIXED)) {
			return true;
		}

		List<Attachment> attachments =
			AttachmentLocalServiceUtil.getAttachments(getMessageId());

		return !attachments.isEmpty();
	}

	public boolean hasFlag(int flag) {
		int[] flags = StringUtil.split(getFlags(), 0);

		return ArrayUtil.contains(flags, flag);
	}

	private static final String _MULTIPART_MIXED = "multipart/MIXED";

}