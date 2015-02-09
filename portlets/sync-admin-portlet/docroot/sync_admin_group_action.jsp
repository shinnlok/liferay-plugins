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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Group group = (Group)row.getObject();
%>

<c:choose>
	<c:when test='<%= !GetterUtil.getBoolean(group.getTypeSettingsProperty("syncEnabled"), true) %>'>
		<portlet:actionURL name="configureSite" var="configureSiteURL">
			<portlet:param name="groupId" value="<%= String.valueOf(group.getGroupId()) %>" />
			<portlet:param name="syncEnabled" value="true" />
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
			<portlet:param name="groupId" value="<%= String.valueOf(group.getGroupId()) %>" />
			<portlet:param name="syncEnabled" value="false" />
		</portlet:actionURL>

		<liferay-ui:icon
			iconCssClass="icon-remove-sign"
			label="<%= true %>"
			message="disable-sync"
			url="<%= configureSiteURL %>"
		/>
	</c:otherwise>
</c:choose>