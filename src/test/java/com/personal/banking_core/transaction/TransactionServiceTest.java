package com.personal.banking_core.transaction;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.personal.banking_core.transaction.dto.CreateTransactionRequest;
import com.personal.banking_core.transaction.dto.TransactionOperation;
import com.personal.banking_core.transaction.dto.TransactionResponse;
import com.personal.banking_core.transaction.service.TransactionService;

@SpringBootTest
public class TransactionServiceTest {
	
	@Autowired
	TransactionService transactionService;
	
	@Test
	public void createTransactionTest() {
		CreateTransactionRequest request = 
				new CreateTransactionRequest("abc123", 2L, 102L, TransactionOperation.TRANSFER, new BigDecimal("50"));
		
		TransactionResponse response = transactionService.createTransaction(request);
		
		System.out.println(response);
	}
}
