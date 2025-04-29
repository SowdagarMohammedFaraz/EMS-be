package com.cts.ems.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {
	String message;

	public UserNotFoundException(){}

	public UserNotFoundException(String message) {
		super(message);
		this.message = message;
	}
	
}
