package com.example.news.aggregator.exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
	
	private String status;
	private String description;
	private LocalDate date;
	private int statusCode;

}
