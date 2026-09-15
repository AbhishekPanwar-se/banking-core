package com.personal.banking_core.account.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.banking_core.account.dto.CreateAccountRequest;
import com.personal.banking_core.account.entity.Account;
import com.personal.banking_core.account.exception.AccountOpeningNotAllowedException;
import com.personal.banking_core.account.repository.AccountRepository;
import com.personal.banking_core.customer.entity.Customer;
import com.personal.banking_core.customer.entity.CustomerStatus;
import com.personal.banking_core.customer.exception.CustomerNotFoundException;
import com.personal.banking_core.customer.repository.CustomerRepository;

import jakarta.persistence.EntityManager;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
	private final EntityManager entityManager;
	private final CustomerRepository customerRepository;
	
	public AccountService(AccountRepository repository, EntityManager entityManager, CustomerRepository customerRepository) {
		this.accountRepository = repository;
		this.entityManager = entityManager;
		this.customerRepository = customerRepository;
	}
	
	private String generateAccountNumber() {
		Number nextSeq = (Number) entityManager
				.createNativeQuery("SELECT nextval('account_number_seq')")
				.getSingleResult();
		
		return String.valueOf(nextSeq);
	}
	
	public Account createAccount(CreateAccountRequest createDto) {
		Optional<Customer> optional = customerRepository.findById(createDto.getCustomerId());
		if(optional.isEmpty()) {
			throw new CustomerNotFoundException("No customer present with provided ID: " + createDto.getCustomerId());
		}
		if(optional.get().getStatus().compareTo(CustomerStatus.BLOCKED)==0 || 
				optional.get().getStatus().compareTo(CustomerStatus.CLOSED)==0) {
			throw new AccountOpeningNotAllowedException("Account Opening is not allowed on customer : " + createDto.getCustomerId()
			+ " Customer either blocked or closed");
		}
		
		String accNum = generateAccountNumber();
		Account account = new Account(accNum, createDto.getCustomerId(), createDto.getAccountType(), createDto.getCurrency());
		return accountRepository.save(account);
	}
	
}
