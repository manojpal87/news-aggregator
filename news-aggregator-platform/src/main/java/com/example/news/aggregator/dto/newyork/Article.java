package com.example.news.aggregator.dto.newyork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

	private String web_url;
	private String snippet;
	private String lead_paragraph;
	private String source;
	
	private Multimedia[] multimedia;
	private Headline headline;
	private Keyword[] keywords;
	private String pub_date;

	private String document_type;
	private String news_desk;
	private String section_name;
	private Byline byline;

	private String type_of_material;
	private String _id;
	private int word_count;
	private String uri;

}
