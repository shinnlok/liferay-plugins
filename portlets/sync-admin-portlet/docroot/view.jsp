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
String redirect = ParamUtil.getString(request, "redirect");

portletPreferences = SyncDLObjectServiceUtil.getPortletPreferences();

boolean enabled = PrefsPropsUtil.getBoolean(portletPreferences, themeDisplay.getCompanyId(), PortletPropsKeys.SYNC_SERVICES_ENABLED);
int maxConnections = PrefsPropsUtil.getInteger(portletPreferences, themeDisplay.getCompanyId(), PortletPropsKeys.SYNC_CLIENT_MAX_CONNECTIONS);
int pollInterval = PrefsPropsUtil.getInteger(portletPreferences, themeDisplay.getCompanyId(), PortletPropsKeys.SYNC_CLIENT_POLL_INTERVAL);
%>

<liferay-portlet:actionURL var="configurationActionURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "submit();" %>'>
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<aui:fieldset>
		<aui:input name="enabled" type="checkbox" value="<%= enabled %>" />
	</aui:fieldset>

	<aui:fieldset label="sync-sites">
		<div class="alert alert-info">
			<liferay-ui:message key="deactivating-a-site-will-delete-all-associated-files-from-all-clients" />
		</div>

		<liferay-ui:header
			title="sites"
		/>

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
			total="<%= groups.size() %>"
		>
			<liferay-ui:search-container-results
				results="<%= groups %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.model.Group"
				escapedModel="<%= true %>"
				keyProperty="groupId"
				modelVar="group"
			>
				<liferay-ui:search-container-column-text
					name="name"
					value="<%= group.getDescriptiveName() %>"
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

	<aui:fieldset label="sync-client">
		<aui:input helpMessage="max-connections-help" label="max-connections" name="maxConnections" type="text" value="<%= maxConnections %>" wrapperCssClass="lfr-input-text-container">
			<aui:validator name="digits" />
			<aui:validator name="min">1</aui:validator>
		</aui:input>

		<aui:input helpMessage="poll-interval-help" label="poll-interval" name="pollInterval" type="text" value="<%= pollInterval %>" wrapperCssClass="lfr-input-text-container">
			<aui:validator name="digits" />
			<aui:validator name="min">1</aui:validator>
		</aui:input>
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script>
	function <portlet:namespace />submit() {
		submitForm(document.<portlet:namespace />fm, '<portlet:actionURL name="submit" />');
	}
</aui:script>