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

package com.liferay.sync.engine.session;

import java.net.URL;

import java.nio.charset.Charset;
import java.nio.file.Path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.BasicHttpContext;

/**
 * @author Shinn Lok
 */
public class Session {

	public Session(URL url, String login, String password) {
		_url = url;

		_httpHost = new HttpHost(
			_url.getHost(), _url.getPort(), _url.getProtocol());

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		CredentialsProvider credentialsProvider =
			new BasicCredentialsProvider();

		credentialsProvider.setCredentials(
			new AuthScope(url.getHost(), url.getPort()),
			new UsernamePasswordCredentials(login, password));

		httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

		httpClientBuilder.setMaxConnPerRoute(2);

		_httpClient = httpClientBuilder.build();
	}

	public HttpResponse executeGet(String urlPath) throws Exception {
		HttpGet httpGet = new HttpGet(_url.toString() + urlPath);

		return _httpClient.execute(_httpHost, httpGet, _getBasicHttpContext());
	}

	public <T> T executeGet(
			String urlPath, ResponseHandler<? extends T> responseHandler)
		throws Exception {

		HttpGet httpGet = new HttpGet(_url.toString() + urlPath);

		return _httpClient.execute(
			_httpHost, httpGet, responseHandler, _getBasicHttpContext());
	}

	public HttpResponse executePost(
		String urlPath, Map<String, Object> parameters)
		throws Exception {

		HttpPost httpPost = new HttpPost(_url.toString() + urlPath);

		_buildHttpPostBody(httpPost, parameters);

		return _httpClient.execute(_httpHost, httpPost, _getBasicHttpContext());
	}

	public <T> T executePost(
		String urlPath, Map<String, Object> parameters,
		ResponseHandler<? extends T> responseHandler)
		throws Exception {

		HttpPost httpPost = new HttpPost(_url.toString() + urlPath);

		_buildHttpPostBody(httpPost, parameters);

		return _httpClient.execute(
			_httpHost, httpPost, responseHandler, _getBasicHttpContext());
	}

	private void _buildHttpPostBody(
		HttpPost httpPost, Map<String, Object> parameters)
		throws Exception {

		Path filePath = (Path)parameters.remove("filePath");

		MultipartEntityBuilder multipartEntityBuilder =
			_getMultipartEntityBuilder(parameters);

		if (filePath != null) {
			multipartEntityBuilder.addPart(
				"file",
				_getFileBody(
					filePath, (String)parameters.get("mimeType"),
					(String)parameters.get("title")));
		}

		httpPost.setEntity(multipartEntityBuilder.build());
	}

	private BasicAuthCache _getBasicAuthCache() {
		BasicAuthCache basicAuthCache = new BasicAuthCache();

		BasicScheme basicScheme = new BasicScheme();

		basicAuthCache.put(_httpHost, basicScheme);

		return basicAuthCache;
	}

	private BasicHttpContext _getBasicHttpContext() {
		BasicHttpContext basicHttpContext = new BasicHttpContext();

		basicHttpContext.setAttribute(
			HttpClientContext.AUTH_CACHE, _getBasicAuthCache());

		return basicHttpContext;
	}

	private ContentBody _getFileBody(
			Path filePath, String mimeType, String fileName)
		throws Exception {

		return new FileBody(
			filePath.toFile(), ContentType.create(mimeType), fileName);
	}

	private MultipartEntityBuilder _getMultipartEntityBuilder(
		Map<String, Object> parameters) {

		MultipartEntityBuilder multipartEntityBuilder =
			MultipartEntityBuilder.create();

		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (_ignoredParameterKeys.contains(entry.getKey())) {
				continue;
			}

			multipartEntityBuilder.addPart(
				entry.getKey(), _getStringBody(entry.getValue()));
		}

		return multipartEntityBuilder;
	}

	private StringBody _getStringBody(Object value) {
		return new StringBody(
			String.valueOf(value),
			ContentType.create(MediaType.TEXT_PLAIN, Charset.defaultCharset()));
	}

	private final CloseableHttpClient _httpClient;
	private HttpHost _httpHost;
	private Set<String> _ignoredParameterKeys = new HashSet<String>(
		Arrays.asList("filePath", "syncFile"));
	private URL _url;

}