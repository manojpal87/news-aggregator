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
import com.example.news.aggregator.dto.guardian.ResponseGuardian;
import com.example.news.aggregator.exception.CustomRunTimeException;
import com.example.news.aggregator.logging.OutboundLog;
import com.example.news.aggregator.security.AppKeyDecrypt;
import com.example.news.aggregator.utils.DateUtils;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;

@Service
public class ContentSearchService {
	
	private final Logger logger = LoggerFactory.getLogger(ContentSearchService.class);

	@Autowired
	private OutboundLog outboundLog;

	@Autowired
	private RestTemplate restTemplate;

	@Getter
	@Value("${guardian.uk.content.search.uri}")
	private String contentSearchUri;

	@Value("${theguardian.content.search.application.key}")
	private String apiKey;

	public ResponseGuardian searchContent(QueryParams queryParams) {

		outboundLog.outBoundRequestStarted(queryParams);

		ResponseEntity<ResponseGuardian> contentData;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(contentSearchUri).append(AppKeyDecrypt.decrypt(apiKey));

			String uri = addFilters(queryParams, builder).toString();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			contentData = restTemplate.exchange(uri, HttpMethod.GET, entity, ResponseGuardian.class);

			outboundLog.outBoundRequestStarted(contentData.getBody());
			
		} catch (Exception e) {
			logger.error("Error occured while calling content search api.");
			throw new CustomRunTimeException("Error occured while calling content search api.");
		}

		return contentData.getStatusCode() == HttpStatus.OK ? contentData.getBody() : ResponseGuardian.builder().build();
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

		if (StringUtils.isNotEmpty(queryParams.getSection())) {
			builder.append("&section=").append(queryParams.getSection());
		}

		if (StringUtils.isNotEmpty(queryParams.getTag())) {
			builder.append("&tag=").append(queryParams.getTag());
		}

		if (queryParams.getFromDate() != null) {
			builder.append("&from-date=").append(DateUtils.format(queryParams.getFromDate()));
		}

		if (queryParams.getToDate() != null) {
			builder.append("&to-date=").append(DateUtils.format(queryParams.getToDate()));
		}

		if (queryParams.getOrderBy() != null) {
			builder.append("&order-by=").append(queryParams.getOrderBy().name());
		}

		return builder;
	}

}
