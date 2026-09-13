package com.personal.banking_core.customer.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

	public CustomerAlreadyExistsException(String message) {
		super(message);
	}
	
}
