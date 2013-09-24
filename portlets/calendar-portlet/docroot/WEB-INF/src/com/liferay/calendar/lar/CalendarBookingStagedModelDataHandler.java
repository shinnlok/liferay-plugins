/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.calendar.lar;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.model.CalendarBookingConstants;
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.service.ServiceContext;

import java.util.Map;

/**
 * @author Andrea Di Giorgi
 * @author Daniel Kocsis
 */
public class CalendarBookingStagedModelDataHandler
	extends BaseStagedModelDataHandler<CalendarBooking> {

	public static final String[] CLASS_NAMES =
		{CalendarBooking.class.getName()};

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException, SystemException {

		CalendarBooking calendarBooking =
			CalendarBookingLocalServiceUtil.
				fetchCalendarBookingByUuidAndGroupId(uuid, groupId);

		if (calendarBooking != null) {
			CalendarBookingLocalServiceUtil.deleteCalendarBooking(
				calendarBooking);
		}
	}

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	public String getDisplayName(CalendarBooking calendarBooking) {
		return calendarBooking.getTitleCurrentValue();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			CalendarBooking calendarBooking)
		throws Exception {

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, calendarBooking.getCalendar());

		if (!calendarBooking.isMasterBooking()) {
			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, calendarBooking.getParentCalendarBooking());
		}

		Element calendarBookingElement =
			portletDataContext.getExportDataElement(calendarBooking);

		portletDataContext.addClassedModel(
			calendarBookingElement,
			ExportImportPathUtil.getModelPath(calendarBooking),
			calendarBooking);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			CalendarBooking calendarBooking)
		throws Exception {

		long userId = portletDataContext.getUserId(
			calendarBooking.getUserUuid());

		String calendarPath = ExportImportPathUtil.getModelPath(
			portletDataContext, Calendar.class.getName(),
			calendarBooking.getCalendarId());

		Calendar calendar = (Calendar)portletDataContext.getZipEntryAsObject(
			calendarPath);

		StagedModelDataHandlerUtil.importStagedModel(
			portletDataContext, calendar);

		Map<Long, Long> calendarIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				Calendar.class);

		long calendarId = MapUtil.getLong(
			calendarIds, calendarBooking.getCalendarId(),
			calendarBooking.getCalendarId());

		long parentCalendarBookingId =
			CalendarBookingConstants.PARENT_CALENDAR_BOOKING_ID_DEFAULT;

		if (!calendarBooking.isMasterBooking()) {
			String parentCalendarBookingPath =
				ExportImportPathUtil.getModelPath(
					portletDataContext, CalendarBooking.class.getName(),
					calendarBooking.getParentCalendarBookingId());

			CalendarBooking parentCalendarBooking =
				(CalendarBooking)portletDataContext.getZipEntryAsObject(
					parentCalendarBookingPath);

			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, parentCalendarBooking);

			Map<Long, Long> calendarBookingIds =
				(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
					CalendarBooking.class);

			parentCalendarBookingId = MapUtil.getLong(
				calendarBookingIds,
				calendarBooking.getParentCalendarBookingId(),
				calendarBooking.getParentCalendarBookingId());
		}

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			calendarBooking);

		CalendarBooking importedCalendarBooking = null;

		if (portletDataContext.isDataStrategyMirror()) {
			CalendarBooking existingCalendarBooking =
				CalendarBookingLocalServiceUtil.
					fetchCalendarBookingByUuidAndGroupId(
						calendarBooking.getUuid(),
						portletDataContext.getScopeGroupId());

			if (existingCalendarBooking == null) {
				serviceContext.setUuid(calendarBooking.getUuid());

				importedCalendarBooking =
					CalendarBookingLocalServiceUtil.addCalendarBooking(
						userId, calendarId, new long[0],
						parentCalendarBookingId, calendarBooking.getTitleMap(),
						calendarBooking.getDescriptionMap(),
						calendarBooking.getLocation(),
						calendarBooking.getStartTime(),
						calendarBooking.getEndTime(),
						calendarBooking.isAllDay(),
						calendarBooking.getRecurrence(),
						calendarBooking.getFirstReminder(),
						calendarBooking.getFirstReminderType(),
						calendarBooking.getSecondReminder(),
						calendarBooking.getSecondReminderType(),
						serviceContext);
			}
			else {
				importedCalendarBooking =
					CalendarBookingLocalServiceUtil.updateCalendarBooking(
						userId, existingCalendarBooking.getCalendarBookingId(),
						calendarId, calendarBooking.getTitleMap(),
						calendarBooking.getDescriptionMap(),
						calendarBooking.getLocation(),
						calendarBooking.getStartTime(),
						calendarBooking.getEndTime(),
						calendarBooking.isAllDay(),
						calendarBooking.getRecurrence(),
						calendarBooking.getFirstReminder(),
						calendarBooking.getFirstReminderType(),
						calendarBooking.getSecondReminder(),
						calendarBooking.getSecondReminderType(),
						calendarBooking.getStatus(), serviceContext);
			}
		}
		else {
			importedCalendarBooking =
				CalendarBookingLocalServiceUtil.addCalendarBooking(
					userId, calendarId, new long[0], parentCalendarBookingId,
					calendarBooking.getTitleMap(),
					calendarBooking.getDescriptionMap(),
					calendarBooking.getLocation(),
					calendarBooking.getStartTime(),
					calendarBooking.getEndTime(), calendarBooking.isAllDay(),
					calendarBooking.getRecurrence(),
					calendarBooking.getFirstReminder(),
					calendarBooking.getFirstReminderType(),
					calendarBooking.getSecondReminder(),
					calendarBooking.getSecondReminderType(), serviceContext);
		}

		portletDataContext.importClassedModel(
			calendarBooking, importedCalendarBooking);
	}

}