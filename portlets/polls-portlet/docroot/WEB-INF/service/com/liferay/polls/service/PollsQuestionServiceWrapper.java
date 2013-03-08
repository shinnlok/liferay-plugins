/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.polls.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link PollsQuestionService}.
 * </p>
 *
 * @author    Juan Fern√°ndez
 * @see       PollsQuestionService
 * @generated
 */
public class PollsQuestionServiceWrapper implements PollsQuestionService,
	ServiceWrapper<PollsQuestionService> {
	public PollsQuestionServiceWrapper(
		PollsQuestionService pollsQuestionService) {
		_pollsQuestionService = pollsQuestionService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _pollsQuestionService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_pollsQuestionService.setBeanIdentifier(beanIdentifier);
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _pollsQuestionService.invokeMethod(name, parameterTypes,
			arguments);
	}

	public com.liferay.polls.model.PollsQuestion addPollsQuestion(
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		int expirationDateMonth, int expirationDateDay, int expirationDateYear,
		int expirationDateHour, int expirationDateMinute, boolean neverExpire,
		java.util.List<com.liferay.polls.model.PollsChoice> pollsChoices,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _pollsQuestionService.addPollsQuestion(titleMap, descriptionMap,
			expirationDateMonth, expirationDateDay, expirationDateYear,
			expirationDateHour, expirationDateMinute, neverExpire,
			pollsChoices, serviceContext);
	}

	public com.liferay.polls.model.PollsQuestion deletePollsQuestion(
		long pollsQuestionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _pollsQuestionService.deletePollsQuestion(pollsQuestionId);
	}

	public com.liferay.polls.model.PollsQuestion getPollsQuestion(
		long pollsQuestionId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _pollsQuestionService.getPollsQuestion(pollsQuestionId);
	}

	public com.liferay.polls.model.PollsQuestion updatePollsQuestion(
		long pollsQuestionId,
		java.util.Map<java.util.Locale, java.lang.String> titleMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		int expirationDateMonth, int expirationDateDay, int expirationDateYear,
		int expirationDateHour, int expirationDateMinute, boolean neverExpire,
		java.util.List<com.liferay.polls.model.PollsChoice> pollsChoices,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _pollsQuestionService.updatePollsQuestion(pollsQuestionId,
			titleMap, descriptionMap, expirationDateMonth, expirationDateDay,
			expirationDateYear, expirationDateHour, expirationDateMinute,
			neverExpire, pollsChoices, serviceContext);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public PollsQuestionService getWrappedPollsQuestionService() {
		return _pollsQuestionService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedPollsQuestionService(
		PollsQuestionService pollsQuestionService) {
		_pollsQuestionService = pollsQuestionService;
	}

	public PollsQuestionService getWrappedService() {
		return _pollsQuestionService;
	}

	public void setWrappedService(PollsQuestionService pollsQuestionService) {
		_pollsQuestionService = pollsQuestionService;
	}

	private PollsQuestionService _pollsQuestionService;
}