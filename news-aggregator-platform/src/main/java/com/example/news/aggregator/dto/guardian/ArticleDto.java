package com.example.news.aggregator.dto.guardian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

	private String id;
	private String type;
	private String sectionId;
	private String sectionName;
	private String webPublicationDate;
	private String webTitle;
	private String webUrl;
	private String apiUrl;
	private boolean isHosted;
	private String pillarId;
	private String pillarName;

}
