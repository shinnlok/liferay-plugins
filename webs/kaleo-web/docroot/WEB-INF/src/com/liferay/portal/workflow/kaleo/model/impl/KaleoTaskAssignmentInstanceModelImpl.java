/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.workflow.kaleo.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstanceModel;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;

/**
 * The base model implementation for the KaleoTaskAssignmentInstance service. Represents a row in the &quot;KaleoTaskAssignmentInstance&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstanceModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link KaleoTaskAssignmentInstanceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTaskAssignmentInstanceImpl
 * @see com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance
 * @see com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstanceModel
 * @generated
 */
public class KaleoTaskAssignmentInstanceModelImpl extends BaseModelImpl<KaleoTaskAssignmentInstance>
	implements KaleoTaskAssignmentInstanceModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a kaleo task assignment instance model instance should use the {@link com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance} interface instead.
	 */
	public static final String TABLE_NAME = "KaleoTaskAssignmentInstance";
	public static final Object[][] TABLE_COLUMNS = {
			{ "kaleoTaskAssignmentInstanceId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "kaleoDefinitionId", Types.BIGINT },
			{ "kaleoInstanceId", Types.BIGINT },
			{ "kaleoInstanceTokenId", Types.BIGINT },
			{ "kaleoTaskInstanceTokenId", Types.BIGINT },
			{ "kaleoTaskId", Types.BIGINT },
			{ "kaleoTaskName", Types.VARCHAR },
			{ "assigneeClassName", Types.VARCHAR },
			{ "assigneeClassPK", Types.BIGINT },
			{ "completed", Types.BOOLEAN },
			{ "completionDate", Types.TIMESTAMP }
		};
	public static final String TABLE_SQL_CREATE = "create table KaleoTaskAssignmentInstance (kaleoTaskAssignmentInstanceId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(200) null,createDate DATE null,modifiedDate DATE null,kaleoDefinitionId LONG,kaleoInstanceId LONG,kaleoInstanceTokenId LONG,kaleoTaskInstanceTokenId LONG,kaleoTaskId LONG,kaleoTaskName VARCHAR(200) null,assigneeClassName VARCHAR(200) null,assigneeClassPK LONG,completed BOOLEAN,completionDate DATE null)";
	public static final String TABLE_SQL_DROP = "drop table KaleoTaskAssignmentInstance";
	public static final String ORDER_BY_JPQL = " ORDER BY kaleoTaskAssignmentInstance.kaleoTaskAssignmentInstanceId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY KaleoTaskAssignmentInstance.kaleoTaskAssignmentInstanceId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance"),
			true);
	public static long COMPANYID_COLUMN_BITMASK = 1L;
	public static long KALEODEFINITIONID_COLUMN_BITMASK = 2L;
	public static long KALEOINSTANCEID_COLUMN_BITMASK = 4L;
	public static long KALEOTASKINSTANCETOKENID_COLUMN_BITMASK = 8L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance"));

	public KaleoTaskAssignmentInstanceModelImpl() {
	}

	public long getPrimaryKey() {
		return _kaleoTaskAssignmentInstanceId;
	}

	public void setPrimaryKey(long primaryKey) {
		setKaleoTaskAssignmentInstanceId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_kaleoTaskAssignmentInstanceId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return KaleoTaskAssignmentInstance.class;
	}

	public String getModelClassName() {
		return KaleoTaskAssignmentInstance.class.getName();
	}

	public long getKaleoTaskAssignmentInstanceId() {
		return _kaleoTaskAssignmentInstanceId;
	}

	public void setKaleoTaskAssignmentInstanceId(
		long kaleoTaskAssignmentInstanceId) {
		_kaleoTaskAssignmentInstanceId = kaleoTaskAssignmentInstanceId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public long getKaleoDefinitionId() {
		return _kaleoDefinitionId;
	}

	public void setKaleoDefinitionId(long kaleoDefinitionId) {
		_columnBitmask |= KALEODEFINITIONID_COLUMN_BITMASK;

		if (!_setOriginalKaleoDefinitionId) {
			_setOriginalKaleoDefinitionId = true;

			_originalKaleoDefinitionId = _kaleoDefinitionId;
		}

		_kaleoDefinitionId = kaleoDefinitionId;
	}

	public long getOriginalKaleoDefinitionId() {
		return _originalKaleoDefinitionId;
	}

	public long getKaleoInstanceId() {
		return _kaleoInstanceId;
	}

	public void setKaleoInstanceId(long kaleoInstanceId) {
		_columnBitmask |= KALEOINSTANCEID_COLUMN_BITMASK;

		if (!_setOriginalKaleoInstanceId) {
			_setOriginalKaleoInstanceId = true;

			_originalKaleoInstanceId = _kaleoInstanceId;
		}

		_kaleoInstanceId = kaleoInstanceId;
	}

	public long getOriginalKaleoInstanceId() {
		return _originalKaleoInstanceId;
	}

	public long getKaleoInstanceTokenId() {
		return _kaleoInstanceTokenId;
	}

	public void setKaleoInstanceTokenId(long kaleoInstanceTokenId) {
		_kaleoInstanceTokenId = kaleoInstanceTokenId;
	}

	public long getKaleoTaskInstanceTokenId() {
		return _kaleoTaskInstanceTokenId;
	}

	public void setKaleoTaskInstanceTokenId(long kaleoTaskInstanceTokenId) {
		_columnBitmask |= KALEOTASKINSTANCETOKENID_COLUMN_BITMASK;

		if (!_setOriginalKaleoTaskInstanceTokenId) {
			_setOriginalKaleoTaskInstanceTokenId = true;

			_originalKaleoTaskInstanceTokenId = _kaleoTaskInstanceTokenId;
		}

		_kaleoTaskInstanceTokenId = kaleoTaskInstanceTokenId;
	}

	public long getOriginalKaleoTaskInstanceTokenId() {
		return _originalKaleoTaskInstanceTokenId;
	}

	public long getKaleoTaskId() {
		return _kaleoTaskId;
	}

	public void setKaleoTaskId(long kaleoTaskId) {
		_kaleoTaskId = kaleoTaskId;
	}

	public String getKaleoTaskName() {
		if (_kaleoTaskName == null) {
			return StringPool.BLANK;
		}
		else {
			return _kaleoTaskName;
		}
	}

	public void setKaleoTaskName(String kaleoTaskName) {
		_kaleoTaskName = kaleoTaskName;
	}

	public String getAssigneeClassName() {
		if (_assigneeClassName == null) {
			return StringPool.BLANK;
		}
		else {
			return _assigneeClassName;
		}
	}

	public void setAssigneeClassName(String assigneeClassName) {
		_assigneeClassName = assigneeClassName;
	}

	public long getAssigneeClassPK() {
		return _assigneeClassPK;
	}

	public void setAssigneeClassPK(long assigneeClassPK) {
		_assigneeClassPK = assigneeClassPK;
	}

	public boolean getCompleted() {
		return _completed;
	}

	public boolean isCompleted() {
		return _completed;
	}

	public void setCompleted(boolean completed) {
		_completed = completed;
	}

	public Date getCompletionDate() {
		return _completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		_completionDate = completionDate;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public KaleoTaskAssignmentInstance toEscapedModel() {
		if (isEscapedModel()) {
			return (KaleoTaskAssignmentInstance)this;
		}
		else {
			if (_escapedModelProxy == null) {
				_escapedModelProxy = (KaleoTaskAssignmentInstance)ProxyUtil.newProxyInstance(_classLoader,
						_escapedModelProxyInterfaces,
						new AutoEscapeBeanHandler(this));
			}

			return _escapedModelProxy;
		}
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
					KaleoTaskAssignmentInstance.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		KaleoTaskAssignmentInstanceImpl kaleoTaskAssignmentInstanceImpl = new KaleoTaskAssignmentInstanceImpl();

		kaleoTaskAssignmentInstanceImpl.setKaleoTaskAssignmentInstanceId(getKaleoTaskAssignmentInstanceId());
		kaleoTaskAssignmentInstanceImpl.setGroupId(getGroupId());
		kaleoTaskAssignmentInstanceImpl.setCompanyId(getCompanyId());
		kaleoTaskAssignmentInstanceImpl.setUserId(getUserId());
		kaleoTaskAssignmentInstanceImpl.setUserName(getUserName());
		kaleoTaskAssignmentInstanceImpl.setCreateDate(getCreateDate());
		kaleoTaskAssignmentInstanceImpl.setModifiedDate(getModifiedDate());
		kaleoTaskAssignmentInstanceImpl.setKaleoDefinitionId(getKaleoDefinitionId());
		kaleoTaskAssignmentInstanceImpl.setKaleoInstanceId(getKaleoInstanceId());
		kaleoTaskAssignmentInstanceImpl.setKaleoInstanceTokenId(getKaleoInstanceTokenId());
		kaleoTaskAssignmentInstanceImpl.setKaleoTaskInstanceTokenId(getKaleoTaskInstanceTokenId());
		kaleoTaskAssignmentInstanceImpl.setKaleoTaskId(getKaleoTaskId());
		kaleoTaskAssignmentInstanceImpl.setKaleoTaskName(getKaleoTaskName());
		kaleoTaskAssignmentInstanceImpl.setAssigneeClassName(getAssigneeClassName());
		kaleoTaskAssignmentInstanceImpl.setAssigneeClassPK(getAssigneeClassPK());
		kaleoTaskAssignmentInstanceImpl.setCompleted(getCompleted());
		kaleoTaskAssignmentInstanceImpl.setCompletionDate(getCompletionDate());

		kaleoTaskAssignmentInstanceImpl.resetOriginalValues();

		return kaleoTaskAssignmentInstanceImpl;
	}

	public int compareTo(
		KaleoTaskAssignmentInstance kaleoTaskAssignmentInstance) {
		int value = 0;

		if (getKaleoTaskAssignmentInstanceId() < kaleoTaskAssignmentInstance.getKaleoTaskAssignmentInstanceId()) {
			value = -1;
		}
		else if (getKaleoTaskAssignmentInstanceId() > kaleoTaskAssignmentInstance.getKaleoTaskAssignmentInstanceId()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		KaleoTaskAssignmentInstance kaleoTaskAssignmentInstance = null;

		try {
			kaleoTaskAssignmentInstance = (KaleoTaskAssignmentInstance)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = kaleoTaskAssignmentInstance.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		KaleoTaskAssignmentInstanceModelImpl kaleoTaskAssignmentInstanceModelImpl =
			this;

		kaleoTaskAssignmentInstanceModelImpl._originalCompanyId = kaleoTaskAssignmentInstanceModelImpl._companyId;

		kaleoTaskAssignmentInstanceModelImpl._setOriginalCompanyId = false;

		kaleoTaskAssignmentInstanceModelImpl._originalKaleoDefinitionId = kaleoTaskAssignmentInstanceModelImpl._kaleoDefinitionId;

		kaleoTaskAssignmentInstanceModelImpl._setOriginalKaleoDefinitionId = false;

		kaleoTaskAssignmentInstanceModelImpl._originalKaleoInstanceId = kaleoTaskAssignmentInstanceModelImpl._kaleoInstanceId;

		kaleoTaskAssignmentInstanceModelImpl._setOriginalKaleoInstanceId = false;

		kaleoTaskAssignmentInstanceModelImpl._originalKaleoTaskInstanceTokenId = kaleoTaskAssignmentInstanceModelImpl._kaleoTaskInstanceTokenId;

		kaleoTaskAssignmentInstanceModelImpl._setOriginalKaleoTaskInstanceTokenId = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<KaleoTaskAssignmentInstance> toCacheModel() {
		KaleoTaskAssignmentInstanceCacheModel kaleoTaskAssignmentInstanceCacheModel =
			new KaleoTaskAssignmentInstanceCacheModel();

		kaleoTaskAssignmentInstanceCacheModel.kaleoTaskAssignmentInstanceId = getKaleoTaskAssignmentInstanceId();

		kaleoTaskAssignmentInstanceCacheModel.groupId = getGroupId();

		kaleoTaskAssignmentInstanceCacheModel.companyId = getCompanyId();

		kaleoTaskAssignmentInstanceCacheModel.userId = getUserId();

		kaleoTaskAssignmentInstanceCacheModel.userName = getUserName();

		String userName = kaleoTaskAssignmentInstanceCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			kaleoTaskAssignmentInstanceCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			kaleoTaskAssignmentInstanceCacheModel.createDate = createDate.getTime();
		}
		else {
			kaleoTaskAssignmentInstanceCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			kaleoTaskAssignmentInstanceCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			kaleoTaskAssignmentInstanceCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		kaleoTaskAssignmentInstanceCacheModel.kaleoDefinitionId = getKaleoDefinitionId();

		kaleoTaskAssignmentInstanceCacheModel.kaleoInstanceId = getKaleoInstanceId();

		kaleoTaskAssignmentInstanceCacheModel.kaleoInstanceTokenId = getKaleoInstanceTokenId();

		kaleoTaskAssignmentInstanceCacheModel.kaleoTaskInstanceTokenId = getKaleoTaskInstanceTokenId();

		kaleoTaskAssignmentInstanceCacheModel.kaleoTaskId = getKaleoTaskId();

		kaleoTaskAssignmentInstanceCacheModel.kaleoTaskName = getKaleoTaskName();

		String kaleoTaskName = kaleoTaskAssignmentInstanceCacheModel.kaleoTaskName;

		if ((kaleoTaskName != null) && (kaleoTaskName.length() == 0)) {
			kaleoTaskAssignmentInstanceCacheModel.kaleoTaskName = null;
		}

		kaleoTaskAssignmentInstanceCacheModel.assigneeClassName = getAssigneeClassName();

		String assigneeClassName = kaleoTaskAssignmentInstanceCacheModel.assigneeClassName;

		if ((assigneeClassName != null) && (assigneeClassName.length() == 0)) {
			kaleoTaskAssignmentInstanceCacheModel.assigneeClassName = null;
		}

		kaleoTaskAssignmentInstanceCacheModel.assigneeClassPK = getAssigneeClassPK();

		kaleoTaskAssignmentInstanceCacheModel.completed = getCompleted();

		Date completionDate = getCompletionDate();

		if (completionDate != null) {
			kaleoTaskAssignmentInstanceCacheModel.completionDate = completionDate.getTime();
		}
		else {
			kaleoTaskAssignmentInstanceCacheModel.completionDate = Long.MIN_VALUE;
		}

		return kaleoTaskAssignmentInstanceCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(35);

		sb.append("{kaleoTaskAssignmentInstanceId=");
		sb.append(getKaleoTaskAssignmentInstanceId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", kaleoDefinitionId=");
		sb.append(getKaleoDefinitionId());
		sb.append(", kaleoInstanceId=");
		sb.append(getKaleoInstanceId());
		sb.append(", kaleoInstanceTokenId=");
		sb.append(getKaleoInstanceTokenId());
		sb.append(", kaleoTaskInstanceTokenId=");
		sb.append(getKaleoTaskInstanceTokenId());
		sb.append(", kaleoTaskId=");
		sb.append(getKaleoTaskId());
		sb.append(", kaleoTaskName=");
		sb.append(getKaleoTaskName());
		sb.append(", assigneeClassName=");
		sb.append(getAssigneeClassName());
		sb.append(", assigneeClassPK=");
		sb.append(getAssigneeClassPK());
		sb.append(", completed=");
		sb.append(getCompleted());
		sb.append(", completionDate=");
		sb.append(getCompletionDate());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(55);

		sb.append("<model><model-name>");
		sb.append(
			"com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>kaleoTaskAssignmentInstanceId</column-name><column-value><![CDATA[");
		sb.append(getKaleoTaskAssignmentInstanceId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>kaleoDefinitionId</column-name><column-value><![CDATA[");
		sb.append(getKaleoDefinitionId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>kaleoInstanceId</column-name><column-value><![CDATA[");
		sb.append(getKaleoInstanceId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>kaleoInstanceTokenId</column-name><column-value><![CDATA[");
		sb.append(getKaleoInstanceTokenId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>kaleoTaskInstanceTokenId</column-name><column-value><![CDATA[");
		sb.append(getKaleoTaskInstanceTokenId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>kaleoTaskId</column-name><column-value><![CDATA[");
		sb.append(getKaleoTaskId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>kaleoTaskName</column-name><column-value><![CDATA[");
		sb.append(getKaleoTaskName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>assigneeClassName</column-name><column-value><![CDATA[");
		sb.append(getAssigneeClassName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>assigneeClassPK</column-name><column-value><![CDATA[");
		sb.append(getAssigneeClassPK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>completed</column-name><column-value><![CDATA[");
		sb.append(getCompleted());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>completionDate</column-name><column-value><![CDATA[");
		sb.append(getCompletionDate());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = KaleoTaskAssignmentInstance.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			KaleoTaskAssignmentInstance.class
		};
	private long _kaleoTaskAssignmentInstanceId;
	private long _groupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _kaleoDefinitionId;
	private long _originalKaleoDefinitionId;
	private boolean _setOriginalKaleoDefinitionId;
	private long _kaleoInstanceId;
	private long _originalKaleoInstanceId;
	private boolean _setOriginalKaleoInstanceId;
	private long _kaleoInstanceTokenId;
	private long _kaleoTaskInstanceTokenId;
	private long _originalKaleoTaskInstanceTokenId;
	private boolean _setOriginalKaleoTaskInstanceTokenId;
	private long _kaleoTaskId;
	private String _kaleoTaskName;
	private String _assigneeClassName;
	private long _assigneeClassPK;
	private boolean _completed;
	private Date _completionDate;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private KaleoTaskAssignmentInstance _escapedModelProxy;
}