package com.personal.banking_core.account.dto;

import com.personal.banking_core.account.entity.AccountType;
import com.personal.banking_core.account.entity.Currency;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateAccountRequest {
	@NotNull
	@Positive
	private Long customerId;
	
	@NotNull
	private AccountType accountType;
	
	@NotNull
	private Currency currency;
	
	public CreateAccountRequest() {
	}

	public CreateAccountRequest(Long customerId, AccountType accountType, Currency currency) {
		this.customerId = customerId;
		this.accountType = accountType;
		this.currency = currency;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	
	
	
}
