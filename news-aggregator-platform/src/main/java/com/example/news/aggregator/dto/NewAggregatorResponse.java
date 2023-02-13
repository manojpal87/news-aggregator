package com.example.news.aggregator.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewAggregatorResponse {

	private List<String> newsWebsite;
	private List<String> articleUrl;
	private List<String> headline;
	private int totalPages;

	private String searchKeyword;
	private int pageNo;
	private int latency;

}
