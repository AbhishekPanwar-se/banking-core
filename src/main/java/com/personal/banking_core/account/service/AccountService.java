package com.personal.banking_core.account.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.banking_core.account.dto.CreateAccountRequest;
import com.personal.banking_core.account.entity.Account;
import com.personal.banking_core.account.entity.AccountStatus;
import com.personal.banking_core.account.exception.AccountNotFoundException;
import com.personal.banking_core.account.exception.OperationNotAllowedException;
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
		if(optional.get().getStatus()==CustomerStatus.BLOCKED || 
				optional.get().getStatus()==CustomerStatus.CLOSED) {
			throw new OperationNotAllowedException("Account Opening is not allowed on customer : " + createDto.getCustomerId()
			+ " Customer either blocked or closed");
		}
		
		String accNum = generateAccountNumber();
		Account account = new Account(accNum, createDto.getCustomerId(), createDto.getAccountType(), createDto.getCurrency());
		return accountRepository.save(account);
	}
	
	public Account getAccountById(Long id) {
		Optional<Account> optional = accountRepository.findById(id);
		if(optional.isEmpty()) {
			throw new AccountNotFoundException("No account present with provided ID: " + id);
		}
		
		return optional.get();
	}

	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}
	
	public List<Account> getAccountsByCustomerId(Long customerId){
		Optional<Customer> optional = customerRepository.findById(customerId);
		if(optional.isEmpty()) {
			throw new CustomerNotFoundException("No customer present with provided ID: " + customerId);
		}
		
		return accountRepository.findByCustomerId(customerId);
	}
	
	public Account blockAccountById(Long id) {
		Optional<Account> optional = accountRepository.findById(id);
		if(optional.isEmpty()) {
			throw new AccountNotFoundException("No account present with provided ID: " + id);
		}
		Account account = optional.get();
		
		if(account.getStatus()==AccountStatus.CLOSED) {
			throw new OperationNotAllowedException("Operation not allowed, provided account is closed : " + id);
		}
		if(account.getStatus()==AccountStatus.BLOCKED) {
			throw new OperationNotAllowedException("Operation not allowed, provided account is already blocked : " + id);
		}
		
		account.setStatus(AccountStatus.BLOCKED);
		
		return accountRepository.save(account);
	}
	
	public Account unblockAccountById(Long id) {
		Optional<Account> optional = accountRepository.findById(id);
		if(optional.isEmpty()) {
			throw new AccountNotFoundException("No account present with provided ID: " + id);
		}
		Account account = optional.get();
		
		if(account.getStatus()==AccountStatus.CLOSED) {
			throw new OperationNotAllowedException("Operation not allowed, provided account is closed : " + id);
		}
		if(account.getStatus()==AccountStatus.ACTIVE) {
			throw new OperationNotAllowedException("Operation not allowed, provided account is already unblocked : " + id);
		}
		
		account.setStatus(AccountStatus.ACTIVE);
		
		return accountRepository.save(account);
	}
	
	public Account closeAccountById(Long id) {
		Optional<Account> optional = accountRepository.findById(id);
		if(optional.isEmpty()) {
			throw new AccountNotFoundException("No account present with provided ID: " + id);
		}
		Account account = optional.get();
		
		if(account.getStatus()==AccountStatus.CLOSED) {
			throw new OperationNotAllowedException("Operation not allowed, provided account is already closed : " + id);
		}
		
		if(account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
			throw new OperationNotAllowedException("Operation not allowed, Balance not zero : " + id);
		}
		
		account.setStatus(AccountStatus.CLOSED);
		
		return accountRepository.save(account);
	}
}
