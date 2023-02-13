package com.example.news.aggregator.dto.newyork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Headline {

	private String main;
	private String kicker;
	private String content_kicker;
	private String print_headline;

	private String name;
	private String seo;
	private String sub;

}
