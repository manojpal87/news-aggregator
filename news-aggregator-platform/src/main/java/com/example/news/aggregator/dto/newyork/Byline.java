package com.example.news.aggregator.dto.newyork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Byline {

	private String original;
	private Person[] person;
	private String organization;

}
