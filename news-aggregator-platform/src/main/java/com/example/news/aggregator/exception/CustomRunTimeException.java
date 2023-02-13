package com.example.news.aggregator.exception;

public class CustomRunTimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String code;
	private String message;

	public CustomRunTimeException(String message) {
		super(message);
	}

	public CustomRunTimeException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
