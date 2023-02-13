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
import com.example.news.aggregator.dto.newyork.ResponseNY;
import com.example.news.aggregator.logging.OutboundLog;
import com.example.news.aggregator.service.ArticleSearchService;

@ExtendWith(MockitoExtension.class)
public class ArticleSearchServiceTest {

	@Mock
	private OutboundLog outboundLog;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private ArticleSearchService articleSearchService;

	private QueryParams queryParams;

	@BeforeEach
	public void setup() {
		queryParams = QueryParams.builder()
				.orderBy(OrderBy.newest)
				.page(2)
				.section("sports")
				.build();
	}

	@DisplayName("JUnit test for article search method")
	@Test
	public void testGetNewsArticles() {

		ResponseNY response = ResponseNY.builder().status("success").build();

		ResponseEntity<ResponseNY> responseEntity = new ResponseEntity<ResponseNY>(response, HttpStatus.OK);

		when(restTemplate.exchange(ArgumentMatchers.anyString(), 
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(), 
				ArgumentMatchers.<Class<ResponseNY>>any()))
		.thenReturn(responseEntity);

		ResponseNY serviceData = articleSearchService.getNewsArticles(queryParams);

		Assertions.assertNotNull(serviceData);
		Assertions.assertEquals(response.getStatus(), serviceData.getStatus());
	}

}
