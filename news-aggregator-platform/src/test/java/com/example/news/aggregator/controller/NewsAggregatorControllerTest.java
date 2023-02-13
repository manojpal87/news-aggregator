package com.example.news.aggregator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.news.aggregator.dto.guardian.ResponseGuardian;
import com.example.news.aggregator.dto.newyork.ResponseNY;
import com.example.news.aggregator.service.ArticleSearchService;
import com.example.news.aggregator.service.ContentSearchService;

@WebMvcTest(NewsAggregatorController.class)
public class NewsAggregatorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ArticleSearchService articleSearchService;

	@MockBean
	private ContentSearchService contentSearchService;

	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void testContentAggregateSearch() throws Exception {

		Mockito.when(articleSearchService.getNewsArticles(any())).thenReturn(ResponseNY.builder().build());
		Mockito.when(contentSearchService.searchContent(any())).thenReturn(ResponseGuardian.builder().build());

		// Need to mock security
		mockMvc.perform(get("/api/news/search")).andExpect(status().is(404));
	}

}
