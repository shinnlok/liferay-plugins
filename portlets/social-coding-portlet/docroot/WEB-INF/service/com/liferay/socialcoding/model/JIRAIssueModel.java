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

package com.liferay.socialcoding.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the JIRAIssue service. Represents a row in the &quot;jiraissue&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.socialcoding.model.impl.JIRAIssueModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.socialcoding.model.impl.JIRAIssueImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see JIRAIssue
 * @see com.liferay.socialcoding.model.impl.JIRAIssueImpl
 * @see com.liferay.socialcoding.model.impl.JIRAIssueModelImpl
 * @generated
 */
@ProviderType
public interface JIRAIssueModel extends BaseModel<JIRAIssue> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a j i r a issue model instance should use the {@link JIRAIssue} interface instead.
	 */

	/**
	 * Returns the primary key of this j i r a issue.
	 *
	 * @return the primary key of this j i r a issue
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this j i r a issue.
	 *
	 * @param primaryKey the primary key of this j i r a issue
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the jira issue ID of this j i r a issue.
	 *
	 * @return the jira issue ID of this j i r a issue
	 */
	public long getJiraIssueId();

	/**
	 * Sets the jira issue ID of this j i r a issue.
	 *
	 * @param jiraIssueId the jira issue ID of this j i r a issue
	 */
	public void setJiraIssueId(long jiraIssueId);

	/**
	 * Returns the create date of this j i r a issue.
	 *
	 * @return the create date of this j i r a issue
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this j i r a issue.
	 *
	 * @param createDate the create date of this j i r a issue
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this j i r a issue.
	 *
	 * @return the modified date of this j i r a issue
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this j i r a issue.
	 *
	 * @param modifiedDate the modified date of this j i r a issue
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the project ID of this j i r a issue.
	 *
	 * @return the project ID of this j i r a issue
	 */
	public long getProjectId();

	/**
	 * Sets the project ID of this j i r a issue.
	 *
	 * @param projectId the project ID of this j i r a issue
	 */
	public void setProjectId(long projectId);

	/**
	 * Returns the issue number of this j i r a issue.
	 *
	 * @return the issue number of this j i r a issue
	 */
	public long getIssueNumber();

	/**
	 * Sets the issue number of this j i r a issue.
	 *
	 * @param issueNumber the issue number of this j i r a issue
	 */
	public void setIssueNumber(long issueNumber);

	/**
	 * Returns the summary of this j i r a issue.
	 *
	 * @return the summary of this j i r a issue
	 */
	@AutoEscape
	public String getSummary();

	/**
	 * Sets the summary of this j i r a issue.
	 *
	 * @param summary the summary of this j i r a issue
	 */
	public void setSummary(String summary);

	/**
	 * Returns the description of this j i r a issue.
	 *
	 * @return the description of this j i r a issue
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this j i r a issue.
	 *
	 * @param description the description of this j i r a issue
	 */
	public void setDescription(String description);

	/**
	 * Returns the reporter jira user ID of this j i r a issue.
	 *
	 * @return the reporter jira user ID of this j i r a issue
	 */
	@AutoEscape
	public String getReporterJiraUserId();

	/**
	 * Sets the reporter jira user ID of this j i r a issue.
	 *
	 * @param reporterJiraUserId the reporter jira user ID of this j i r a issue
	 */
	public void setReporterJiraUserId(String reporterJiraUserId);

	/**
	 * Returns the assignee jira user ID of this j i r a issue.
	 *
	 * @return the assignee jira user ID of this j i r a issue
	 */
	@AutoEscape
	public String getAssigneeJiraUserId();

	/**
	 * Sets the assignee jira user ID of this j i r a issue.
	 *
	 * @param assigneeJiraUserId the assignee jira user ID of this j i r a issue
	 */
	public void setAssigneeJiraUserId(String assigneeJiraUserId);

	/**
	 * Returns the resolution of this j i r a issue.
	 *
	 * @return the resolution of this j i r a issue
	 */
	@AutoEscape
	public String getResolution();

	/**
	 * Sets the resolution of this j i r a issue.
	 *
	 * @param resolution the resolution of this j i r a issue
	 */
	public void setResolution(String resolution);

	/**
	 * Returns the status of this j i r a issue.
	 *
	 * @return the status of this j i r a issue
	 */
	@AutoEscape
	public String getStatus();

	/**
	 * Sets the status of this j i r a issue.
	 *
	 * @param status the status of this j i r a issue
	 */
	public void setStatus(String status);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(com.liferay.socialcoding.model.JIRAIssue jiraIssue);

	@Override
	public int hashCode();

	@Override
	public CacheModel<com.liferay.socialcoding.model.JIRAIssue> toCacheModel();

	@Override
	public com.liferay.socialcoding.model.JIRAIssue toEscapedModel();

	@Override
	public com.liferay.socialcoding.model.JIRAIssue toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}