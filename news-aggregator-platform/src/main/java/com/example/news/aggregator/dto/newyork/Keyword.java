package com.example.news.aggregator.dto.newyork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

	private String name;
	private String value;
	private int rank;
	private String major;

}
