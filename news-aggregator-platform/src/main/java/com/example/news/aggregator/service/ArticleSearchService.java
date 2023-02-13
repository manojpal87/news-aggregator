package com.example.news.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.news.aggregator.dto.QueryParams;
import com.example.news.aggregator.dto.newyork.ResponseNY;
import com.example.news.aggregator.exception.CustomRunTimeException;
import com.example.news.aggregator.logging.OutboundLog;
import com.example.news.aggregator.security.AppKeyDecrypt;
import com.example.news.aggregator.utils.DateUtils;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;

@Service
public class ArticleSearchService {
	
	private final Logger logger = LoggerFactory.getLogger(ArticleSearchService.class);

	@Autowired
	private OutboundLog outboundLog;

	@Autowired
	private RestTemplate restTemplate;

	@Getter
	@Value("${newyorktimes.us.article.search.uri}")
	private String articleSearchUri;

	@Value("${newyorktimes.article.search.application.key}")
	private String apiKey;

	public ResponseNY getNewsArticles(QueryParams queryParams) {

		outboundLog.outBoundRequestStarted(queryParams);

		ResponseEntity<ResponseNY> articleData;

		try {
			StringBuilder builder = new StringBuilder();
			builder.append(articleSearchUri).append(AppKeyDecrypt.decrypt(apiKey));

			String uri = addFilters(queryParams, builder).toString();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			articleData = restTemplate.exchange(uri, HttpMethod.GET, entity, ResponseNY.class);

			outboundLog.outBoundRequestStarted(articleData.getBody());
			
		} catch (Exception e) {
			logger.error("Error occured while calling newyork article search api.");
			throw new CustomRunTimeException("Error occured while calling newyork article search api.");
		}

		return articleData.getStatusCode() == HttpStatus.OK ? articleData.getBody() : ResponseNY.builder().build();

	}

	/**
	 * Adding filter values as query parameters if values are not null.
	 * 
	 * @param queryParams
	 * @param builder
	 * @return
	 */
	private StringBuilder addFilters(QueryParams queryParams, StringBuilder builder) {

		builder.append("&page=").append(queryParams.getPage());

		if (StringUtils.isNotEmpty(queryParams.getQuery())) {
			builder.append("&q=").append(queryParams.getQuery());
		}

		if (queryParams.getFromDate() != null) {
			builder.append("&begin_date=").append(DateUtils.format(queryParams.getFromDate()));
		}

		if (queryParams.getToDate() != null) {
			builder.append("&end_date=").append(DateUtils.format(queryParams.getToDate()));
		}

		if (queryParams.getOrderBy() != null) {
			builder.append("&sort=").append(queryParams.getOrderBy().name());
		}

		return builder;
	}

}
