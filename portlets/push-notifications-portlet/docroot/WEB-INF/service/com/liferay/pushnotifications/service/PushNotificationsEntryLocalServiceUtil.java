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

package com.liferay.pushnotifications.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableLocalService;

/**
 * Provides the local service utility for PushNotificationsEntry. This utility wraps
 * {@link com.liferay.pushnotifications.service.impl.PushNotificationsEntryLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Bruno Farache
 * @see PushNotificationsEntryLocalService
 * @see com.liferay.pushnotifications.service.base.PushNotificationsEntryLocalServiceBaseImpl
 * @see com.liferay.pushnotifications.service.impl.PushNotificationsEntryLocalServiceImpl
 * @generated
 */
@ProviderType
public class PushNotificationsEntryLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.pushnotifications.service.impl.PushNotificationsEntryLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the push notifications entry to the database. Also notifies the appropriate model listeners.
	*
	* @param pushNotificationsEntry the push notifications entry
	* @return the push notifications entry that was added
	*/
	public static com.liferay.pushnotifications.model.PushNotificationsEntry addPushNotificationsEntry(
		com.liferay.pushnotifications.model.PushNotificationsEntry pushNotificationsEntry) {
		return getService().addPushNotificationsEntry(pushNotificationsEntry);
	}

	public static com.liferay.pushnotifications.model.PushNotificationsEntry addPushNotificationsEntry(
		long userId, long parentPushNotificationsEntryId,
		com.liferay.portal.kernel.json.JSONObject payloadJSONObject) {
		return getService()
				   .addPushNotificationsEntry(userId,
			parentPushNotificationsEntryId, payloadJSONObject);
	}

	/**
	* Creates a new push notifications entry with the primary key. Does not add the push notifications entry to the database.
	*
	* @param pushNotificationsEntryId the primary key for the new push notifications entry
	* @return the new push notifications entry
	*/
	public static com.liferay.pushnotifications.model.PushNotificationsEntry createPushNotificationsEntry(
		long pushNotificationsEntryId) {
		return getService()
				   .createPushNotificationsEntry(pushNotificationsEntryId);
	}

	/**
	* @throws PortalException
	*/
	public static com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deletePersistedModel(persistedModel);
	}

	/**
	* Deletes the push notifications entry from the database. Also notifies the appropriate model listeners.
	*
	* @param pushNotificationsEntry the push notifications entry
	* @return the push notifications entry that was removed
	*/
	public static com.liferay.pushnotifications.model.PushNotificationsEntry deletePushNotificationsEntry(
		com.liferay.pushnotifications.model.PushNotificationsEntry pushNotificationsEntry) {
		return getService().deletePushNotificationsEntry(pushNotificationsEntry);
	}

	/**
	* Deletes the push notifications entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param pushNotificationsEntryId the primary key of the push notifications entry
	* @return the push notifications entry that was removed
	* @throws PortalException if a push notifications entry with the primary key could not be found
	*/
	public static com.liferay.pushnotifications.model.PushNotificationsEntry deletePushNotificationsEntry(
		long pushNotificationsEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .deletePushNotificationsEntry(pushNotificationsEntryId);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.pushnotifications.model.impl.PushNotificationsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.pushnotifications.model.impl.PushNotificationsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows that match the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.pushnotifications.model.PushNotificationsEntry fetchPushNotificationsEntry(
		long pushNotificationsEntryId) {
		return getService().fetchPushNotificationsEntry(pushNotificationsEntryId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return getService().getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	public static com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getPersistedModel(primaryKeyObj);
	}

	public static java.util.List<com.liferay.pushnotifications.model.PushNotificationsEntry> getPushNotificationsEntries(
		long parentPushNotificationsEntryId, long lastAccessTime, int start,
		int end) {
		return getService()
				   .getPushNotificationsEntries(parentPushNotificationsEntryId,
			lastAccessTime, start, end);
	}

	/**
	* Returns a range of all the push notifications entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.pushnotifications.model.impl.PushNotificationsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of push notifications entries
	* @param end the upper bound of the range of push notifications entries (not inclusive)
	* @return the range of push notifications entries
	*/
	public static java.util.List<com.liferay.pushnotifications.model.PushNotificationsEntry> getPushNotificationsEntries(
		int start, int end) {
		return getService().getPushNotificationsEntries(start, end);
	}

	/**
	* Returns the number of push notifications entries.
	*
	* @return the number of push notifications entries
	*/
	public static int getPushNotificationsEntriesCount() {
		return getService().getPushNotificationsEntriesCount();
	}

	/**
	* Returns the push notifications entry with the primary key.
	*
	* @param pushNotificationsEntryId the primary key of the push notifications entry
	* @return the push notifications entry
	* @throws PortalException if a push notifications entry with the primary key could not be found
	*/
	public static com.liferay.pushnotifications.model.PushNotificationsEntry getPushNotificationsEntry(
		long pushNotificationsEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getPushNotificationsEntry(pushNotificationsEntryId);
	}

	public static java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return getService().invokeMethod(name, parameterTypes, arguments);
	}

	public static void sendPushNotification(
		com.liferay.portal.kernel.json.JSONObject jsonObject, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().sendPushNotification(jsonObject, start, end);
	}

	public static void sendPushNotification(long toUserId,
		com.liferay.portal.kernel.json.JSONObject jsonObject, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().sendPushNotification(toUserId, jsonObject, start, end);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	/**
	* Updates the push notifications entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param pushNotificationsEntry the push notifications entry
	* @return the push notifications entry that was updated
	*/
	public static com.liferay.pushnotifications.model.PushNotificationsEntry updatePushNotificationsEntry(
		com.liferay.pushnotifications.model.PushNotificationsEntry pushNotificationsEntry) {
		return getService().updatePushNotificationsEntry(pushNotificationsEntry);
	}

	public static void clearService() {
		_service = null;
	}

	public static PushNotificationsEntryLocalService getService() {
		if (_service == null) {
			InvokableLocalService invokableLocalService = (InvokableLocalService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					PushNotificationsEntryLocalService.class.getName());

			if (invokableLocalService instanceof PushNotificationsEntryLocalService) {
				_service = (PushNotificationsEntryLocalService)invokableLocalService;
			}
			else {
				_service = new PushNotificationsEntryLocalServiceClp(invokableLocalService);
			}

			ReferenceRegistry.registerReference(PushNotificationsEntryLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	@Deprecated
	public void setService(PushNotificationsEntryLocalService service) {
	}

	private static PushNotificationsEntryLocalService _service;
}