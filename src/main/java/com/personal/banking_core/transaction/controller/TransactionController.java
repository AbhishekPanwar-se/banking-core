package com.personal.banking_core.transaction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.banking_core.transaction.dto.CreateTransactionRequest;
import com.personal.banking_core.transaction.dto.TransactionResponse;
import com.personal.banking_core.transaction.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
	
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PostMapping
	public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request){
		TransactionResponse response = transactionService.createTransaction(request);
		return new ResponseEntity<TransactionResponse>(response, HttpStatus.CREATED);
	}
}
