package com.example.news.aggregator.dto.guardian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {

	private String status;
	private String userTier;
	private int total;
	private int startIndex;
	private int pageSize;
	private int currentPage;
	private int pages;
	private String orderBy;
	private ArticleDto[] results;

}
