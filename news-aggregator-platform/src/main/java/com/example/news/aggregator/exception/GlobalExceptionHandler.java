package com.example.news.aggregator.exception;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * All the exceptions can be handled here.
 * 
 * @author manoj
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = CustomRunTimeException.class)
	public ResponseEntity<ErrorMessage> resourceNotFoundException(CustomRunTimeException ex) {
	
		ErrorMessage message = ErrorMessage.builder()
				.description(ex.getMessage())
				.status("Exception occured")
				.date(LocalDate.now())
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
