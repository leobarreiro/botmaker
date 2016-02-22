package com.javaleo.systems.botrise.ejb.exceptions;

public class BotRiseException extends Exception {

	private static final long serialVersionUID = 1L;

	public BotRiseException() {
		super();
	}

	public BotRiseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BotRiseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BotRiseException(String message) {
		super(message);
	}

	public BotRiseException(Throwable cause) {
		super(cause);
	}

}
