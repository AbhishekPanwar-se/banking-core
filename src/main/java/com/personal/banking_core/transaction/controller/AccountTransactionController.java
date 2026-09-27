package com.personal.banking_core.transaction.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.banking_core.transaction.dto.TransactionHistoryItem;
import com.personal.banking_core.transaction.dto.TransactionOperation;
import com.personal.banking_core.transaction.dto.TransactionSortDirection;
import com.personal.banking_core.transaction.dto.TransactionSortField;
import com.personal.banking_core.transaction.exception.SortingNotAllowedException;
import com.personal.banking_core.transaction.service.TransactionService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountTransactionController {
	
	private final TransactionService transactionService;

	public AccountTransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@GetMapping("/{accountId}/transactions")
	public ResponseEntity<Page<TransactionHistoryItem>> getTransactionByAccountId(
			@PathVariable @Positive Long accountId, 
			@RequestParam(name = "operation", required = false) TransactionOperation transactionOperation, 
			@RequestParam(defaultValue = "0") @PositiveOrZero int page,
			@RequestParam(defaultValue = "5") @Min(1) @Max(20) int size,
			@RequestParam(defaultValue = "transactionDateTime") String sortField,
			@RequestParam(defaultValue = "desc") String sortDirection){
		Pageable pageable = buildPageable(page, size, getSortField(sortField), getSortDirection(sortDirection));
		Page<TransactionHistoryItem> response = transactionService.getTransactionByAccountId(accountId, transactionOperation, pageable);
		return new ResponseEntity<Page<TransactionHistoryItem>>(response, HttpStatus.OK);
	}
	
	public String getSortField(String sort) {
		for(TransactionSortField field : TransactionSortField.values()) {
			if(field.getField().equalsIgnoreCase(sort)) return field.getField();
		}
		throw new SortingNotAllowedException("Sorting not allowed for provided field : " + sort);
	}
	
	private TransactionSortDirection getSortDirection(String direction) {
		try {
	        return TransactionSortDirection.valueOf(direction.toUpperCase());
	    } catch (IllegalArgumentException ex) {
	    	throw new SortingNotAllowedException("Sorting not allowed for provided direction : " + direction);
	    }
		
	}
	
	private Sort buildSort(String field, TransactionSortDirection direction) {
	    return Sort.by(Sort.Direction.fromString(direction.name()) , field);
	}
	
	private Pageable buildPageable(int page, int size, String field, TransactionSortDirection direction) {
		return PageRequest.of(page, size, buildSort(field, direction));
	}
}
