package com.personal.banking_core.account.exception;

public class AccountOpeningNotAllowedException extends RuntimeException {

	public AccountOpeningNotAllowedException(String message) {
		super(message);
	}
	
}
