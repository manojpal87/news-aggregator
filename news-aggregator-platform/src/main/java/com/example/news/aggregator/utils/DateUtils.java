package com.example.news.aggregator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DateUtils {

	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String format(Date date) {
		return dateFormat.format(date);
	}

	public static Date parse(String date) {
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			log.error("Error occured while parsing the given date", e);
		}
		return new Date();
	}

}
