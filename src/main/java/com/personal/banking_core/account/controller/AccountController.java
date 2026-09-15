package com.personal.banking_core.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.personal.banking_core.account.dto.CreateAccountRequest;
import com.personal.banking_core.account.entity.Account;
import com.personal.banking_core.account.service.AccountService;

import jakarta.validation.Valid;

@RestController
public class AccountController {
	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@PostMapping("/api/v1/accounts")
	public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request){
		Account account = accountService.createAccount(request);
		return new ResponseEntity<Account>(account, HttpStatus.CREATED);
	}
}
