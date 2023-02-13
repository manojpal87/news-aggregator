package com.example.news.aggregator.dto.newyork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Multimedia {

	private int rank;
	private String subtype;
	private String caption;
	private String credit;

	private String type;
	private String url;
	private int height;
	private int width;

	private Legacy legacy;
	private String crop_name;

}
