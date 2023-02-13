package com.example.news.aggregator.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This utility class will be used to log in bound traffic. Can be used to
 * calculate latency for SLA purpose.
 * 
 * @author manoj
 *
 */

@Component
public class InboundLog {

	private final Logger logger = LoggerFactory.getLogger("InboundLog");

	public void logRequestStarted() {

		// TODO required fields can be logged here.

		logger.info("Inbound request started...");
	}

	public void logRequestEnded() {

		logger.info("Inbound request ended...");
	}

}
