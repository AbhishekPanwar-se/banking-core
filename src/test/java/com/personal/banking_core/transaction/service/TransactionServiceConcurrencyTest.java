package com.personal.banking_core.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.personal.banking_core.account.entity.Account;
import com.personal.banking_core.account.repository.AccountRepository;
import com.personal.banking_core.transaction.dto.CreateTransactionRequest;
import com.personal.banking_core.transaction.dto.TransactionOperation;
import com.personal.banking_core.transaction.dto.TransactionResponse;
import com.personal.banking_core.transaction.repository.TransactionRepository;

import jakarta.persistence.OptimisticLockException;

@SpringBootTest
public class TransactionServiceConcurrencyTest {
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Test
	public void createTransactionTest() throws InterruptedException, ExecutionException {
		CreateTransactionRequest request1 =
		        new CreateTransactionRequest(
		        		UUID.randomUUID().toString(),
		                102L,
		                null,
		                TransactionOperation.WITHDRAWAL,
		                new BigDecimal("1000"));

		CreateTransactionRequest request2 =
		        new CreateTransactionRequest(
		        		UUID.randomUUID().toString(),
		                102L,
		                null,
		                TransactionOperation.WITHDRAWAL,
		                new BigDecimal("1000"));
		
		CountDownLatch startLatch = new CountDownLatch(1);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		Account initialAccount = accountRepository.findById(102L).orElseThrow();
		BigDecimal initialBalance = initialAccount.getBalance();
		
		long initialTransactionCount = transactionRepository.count();
		
		Runnable task1 = () -> {
		    try {
		        startLatch.await();
		        transactionService.createTransaction(request1);
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		    }
		};

		Runnable task2 = () -> {
		    try {
		        startLatch.await();
		        transactionService.createTransaction(request2);
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		    }
		};
		
		Future<?> future1 = executor.submit(task1);
		Future<?> future2 = executor.submit(task2);
		
		startLatch.countDown();
		
		ExecutionException exception1 = null;
		ExecutionException exception2 = null;

		try {
		    future1.get();
		} catch (ExecutionException e) {
		    exception1 = e;
		}

		try {
		    future2.get();
		} catch (ExecutionException e) {
		    exception2 = e;
		}
		
		Account finalAccount = accountRepository.findById(102L).orElseThrow();
		
		assertEquals(
			    initialBalance.subtract(new BigDecimal("1000")),
			    finalAccount.getBalance()
			);
		
		long finalTransactionCount = transactionRepository.count();

		assertEquals(
		    initialTransactionCount + 1,
		    finalTransactionCount,
		    "Exactly one withdrawal transaction should be persisted"
		);
		
		assertTrue(
			    exception1 != null || exception2 != null,
			    "At least one transaction should fail due to optimistic locking"
			);
		
		assertTrue(
			    (exception1 == null && exception2 != null)
			    || (exception1 != null && exception2 == null),
			    "Exactly one transaction should succeed and the other should fail"
			);
		
//		assertTrue(
//			    (exception1 != null && exception1.getCause() instanceof OptimisticLockException)
//			    || (exception2 != null && exception2.getCause() instanceof OptimisticLockException),
//			    "The failed transaction should fail due to optimistic locking"
//			);

		
	}
}
