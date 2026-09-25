package com.personal.banking_core.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/api/v1/accounts/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable Long id){
		Account account = accountService.getAccountById(id);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
	
	@GetMapping("/api/v1/accounts")
	public ResponseEntity<List<Account>> getAllAccounts(){
		List<Account> accounts = accountService.getAllAccounts();
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
	}
	
	@GetMapping("/api/v1/accounts/customer/{customerId}")
	public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable Long customerId){
		List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
	}
	
	@PatchMapping("/api/v1/accounts/{id}/block")
	public ResponseEntity<Account> blockAccountById(@PathVariable Long id){
		Account account = accountService.blockAccountById(id);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
	
	@PatchMapping("/api/v1/accounts/{id}/unblock")
	public ResponseEntity<Account> unblockAccountById(@PathVariable Long id){
		Account account = accountService.unblockAccountById(id);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
	
	@PatchMapping("/api/v1/accounts/{id}/close")
	public ResponseEntity<Account> closeAccountById(@PathVariable Long id){
		Account account = accountService.closeAccountById(id);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
}
