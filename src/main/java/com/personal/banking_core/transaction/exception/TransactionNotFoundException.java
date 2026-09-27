package com.personal.banking_core.transaction.exception;

public class TransactionNotFoundException extends RuntimeException {

	public TransactionNotFoundException(String message) {
		super(message);
	}
	
}
