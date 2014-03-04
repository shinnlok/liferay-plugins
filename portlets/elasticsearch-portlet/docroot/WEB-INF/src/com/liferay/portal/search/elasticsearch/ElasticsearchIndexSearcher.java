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

package com.liferay.portal.search.elasticsearch;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexSearcher;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.HitsImpl;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.facet.ElasticsearchFacetFieldCollector;
import com.liferay.portal.search.elasticsearch.facet.FacetProcessorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @author Michael C. Han
 * @author Milen Dyankov
 */
public class ElasticsearchIndexSearcher extends BaseIndexSearcher {

	@Override
	public Hits search(SearchContext searchContext, Query query) {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		ElasticsearchConnectionManager elasticsearchConnectionManager =
			ElasticsearchConnectionManager.getInstance();

		Client client = elasticsearchConnectionManager.getClient();

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(
			String.valueOf(searchContext.getCompanyId()));

		addFacets(searchRequestBuilder, searchContext);
		addHighlights(searchRequestBuilder, query.getQueryConfig());
		addPagination(
			searchRequestBuilder, searchContext.getStart(),
			searchContext.getEnd());
		addSelectedFields(searchRequestBuilder, query.getQueryConfig());
		addSort(searchRequestBuilder, searchContext.getSorts());

		QueryBuilder queryBuilder = QueryBuilders.queryString(query.toString());

		searchRequestBuilder.setQuery(queryBuilder);

		SearchRequest searchRequest = searchRequestBuilder.request();

		ActionFuture<SearchResponse> future = client.search(searchRequest);

		SearchResponse searchResponse = future.actionGet();

		updateFacetCollectors(searchContext, searchResponse);

		Hits hits = processSearchHits(
			searchResponse.getHits(), query.getQueryConfig());

		hits.setQuery(query);

		TimeValue timeValue = searchResponse.getTook();

		hits.setSearchTime((float)timeValue.getSecondsFrac());

		hits.setStart(stopWatch.getStartTime());

		if (_log.isInfoEnabled()) {
			stopWatch.stop();

			_log.info(
				"Searching " + queryBuilder.toString() + " took " +
					stopWatch.getTime() + " ms with the search engine using " +
						hits.getSearchTime() + " s");
		}

		return hits;
	}

	protected void addFacets(
		SearchRequestBuilder searchRequestBuilder,
		SearchContext searchContext) {

		Map<String, Facet> facetsMap = searchContext.getFacets();

		for (Facet facet : facetsMap.values()) {
			if (facet.isStatic()) {
				continue;
			}

			FacetProcessorUtil.processFacet(searchRequestBuilder, facet);
		}
	}

	protected void addHighlights(
		SearchRequestBuilder searchRequestBuilder, QueryConfig queryConfig) {

		if (!queryConfig.isHighlightEnabled()) {
			return;
		}

		String localizedContentName = DocumentImpl.getLocalizedName(
			queryConfig.getLocale(), Field.CONTENT);

		searchRequestBuilder.addHighlightedField(
			localizedContentName, queryConfig.getHighlightFragmentSize(),
			queryConfig.getHighlightSnippetSize());

		String localizedTitleName = DocumentImpl.getLocalizedName(
			queryConfig.getLocale(), Field.TITLE);

		searchRequestBuilder.addHighlightedField(
			localizedTitleName, queryConfig.getHighlightFragmentSize(),
			queryConfig.getHighlightSnippetSize());
	}

	protected void addPagination(
		SearchRequestBuilder searchRequestBuilder, int start, int end) {

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS)) {
			searchRequestBuilder.setSize(0);
		}
		else {
			searchRequestBuilder.setFrom(start);
			searchRequestBuilder.setSize(end - start);
		}
	}

	protected void addSelectedFields(
		SearchRequestBuilder searchRequestBuilder, QueryConfig queryConfig) {

		String[] selectedFieldNames = queryConfig.getSelectedFieldNames();

		if ((selectedFieldNames == null) || (selectedFieldNames.length == 0)) {
			searchRequestBuilder.addFields("*");
		}
		else {
			searchRequestBuilder.addFields(selectedFieldNames);
		}
	}

	protected void addSort(
		SearchRequestBuilder searchRequestBuilder, Sort[] sorts) {

		if ((sorts == null) || (sorts.length == 0)) {
			return;
		}

		for (Sort sort : sorts) {
			if (sort == null) {
				continue;
			}

			String sortFieldName = sort.getFieldName();

			if (DocumentImpl.isSortableTextField(sortFieldName)) {
				sortFieldName = DocumentImpl.getSortableFieldName(
					sortFieldName);
			}

			SortOrder order = SortOrder.ASC;

			if (Validator.isNull(sortFieldName) ||
				!sortFieldName.endsWith("sortable")) {

				order = SortOrder.DESC;

				sortFieldName = "_score";
			}

			if (sort.isReverse()) {
				order = SortOrder.DESC;
			}

			searchRequestBuilder.addSort(sortFieldName, order);
		}
	}

	protected String getSnippet(SearchHit hit, QueryConfig queryConfig) {
		String snippet = null;

		Map<String, HighlightField> highlightFields = hit.getHighlightFields();

		if ((highlightFields == null) || highlightFields.isEmpty()) {
			return StringPool.BLANK;
		}

		String localizedContentName = DocumentImpl.getLocalizedName(
			queryConfig.getLocale(), Field.CONTENT);

		if (localizedContentName != null) {
			HighlightField highlightField = highlightFields.get(
				localizedContentName);

			if (highlightField != null) {
				Text[] texts = highlightField.fragments();

				if ((texts != null) && (texts.length > 0)) {
					snippet = texts[0].string();
				}
			}
		}

		String localizedTitleName = DocumentImpl.getLocalizedName(
			queryConfig.getLocale(), Field.TITLE);

		if ((snippet == null) && (localizedTitleName != null)) {
			HighlightField highlightField = highlightFields.get(
				localizedContentName);

			if (highlightField != null) {
				Text[] texts = highlightField.fragments();

				if ((texts != null) && (texts.length > 0)) {
					snippet = texts[0].string();
				}
			}
		}

		if (snippet == null) {
			snippet = StringPool.BLANK;
		}

		return snippet;
	}

	protected Document processSearchHit(SearchHit hit) {
		Document document = new DocumentImpl();

		Map<String, SearchHitField> searchHitFields = hit.getFields();

		for (Map.Entry<String, SearchHitField> entry :
				searchHitFields.entrySet()) {

			SearchHitField searchHitField = entry.getValue();

			Collection<Object> fieldValues = searchHitField.getValues();

			Field field = new Field(
				entry.getKey(),
				ArrayUtil.toStringArray(
					fieldValues.toArray(new Object[fieldValues.size()])));

			document.add(field);
		}

		return document;
	}

	protected Hits processSearchHits(
		SearchHits searchHits, QueryConfig queryConfig) {

		Hits hits = new HitsImpl();

		List<Document> documents = new ArrayList<Document>();
		Set<String> queryTerms = new HashSet<String>();
		List<Float> scores = new ArrayList<Float>();
		List<String> snippets = new ArrayList<String>();

		if (searchHits.totalHits() > 0) {
			SearchHit[] searchHitsArray = searchHits.getHits();

			for (SearchHit searchHit : searchHitsArray) {
				Document document = processSearchHit(searchHit);

				documents.add(document);

				String snippet = getSnippet(searchHit, queryConfig);

				queryTerms.add(snippet);

				scores.add(searchHit.getScore());

				snippets.add(snippet);
			}
		}

		hits.setDocs(documents.toArray(new Document[documents.size()]));
		hits.setLength((int)searchHits.getTotalHits());
		hits.setQueryTerms(queryTerms.toArray(new String[queryTerms.size()]));
		hits.setScores(scores.toArray(new Float[scores.size()]));
		hits.setSnippets(snippets.toArray(new String[snippets.size()]));

		return hits;
	}

	protected void updateFacetCollectors(
		SearchContext searchContext, SearchResponse searchResponse) {

		Map<String, Facet> facetsMap = searchContext.getFacets();

		for (Facet facet : facetsMap.values()) {
			if (facet.isStatic()) {
				continue;
			}

			Facets facets = searchResponse.getFacets();

			org.elasticsearch.search.facet.Facet elasticsearchFacet =
				facets.facet(facet.getFieldName());

			FacetCollector facetCollector =
				new ElasticsearchFacetFieldCollector(elasticsearchFacet);

			facet.setFacetCollector(facetCollector);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		ElasticsearchIndexSearcher.class);

}