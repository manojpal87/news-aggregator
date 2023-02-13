package com.example.news.aggregator.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This utility class will be used to log out bound traffic. Can be used to
 * calculate latency for SLA purpose.
 * 
 * @author manoj
 *
 */

@Component
public class OutboundLog {

	private final Logger logger = LoggerFactory.getLogger("OutboundLog");

	public void outBoundRequestStarted(Object object) {
		logger.info("Outbound request started. Request payload - " + object);
	}

	public void outBoundRequestEnded(Object object) {
		logger.info("Outbound request ended. Output response - " + object);
	}

}
