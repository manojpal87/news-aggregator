package com.example.news.aggregator.controller;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.news.aggregator.dto.NewAggregatorResponse;
import com.example.news.aggregator.dto.OrderBy;
import com.example.news.aggregator.dto.QueryParams;
import com.example.news.aggregator.dto.guardian.ResponseGuardian;
import com.example.news.aggregator.dto.newyork.ResponseNY;
import com.example.news.aggregator.exception.CustomRunTimeException;
import com.example.news.aggregator.service.ArticleSearchService;
import com.example.news.aggregator.service.ContentSearchService;
import com.example.news.aggregator.utils.DateUtils;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * Rest end to used to aggregate news from New York times US and Guardian UK.
 * 
 * @author Manoj
 *
 */
@RestController
@RequestMapping("/news-aggregator/v1/")
@Validated
public class NewsAggregatorController {
	
	private final Logger logger = LoggerFactory.getLogger(NewsAggregatorController.class);

	@Autowired
	private Clock clock;
	
	@Autowired
	private ArticleSearchService articleSearchService;

	@Autowired
	private ContentSearchService contentSearchService;

	@GetMapping(value = "/api/news/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@CircuitBreaker(name = "fallbackMethod", fallbackMethod = "contentAggregateSearchfallback")
	public ResponseEntity<NewAggregatorResponse> contentAggregateSearch(
												@RequestParam(name = "queryParam", required = false) String query,
												@RequestParam(required = false) String section, 
												@RequestParam(required = false) String tag,
												@RequestParam(required = false) String fromDate,
												@RequestParam(required = false) String toDate,
												@RequestParam(required = false, defaultValue = "1") String page,
												@RequestParam(required = false) String orderBy) throws Exception {

		// validate dates
		validateDates(fromDate, toDate);
		
		Instant startedTime = clock.instant();

		// call to guardian content service
		ResponseGuardian contentServiceResponse = contentSearchService.searchContent(QueryParams.builder().query(query)
				.section(section).tag(tag).fromDate(fromDate != null ? DateUtils.parse(fromDate) : null)
				.toDate(toDate != null ? DateUtils.parse(toDate) : null).page(Integer.parseInt(page))
				.orderBy(orderBy != null ? OrderBy.valueOf(orderBy) : null).build());

		logger.info("contentServiceResponse : " + contentServiceResponse);

		// call to new york times article service
		ResponseNY articleServiceResponse = articleSearchService.getNewsArticles(QueryParams.builder().query(query)
				.section(section).tag(tag).fromDate(fromDate != null ? DateUtils.parse(fromDate) : null)
				.toDate(toDate != null ? DateUtils.parse(toDate) : null).page(Integer.parseInt(page))
				.orderBy(orderBy != null ? OrderBy.valueOf(orderBy) : null).build());

		logger.info("articleServiceResponse : " + articleServiceResponse);
		
        // aggregating news data
		NewAggregatorResponse response = aggregateNewsData(contentServiceResponse, articleServiceResponse);
		response.setLatency(Duration.between(startedTime, clock.instant()).getNano());
		response.setPageNo(Integer.parseInt(page));
		
		StringBuilder sb = new StringBuilder();
		sb.append("query = ").append(query)
		.append(", section = ").append(section)
		.append(", tag = ").append(tag)
		.append(", fromDate = ").append(fromDate)
		.append(", toDate = ").append(toDate)
		.append(", page = ").append(page)
		.append(", orderBy = ").append(orderBy);
		 
		response.setSearchKeyword(sb.toString());
		
		logger.info("Aggrgated final news response - :" + response);
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Method will be used to aggregate the news and form the required
	 * information.<Br>
	 * Remove if there are any duplicates data.
	 * 
	 * @param contentServiceResponse
	 * @param articleServiceResponse
	 * @return
	 */
	private NewAggregatorResponse aggregateNewsData(ResponseGuardian contentServiceResponse, ResponseNY articleServiceResponse) {

		Set<String> articleUrl = new HashSet<>();
		Set<String> headline = new HashSet<>();

		Stream.of(contentServiceResponse.getResponse().getResults())
		.forEach(content -> {
			headline.add(content.getWebTitle());
			articleUrl.add(content.getWebUrl());
		});

		Stream.of(articleServiceResponse.getResponse().getDocs())
		.forEach(article -> {
			if (article.getHeadline() != null) {
				headline.add(article.getHeadline().getMain());
			}
			articleUrl.add(article.getWeb_url());
		});

		NewAggregatorResponse response = NewAggregatorResponse.builder()
				.newsWebsite(Arrays.asList(articleSearchService.getArticleSearchUri(), contentSearchService.getContentSearchUri()))
				.totalPages(contentServiceResponse.getResponse().getPages() + articleServiceResponse.getResponse().getMeta().getHits())
				.articleUrl( articleUrl.stream().collect(Collectors.toList()))
				.headline(headline.stream().collect(Collectors.toList()))
				.build();
		
		logger.info("Prepared response - :" + response);

		return response;
	}

	/**
	 * Basic date validation.
	 * 
	 * @param fromDate
	 * @param toDate
	 */
	private void validateDates(String fromDate, String toDate) {

		Date currentDate = new Date();

		if (fromDate != null && DateUtils.parse(fromDate).compareTo(currentDate) > 1) {
			throw new CustomRunTimeException("Begin date cannot be future dated.");
		}

		if (toDate != null && DateUtils.parse(toDate).compareTo(currentDate) > 1) {
			throw new CustomRunTimeException("End date cannot be future dated.");
		}

		if (fromDate != null && toDate != null && DateUtils.parse(fromDate).compareTo(DateUtils.parse(toDate)) > 1) {

			throw new CustomRunTimeException("Begin date cannot be greater than end date.");
		}

	}

	/**
	 * Fallback method
	 * 
	 * @return
	 */
	public ResponseEntity<NewAggregatorResponse> contentAggregateSearchfallback(final Throwable throwable) {

		NewAggregatorResponse aggregatorResponse = NewAggregatorResponse.builder()
				.newsWebsite(Arrays.asList(articleSearchService.getArticleSearchUri(), contentSearchService.getContentSearchUri()))
				.totalPages(10)
				.searchKeyword("world news")
				.articleUrl(Arrays.asList(
						"https://www.moneycontrol.com/news/world/looting-unrest-make-rescue-efforts-harder-as-turkey-quake-toll-nears-26000-10058571.html",
						"https://timesofindia.indiatimes.com/world/us/radar-anomaly-detected-but-no-object-found-over-montana-us-military/articleshow/97838720.cms",
						"https://timesofindia.indiatimes.com/travel/travel-news/india-drops-air-suvidha-requirement-for-6-countries-2-percent-random-testing-to-continue/articleshow/97819218.cms"))
				.headline(Arrays.asList("Turkey arrests building contractors 6 days after quakes",
						"US warjet shoots down ‘unidentified object’ above Canada: Here’s what we know",
						"Lightning strikes Brazil's Christ the Redeemer statue, Netizens stunned"))
				.pageNo(1)
				.latency(5)
				.build();

		return new ResponseEntity<NewAggregatorResponse>(aggregatorResponse, HttpStatus.OK);
	}

}
