package com.example.news.aggregator.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.news.aggregator.dto.OrderBy;
import com.example.news.aggregator.dto.QueryParams;
import com.example.news.aggregator.dto.guardian.ContentResponse;
import com.example.news.aggregator.dto.guardian.ResponseGuardian;
import com.example.news.aggregator.logging.OutboundLog;
import com.example.news.aggregator.service.ContentSearchService;

@ExtendWith(MockitoExtension.class)
public class ContentSearchServiceTest {

	@Mock
	private OutboundLog outboundLog;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private ContentSearchService contentSearchService;

	private QueryParams queryParams;

	@BeforeEach
	public void setup() {
		queryParams = QueryParams.builder().orderBy(OrderBy.newest).page(2).section("sports").build();
	}

	@DisplayName("JUnit test for content search method")
	@Test
	public void testSearchContent() {

		ResponseGuardian response = ResponseGuardian.builder()
				.response(ContentResponse.builder()
						.status("success")
						.build())
				.build();

		ResponseEntity<ResponseGuardian> responseEntity = new ResponseEntity<ResponseGuardian>(response, HttpStatus.OK);

		when(restTemplate.exchange(ArgumentMatchers.anyString(), 
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<Class<ResponseGuardian>>any()))
		.thenReturn(responseEntity);

		ResponseGuardian serviceData = contentSearchService.searchContent(queryParams);

		Assertions.assertNotNull(serviceData);
		Assertions.assertEquals(response.getResponse().getStatus(), serviceData.getResponse().getStatus());
	}

}
