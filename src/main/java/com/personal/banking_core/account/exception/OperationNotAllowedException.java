package com.personal.banking_core.account.exception;

public class OperationNotAllowedException extends RuntimeException {

	public OperationNotAllowedException(String message) {
		super(message);
	}
	
}
