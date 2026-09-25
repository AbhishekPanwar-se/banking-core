package com.personal.banking_core.account.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.support.TransactionTemplate;

import com.personal.banking_core.account.entity.Account;

@SpringBootTest
public class AccountRepositoryOptimisticLockTest {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Test
	public void optimisticLockTest() {
		Account account1 = transactionTemplate.execute(status -> accountRepository.findById(2L).orElseThrow());

		Account account2 = transactionTemplate.execute(status -> accountRepository.findById(2L).orElseThrow());
		
		System.out.println("Account 1 version: " + account1.getVersion());
		System.out.println("Account 2 version: " + account2.getVersion());
		
		account1.setBalance(account1.getBalance().add(new BigDecimal("100")));

		transactionTemplate.execute(status -> {
		    accountRepository.save(account1);
		    return null;
		});
		
		account2.setBalance(account2.getBalance().add(new BigDecimal("200")));

		assertThrows(ObjectOptimisticLockingFailureException.class, () -> {

		    transactionTemplate.execute(status -> {
		        accountRepository.save(account2);
		        return null;
		    });

		});
	}
}
