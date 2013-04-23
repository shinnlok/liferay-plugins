/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This file is part of Liferay Social Office. Liferay Social Office is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Liferay Social Office is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Liferay Social Office. If not, see http://www.gnu.org/licenses/agpl-3.0.html.
 */

package com.liferay.so.hook.upgrade.v2_0_0;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Ryan Park
 */
public class UpgradeDocumentLibrary extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		List<Group> groups = GroupLocalServiceUtil.getGroups(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Group group : groups) {
			if (!group.isSite() || group.isControlPanel() || group.isGuest()) {
				continue;
			}

			Layout layout = LayoutLocalServiceUtil.getLayout(
				group.getGroupId(), group.hasPrivateLayouts(), 3);

			PortletPreferences portletSetup =
				PortletPreferencesFactoryUtil.getLayoutPortletSetup(
					layout, PortletKeys.DOCUMENT_LIBRARY);

			long rootFolderId = GetterUtil.getLong(
				portletSetup.getValue("rootFolderId", null),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

			if (rootFolderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				continue;
			}

			portletSetup.setValue(
				"rootFolderId",
				String.valueOf(DLFolderConstants.DEFAULT_PARENT_FOLDER_ID));

			portletSetup.store();

			moveDocumentsAndFolders(group.getGroupId(), rootFolderId);
		}
	}

	protected void moveDocumentsAndFolders(long groupId, long rootFolderId)
		throws Exception {

		boolean deleteFolder = true;

		List<DLFileEntry> dlFileEntries =
			DLFileEntryLocalServiceUtil.getFileEntries(
				groupId, rootFolderId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null);

		for (DLFileEntry dlFileEntry : dlFileEntries) {
			ServiceContext serviceContext = new ServiceContext();

			try {
				DLAppLocalServiceUtil.moveFileEntry(
					dlFileEntry.getUserId(), dlFileEntry.getFileEntryId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, serviceContext);
			}
			catch (Exception e) {
				deleteFolder = false;
			}
		}

		List<DLFolder> dlFolders = DLFolderLocalServiceUtil.getFolders(
			groupId, rootFolderId);

		for (DLFolder dlFolder : dlFolders) {
			ServiceContext serviceContext = new ServiceContext();

			try {
				DLAppLocalServiceUtil.updateFolder(
					dlFolder.getFolderId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					dlFolder.getName(), dlFolder.getDescription(),
					serviceContext);
			}
			catch (Exception e) {
				deleteFolder = false;
			}
		}

		if (deleteFolder) {
			DLAppLocalServiceUtil.deleteFolder(rootFolderId);
		}
	}

}