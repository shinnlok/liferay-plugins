<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String keywords = ParamUtil.getString(request, "keywords");
String tabs1 = ParamUtil.getString(request, "tabs1");

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Group group = (Group)row.getObject();
%>

<liferay-ui:icon-menu icon="" message="">
	<c:choose>
		<c:when test='<%= !GetterUtil.getBoolean(group.getTypeSettingsProperty("syncEnabled"), true) %>'>
			<portlet:actionURL name="configureSite" var="configureSiteURL">
				<portlet:param name="enableSite" value="true" />
				<portlet:param name="groupIds" value="<%= String.valueOf(group.getGroupId()) %>" />
				<portlet:param name="keywords" value="<%= keywords %>" />
				<portlet:param name="syncEnabled" value="true" />
				<portlet:param name="tabs1" value="<%= tabs1 %>" />
			</portlet:actionURL>

			<liferay-ui:icon
				iconCssClass="icon-ok-sign"
				label="<%= true %>"
				message="enable-sync"
				url="<%= configureSiteURL %>"
			/>
		</c:when>
		<c:otherwise>
			<portlet:actionURL name="configureSite" var="configureSiteURL">
				<portlet:param name="enableSite" value="false" />
				<portlet:param name="groupIds" value="<%= String.valueOf(group.getGroupId()) %>" />
				<portlet:param name="keywords" value="<%= keywords %>" />
				<portlet:param name="syncEnabled" value="false" />
				<portlet:param name="tabs1" value="<%= tabs1 %>" />
			</portlet:actionURL>

			<liferay-ui:icon
				iconCssClass="icon-remove-sign"
				label="<%= true %>"
				message="disable-sync"
				url="<%= configureSiteURL %>"
			/>
		</c:otherwise>
	</c:choose>

	<portlet:actionURL name="configurePermissions" var="setViewPermissionURL">
		<portlet:param name="groupIds" value="<%= String.valueOf(group.getGroupId()) %>" />
		<portlet:param name="keywords" value="<%= keywords %>" />
		<portlet:param name="permissions" value="view-permission" />
		<portlet:param name="tabs1" value="<%= tabs1 %>" />
	</portlet:actionURL>

	<liferay-ui:icon
		iconCssClass="icon-ok-sign"
		label="<%= true %>"
		message="set-view-permission"
		url="<%= setViewPermissionURL %>"
	/>

	<portlet:actionURL name="configurePermissions" var="setViewAndAddDiscussionPermissionURL">
		<portlet:param name="groupIds" value="<%= String.valueOf(group.getGroupId()) %>" />
		<portlet:param name="keywords" value="<%= keywords %>" />
		<portlet:param name="permissions" value="view-and-add-discussion-permission" />
		<portlet:param name="tabs1" value="<%= tabs1 %>" />
	</portlet:actionURL>

	<liferay-ui:icon
		iconCssClass="icon-ok-sign"
		label="<%= true %>"
		message="set-view-and-add-discussion-permission"
		url="<%= setViewAndAddDiscussionPermissionURL %>"
	/>

	<portlet:actionURL name="configurePermissions" var="setFullAccessPermissionURL">
		<portlet:param name="groupIds" value="<%= String.valueOf(group.getGroupId()) %>" />
		<portlet:param name="keywords" value="<%= keywords %>" />
		<portlet:param name="permissions" value="full-access-permission" />
		<portlet:param name="tabs1" value="<%= tabs1 %>" />
	</portlet:actionURL>

	<liferay-ui:icon
		iconCssClass="icon-ok-sign"
		label="<%= true %>"
		message="set-full-access-permission"
		url="<%= setFullAccessPermissionURL %>"
	/>
</liferay-ui:icon-menu>