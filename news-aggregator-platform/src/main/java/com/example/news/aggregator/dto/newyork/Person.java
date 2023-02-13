package com.example.news.aggregator.dto.newyork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

	private String firstname;
	private String middlename;
	private String lastname;
	private String qualifier;
	private String title;
	private String role;
	private String organization;
	private int rank;

}
