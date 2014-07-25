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

package com.liferay.knowledgebase.service.impl;

import com.liferay.knowledgebase.model.KBComment;
import com.liferay.knowledgebase.service.base.KBCommentServiceBaseImpl;
import com.liferay.knowledgebase.service.permission.AdminPermission;
import com.liferay.knowledgebase.service.permission.KBCommentPermission;
import com.liferay.knowledgebase.util.ActionKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.service.ServiceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class KBCommentServiceImpl extends KBCommentServiceBaseImpl {

	@Override
	public KBComment deleteKBComment(KBComment kbComment)
		throws PortalException {

		KBCommentPermission.check(
			getPermissionChecker(), kbComment, ActionKeys.DELETE);

		return kbCommentLocalService.deleteKBComment(kbComment);
	}

	@Override
	public KBComment deleteKBComment(long kbCommentId) throws PortalException {
		KBComment kbComment = kbCommentPersistence.findByPrimaryKey(
			kbCommentId);

		return deleteKBComment(kbComment);
	}

	@Override
	public KBComment getKBComment(long kbCommentId) throws PortalException {
		KBCommentPermission.check(
			getPermissionChecker(), kbCommentId, ActionKeys.VIEW);

		return kbCommentLocalService.getKBComment(kbCommentId);
	}

	public List<KBComment> getKBComments(
			long groupId, int status, int start, int end)
		throws PortalException {

		List<KBComment> kbComments = new ArrayList<KBComment>();

		if (AdminPermission.contains(
				getPermissionChecker(), groupId, ActionKeys.VIEW_KB_FEEDBACK)) {

			kbComments = kbCommentPersistence.findByG_S(
				groupId, status, start, end);
		}

		return kbComments;
	}

	public int getKBCommentsCount(long groupId, int status)
		throws PortalException {

		int kbCommentsCount = 0;

		if (AdminPermission.contains(
				getPermissionChecker(), groupId, ActionKeys.VIEW_KB_FEEDBACK)) {

			kbCommentsCount = kbCommentPersistence.countByG_S(groupId, status);
		}

		return kbCommentsCount;
	}

	public KBComment updateKBComment(
			long kbCommentId, long classNameId, long classPK, String content,
			boolean helpful, int status, ServiceContext serviceContext)
		throws PortalException {

		KBCommentPermission.check(
			getPermissionChecker(), kbCommentId, ActionKeys.UPDATE);

		return kbCommentLocalService.updateKBComment(
			kbCommentId, classNameId, classPK, content, helpful, status,
			serviceContext);
	}

	public KBComment updateKBComment(
			long kbCommentId, long classNameId, long classPK, String content,
			boolean helpful, ServiceContext serviceContext)
		throws PortalException {

		KBComment kbComment = kbCommentPersistence.findByPrimaryKey(
			kbCommentId);

		return updateKBComment(
			kbCommentId, classNameId, classPK, content, helpful,
			kbComment.getStatus(), serviceContext);
	}

	public KBComment updateStatus(
			long kbCommentId, int status, ServiceContext serviceContext)
		throws PortalException {

		KBCommentPermission.check(
			getPermissionChecker(), kbCommentId, ActionKeys.UPDATE);

		return kbCommentLocalService.updateStatus(
			kbCommentId, status, serviceContext);
	}

}