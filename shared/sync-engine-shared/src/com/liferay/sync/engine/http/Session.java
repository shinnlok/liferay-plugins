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

package com.liferay.sync.engine.http;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shinn Lok
 * @author Dennis Ju
 */
public class Session {

	public Session(URL url, String login, String password, boolean ssoEnabled) {
		_url = url;
		_login = login;
		_password = password;
		_ssoEnabled = ssoEnabled;

		_httpHost = new HttpHost(
			_url.getHost(), _url.getPort(), _url.getProtocol());

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		if (ssoEnabled) {
			//TODO
		}
		else {
			CredentialsProvider credentialsProvider =
				new BasicCredentialsProvider();

			credentialsProvider.setCredentials(
				new AuthScope(url.getHost(), url.getPort()),
				new UsernamePasswordCredentials(login, password));

			httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		}

		httpClientBuilder.setMaxConnPerRoute(2);

		_httpClient = httpClientBuilder.build();

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		_futureRequestExecutionService = new FutureRequestExecutionService(
			_httpClient, executorService);
	}

	public HttpResponse executeGet(String urlPath) throws IOException {
		HttpGet httpGet = new HttpGet(_url.toString() + urlPath);

		return _httpClient.execute(httpGet, _getHttpContext());
	}

	public <T> T executeGet(
			String urlPath, ResponseHandler<? extends T> responseHandler)
		throws IOException {

		HttpGet httpGet = new HttpGet(_url.toString() + urlPath);

		return _httpClient.execute(httpGet, responseHandler, _getHttpContext());
	}

	public <T> HttpRequestFutureTask<T> executeGetAsync(
			String urlPath, ResponseHandler<T> responseHandler)
		throws IOException {

		HttpGet httpGet = new HttpGet(_url.toString() + urlPath);

		return _futureRequestExecutionService.execute(
			httpGet, _getHttpContext(), responseHandler);
	}

	public <T> HttpRequestFutureTask<T> executeGetAsync(
			String urlPath, ResponseHandler<T> responseHandler,
			FutureCallback callback)
		throws IOException {

		HttpGet httpGet = new HttpGet(_url.toString() + urlPath);

		return _futureRequestExecutionService.execute(
			httpGet, _getHttpContext(), responseHandler, callback);
	}

	public HttpResponse executePost(
			String urlPath, Map<String, Object> parameters)
		throws Exception {

		HttpPost httpPost = new HttpPost(_url.toString() + urlPath);

		_buildHttpPostBody(httpPost, parameters);

		return _httpClient.execute(httpPost, _getHttpContext());
	}

	public <T> T executePost(
			String urlPath, Map<String, Object> parameters,
			ResponseHandler<? extends T> responseHandler)
		throws Exception {

		HttpPost httpPost = new HttpPost(_url.toString() + urlPath);

		_buildHttpPostBody(httpPost, parameters);

		return _httpClient.execute(httpPost, responseHandler, _getHttpContext());
	}

	public <T> HttpRequestFutureTask<T> executePostAsync(
			String urlPath, Map<String, Object> parameters,
			ResponseHandler<T> responseHandler)
		throws Exception {

		HttpPost httpPost = new HttpPost(_url.toString() + urlPath);

		_buildHttpPostBody(httpPost, parameters);

		return _futureRequestExecutionService.execute(
			httpPost, _getHttpContext(), responseHandler);
	}

	public <T> HttpRequestFutureTask<T> executePostAsync(
			String urlPath, Map<String, Object> parameters,
			ResponseHandler<T> responseHandler, FutureCallback callback)
		throws Exception {

		HttpPost httpPost = new HttpPost(_url.toString() + urlPath);

		_buildHttpPostBody(httpPost, parameters);

		return _futureRequestExecutionService.execute(
			httpPost, _getHttpContext(), responseHandler, callback);
	}

	private static ContentBody _getFileBody(
			Path filePath, String mimeType, String fileName)
		throws Exception {

		return new FileBody(
			filePath.toFile(), ContentType.create(mimeType), fileName);
	}

	private void _buildHttpPostBody(
			HttpPost httpPost, Map<String, Object> parameters)
		throws Exception {

		Path filePath = (Path)parameters.remove("filePath");
		Path deltaFilePath = (Path)parameters.remove("deltaFilePath");

		MultipartEntityBuilder multipartEntityBuilder =
			_getMultipartEntityBuilder(parameters);

		if (filePath != null) {
			multipartEntityBuilder.addPart(
				"file",
				_getFileBody(
					filePath, (String)parameters.get("mimeType"),
					(String)parameters.get("title")));
		}
		else if (deltaFilePath != null) {
			multipartEntityBuilder.addPart(
				"deltaFile",
				_getFileBody(
					deltaFilePath, (String)parameters.get("mimeType"),
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

	private HttpContext _getHttpContext() throws MalformedURLException {
		HttpContext httpContext = null;

		if (_ssoEnabled) {
			//TODO
		}
		else {
			httpContext = new BasicHttpContext();

			httpContext.setAttribute(
				HttpClientContext.AUTH_CACHE, _getBasicAuthCache());

			return httpContext;
		}

		return httpContext;
	}

	private MultipartEntityBuilder _getMultipartEntityBuilder(
		Map<String, Object> parameters) {

		MultipartEntityBuilder multipartEntityBuilder =
			MultipartEntityBuilder.create();

		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (_ignoredParameterKeys.contains(entry.getKey())) {
				continue;
			}

			multipartEntityBuilder.addTextBody(
				entry.getKey(), String.valueOf(entry.getValue()));
		}

		return multipartEntityBuilder;
	}

	private static Logger _logger = LoggerFactory.getLogger(Session.class);

	private static Set<String> _ignoredParameterKeys = new HashSet<String>(
		Arrays.asList("filePath", "syncFile"));

	private final FutureRequestExecutionService _futureRequestExecutionService;
	private final CloseableHttpClient _httpClient;
	private HttpHost _httpHost;
	private String _login;
	private String _password;
	private boolean _ssoEnabled;
	private URL _url;

}