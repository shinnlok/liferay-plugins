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
PortletURL portletURL = renderResponse.createRenderURL();

String redirect = ParamUtil.getString(request, "redirect");
%>

<aui:form method="post" name="fm">
	<aui:input name="enableSite" type="hidden" />
	<aui:input name="permissions" type="hidden" />
	<aui:input name="groupIds" type="hidden" />
	<aui:input name="tabs1" type="hidden" value='<%= ParamUtil.getString(request, "tabs1", "sync-admin") %>' />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<aui:fieldset label="sync-sites">
		<div class="alert alert-info">
			<liferay-ui:message key="deactivating-a-site-will-delete-all-associated-files-from-all-clients" />
		</div>

		<aui:nav-bar>
			<aui:nav cssClass="navbar-nav" id="toolbarContainer">

				<%
				String taglibURL = "javascript:" + renderResponse.getNamespace() + "configureSite('true');";
				%>

				<aui:nav-item href="<%= taglibURL %>" iconCssClass="icon-ok" label="enable-sync" />

				<%
				taglibURL = "javascript:" + renderResponse.getNamespace() + "configureSite('false');";
				%>

				<aui:nav-item href="<%= taglibURL %>" iconCssClass="icon-remove" label="disable-sync" />

				<aui:nav-item dropdown="<%= true %>" iconCssClass="icon-lock" id="permissionsButton" label="permissions">
					<%
					taglibURL = "javascript:" + renderResponse.getNamespace() + "configurePermissions('view-permission');";
					%>

					<aui:nav-item href="<%= taglibURL %>" label="set-view-permission" />

					<%
					taglibURL = "javascript:" + renderResponse.getNamespace() + "configurePermissions('view-and-add-discussion-permission');";
					%>

					<aui:nav-item href="<%= taglibURL %>" label="set-view-and-add-discussion-permission" />

					<%
					taglibURL = "javascript:" + renderResponse.getNamespace() + "configurePermissions('full-access-permission');";
					%>

					<aui:nav-item href="<%= taglibURL %>" label="set-full-access-permission" />
				</aui:nav-item>
			</aui:nav>
		</aui:nav-bar>

		<%
		List<Group> groups = GroupLocalServiceUtil.getGroups(themeDisplay.getCompanyId(), GroupConstants.ANY_PARENT_GROUP_ID, true);

		groups = ListUtil.copy(groups);

		Iterator<Group> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Group group = iterator.next();

			if (group.isCompany()) {
				iterator.remove();
			}
		}
		%>

		<liferay-ui:search-container
			emptyResultsMessage="no-sites-found"
			iteratorURL="<%= portletURL %>"
			rowChecker="<%= new RowChecker(renderResponse) %>"
			total="<%= groups.size() %>"
		>
			<liferay-ui:search-container-results
				results="<%= ListUtil.subList(groups, searchContainer.getStart(), searchContainer.getEnd()) %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.model.Group"
				escapedModel="<%= true %>"
				keyProperty="groupId"
				modelVar="group"
			>
				<liferay-ui:search-container-column-text
					name="name"
					orderable="<%= true %>"
					value="<%= group.getDescriptiveName() %>"
				/>

				<liferay-ui:search-container-column-text
					name="sync-enabled"
					value='<%= LanguageUtil.get(locale, GetterUtil.getString(group.getTypeSettingsProperty("syncEnabled"), "true")) %>'
				/>

				<liferay-ui:search-container-column-text
					name="permissions"
					value='<%= LanguageUtil.get(locale, GetterUtil.getString(group.getTypeSettingsProperty("permissions"), "full-access-permission")) %>'
				/>

				<liferay-ui:search-container-column-jsp
					align="right"
					cssClass="entry-action"
					path="/sync_admin_group_action.jsp"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator />
		</liferay-ui:search-container>
	</aui:fieldset>
</aui:form>

<aui:script use="aui-base">
	Liferay.provide(
		window,
		'<portlet:namespace />configurePermissions',
		function(permissions) {
			document.<portlet:namespace />fm.<portlet:namespace />permissions.value = permissions;

			document.<portlet:namespace />fm.<portlet:namespace />groupIds.value = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, '<portlet:namespace />allRowIds');

			submitForm(document.<portlet:namespace />fm, '<liferay-portlet:actionURL name="configurePermissions" />');
		},
		['aui-base']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />configureSite',
		function(enableSite) {
			document.<portlet:namespace />fm.<portlet:namespace />enableSite.value = enableSite;

			document.<portlet:namespace />fm.<portlet:namespace />groupIds.value = Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, '<portlet:namespace />allRowIds');

			submitForm(document.<portlet:namespace />fm, '<liferay-portlet:actionURL name="configureSite" />');
		},
		['aui-base']
	);
</aui:script>