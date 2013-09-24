<%--
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
--%>

<%@ page import="com.liferay.portal.util.PortalUtil" %>

<div style="text-align: center;">
	<object data="<%= PortalUtil.getPathContext(request) %>/hello_laszlo.lzx?lzt=swf" height="15" type="application/x-shockwave-flash" width="100%">
		<param name="menu" value="false" />
		<param name="movie" value="<%= PortalUtil.getPathContext(request) %>/hello_laszlo.lzx?lzt=swf" />
		<param name="quality" value="high" />
		<param name="salign" value="LT" />
		<param name="scale" value="noscale" />
	</object>
</div>