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

package com.liferay.arquillian.container.protocols;

import com.liferay.arquillian.container.adapters.ExecutionListenerAdapter;

import com.liferay.arquillian.container.enrichers.LiferayTestEnricher;
import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * @author Carlos Sierra Andrés
 */
public class LiferayProtocolRemoteExtension implements RemoteLoadableExtension {

	@Override
	public void register(ExtensionBuilder builder) {
		if (Validate.classExists("org.osgi.framework.Bundle")) {
			builder.service(TestEnricher.class, LiferayTestEnricher.class);
		}

		builder.observer(ExecutionListenerAdapter.class);
	}

}