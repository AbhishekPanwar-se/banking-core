package com.personal.banking_core.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.personal.banking_core.account.entity.Account;
import com.personal.banking_core.account.entity.AccountStatus;
import com.personal.banking_core.account.exception.AccountNotFoundException;
import com.personal.banking_core.account.exception.OperationNotAllowedException;
import com.personal.banking_core.account.repository.AccountRepository;
import com.personal.banking_core.transaction.dto.CreateTransactionRequest;
import com.personal.banking_core.transaction.dto.TransactionOperation;
import com.personal.banking_core.transaction.dto.TransactionResponse;
import com.personal.banking_core.transaction.dto.TransactionSummary;
import com.personal.banking_core.transaction.entity.IdempotencyRecord;
import com.personal.banking_core.transaction.entity.Transaction;
import com.personal.banking_core.transaction.entity.TransactionType;
import com.personal.banking_core.transaction.repository.IdempotencyRecordRepository;
import com.personal.banking_core.transaction.repository.TransactionRepository;

import jakarta.persistence.EntityManager;

@Service
public class TransactionService {
	private final EntityManager entityManager;
	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	private final IdempotencyRecordRepository idempotencyRecordRepository;
	
	public TransactionService(EntityManager entityManager, TransactionRepository transactionRepository, AccountRepository accountRepository, IdempotencyRecordRepository idempotencyRecordRepository) {
		this.entityManager = entityManager;
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
		this.idempotencyRecordRepository = idempotencyRecordRepository;
	}

	private String generateTransactionReference() {
		Number nextSeq = (Number) entityManager
				.createNativeQuery("SELECT nextval('transaction_reference_seq')")
				.getSingleResult();
		
		return "TXN" + String.valueOf(nextSeq);
	}
	
	@Transactional
	public TransactionResponse createTransaction(CreateTransactionRequest request) {
		//idempotency check
		Optional<IdempotencyRecord> existing = idempotencyRecordRepository.findByIdempotencyKey(request.getIdempotencyKey());
		if(existing.isPresent()) {
			List<Transaction> transactions = transactionRepository.findByTransactionReference(existing.get().getTransactionReference());
			List<TransactionSummary> summaryList = new ArrayList<>();
			
			for(Transaction transaction : transactions) {
				TransactionSummary summary = new TransactionSummary(transaction.getId(), transaction.getAccountId(), transaction.getTransactionType(), transaction.getBalanceAfterTransaction());
				summaryList.add(summary);
			}
			
			TransactionOperation operation = null;
			if(transactions.get(0).getTransactionType() == TransactionType.DEPOSIT){
				operation = TransactionOperation.DEPOSIT;
			}
			else if (transactions.get(0).getTransactionType() == TransactionType.WITHDRAWAL) {
				operation = TransactionOperation.WITHDRAWAL;
			}
			else {
				operation = TransactionOperation.TRANSFER;
			}
			
			return new TransactionResponse(existing.get().getTransactionReference(), operation, transactions.get(0).getAmount(), summaryList);
		}
		
		//source account validation check
		Optional<Account> sourceOptional = accountRepository.findById(request.getSourceAccountId());
		if(sourceOptional.isEmpty()) {
			throw new AccountNotFoundException("No source account present with provided ID: " + request.getSourceAccountId());
		}
		Account sourceAccount = sourceOptional.get();
		
		if(sourceAccount.getStatus()==AccountStatus.CLOSED) {
			throw new OperationNotAllowedException("Operation not allowed, provided source account is closed : " + request.getSourceAccountId());
		}
		if(sourceAccount.getStatus()==AccountStatus.BLOCKED) {
			throw new OperationNotAllowedException("Operation not allowed, provided source account is blocked : " + request.getSourceAccountId());
		}
		
		
		if(request.getTransactionOperation() == TransactionOperation.DEPOSIT) {
			BigDecimal balance = sourceAccount.getBalance();
			balance = balance.add(request.getAmount());
			
			sourceAccount.setBalance(balance);
			accountRepository.save(sourceAccount);
			
			Transaction transaction = new Transaction(generateTransactionReference(), request.getSourceAccountId(), TransactionType.DEPOSIT, request.getAmount(), balance);
			Transaction savedTransaction = transactionRepository.save(transaction);
			
			TransactionSummary summary = new TransactionSummary(savedTransaction.getId(), savedTransaction.getAccountId(), savedTransaction.getTransactionType(), savedTransaction.getBalanceAfterTransaction());
			List<TransactionSummary> summaryList = new ArrayList<>();
			summaryList.add(summary);
			
			//create idempotency record
			createIdempotencyRecord(request.getIdempotencyKey(), savedTransaction.getTransactionReference());
			
			return new TransactionResponse(savedTransaction.getTransactionReference(), TransactionOperation.DEPOSIT, request.getAmount(), summaryList);
		}
		else if(request.getTransactionOperation() == TransactionOperation.WITHDRAWAL) {
			BigDecimal balance = sourceAccount.getBalance();
			BigDecimal updatedBalance = balance.subtract(request.getAmount());
			
			if(updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
				throw new OperationNotAllowedException("Insufficient balance to perform withdrawal. current balance: " + balance);
			}
			
			sourceAccount.setBalance(updatedBalance);
			accountRepository.save(sourceAccount);
			
			Transaction transaction = new Transaction(generateTransactionReference(), request.getSourceAccountId(), TransactionType.WITHDRAWAL, request.getAmount(), updatedBalance);
			Transaction savedTransaction = transactionRepository.save(transaction);
			
			TransactionSummary summary = new TransactionSummary(savedTransaction.getId(), savedTransaction.getAccountId(), savedTransaction.getTransactionType(), savedTransaction.getBalanceAfterTransaction());
			List<TransactionSummary> summaryList = new ArrayList<>();
			summaryList.add(summary);
			
			//create idempotency record
			createIdempotencyRecord(request.getIdempotencyKey(), savedTransaction.getTransactionReference());
			
			return new TransactionResponse(savedTransaction.getTransactionReference(), TransactionOperation.WITHDRAWAL, request.getAmount(), summaryList);
		}
		else if (request.getTransactionOperation() == TransactionOperation.TRANSFER) {
			if(request.getDestinationAccountId() == null) {
				throw new OperationNotAllowedException("Operation not allowed, provided destination account is null");
			}
			
			Optional<Account> destinationOptional = accountRepository.findById(request.getDestinationAccountId());
			if(destinationOptional.isEmpty()) {
				throw new AccountNotFoundException("No destination account present with provided ID: " + request.getDestinationAccountId());
			}
			Account destinationAccount = destinationOptional.get();
			
			if(destinationAccount.getStatus()==AccountStatus.CLOSED) {
				throw new OperationNotAllowedException("Operation not allowed, provided destination account is closed : " + request.getDestinationAccountId());
			}
			if(destinationAccount.getStatus()==AccountStatus.BLOCKED) {
				throw new OperationNotAllowedException("Operation not allowed, provided destination account is blocked : " + request.getDestinationAccountId());
			}
			//checking if both source and destination accounts are same 
			if(sourceAccount.getId().equals(destinationAccount.getId())) {
				throw new OperationNotAllowedException("Operation not allowed, source and destination accounts are same");
			}
			
			//source withdrawal
			BigDecimal sourceBalance = sourceAccount.getBalance();
			BigDecimal updatedBalance = sourceBalance.subtract(request.getAmount());
			
			if(updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
				throw new OperationNotAllowedException("Insufficient balance in source account to perform transfer. current balance: " + sourceBalance);
			}
			
			sourceAccount.setBalance(updatedBalance);
			accountRepository.save(sourceAccount);
			
			String transactionRef = generateTransactionReference();
			
			Transaction sourceTransaction = new Transaction(transactionRef, request.getSourceAccountId(), TransactionType.TRANSFER_DEBIT, request.getAmount(), updatedBalance);
			Transaction savedSourceTransaction = transactionRepository.save(sourceTransaction);
			
			// destination deposit
			BigDecimal destinationBalance = destinationAccount.getBalance();
			destinationBalance = destinationBalance.add(request.getAmount());
			
			destinationAccount.setBalance(destinationBalance);
			accountRepository.save(destinationAccount);
			
			Transaction destinationTransaction = new Transaction(transactionRef, request.getDestinationAccountId(), TransactionType.TRANSFER_CREDIT, request.getAmount(), destinationBalance);
			Transaction savedDestinationTransaction = transactionRepository.save(destinationTransaction);
			
			//response creation
			TransactionSummary sourceSummary = new TransactionSummary(savedSourceTransaction.getId(), savedSourceTransaction.getAccountId(), savedSourceTransaction.getTransactionType(), savedSourceTransaction.getBalanceAfterTransaction());
			TransactionSummary destinationSummary = new TransactionSummary(savedDestinationTransaction.getId(), savedDestinationTransaction.getAccountId(), savedDestinationTransaction.getTransactionType(), savedDestinationTransaction.getBalanceAfterTransaction());
			
			List<TransactionSummary> summaryList = new ArrayList<>();
			summaryList.add(sourceSummary);
			summaryList.add(destinationSummary);
			
			//create idempotency record
			createIdempotencyRecord(request.getIdempotencyKey(), transactionRef);
			
			return new TransactionResponse(transactionRef, TransactionOperation.TRANSFER, request.getAmount(), summaryList);
		}
		else {
			throw new OperationNotAllowedException("Unsupported transaction operation: " + request.getTransactionOperation());
		}
	}

	private void createIdempotencyRecord(String idempotencyKey, String transactionReference) {
		IdempotencyRecord idempotencyRecord = new IdempotencyRecord(idempotencyKey, transactionReference);
		idempotencyRecordRepository.save(idempotencyRecord);
	}
	
}
