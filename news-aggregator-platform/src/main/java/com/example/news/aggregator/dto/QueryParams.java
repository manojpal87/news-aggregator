package com.example.news.aggregator.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Query filter payloads
 * 
 * @author manoj
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryParams {

	private String query;
	private String section;
	private String tag;
	private Date fromDate;
	private Date toDate;
	private int page;
	private OrderBy orderBy;

}
