package com.personal.banking_core.transaction.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.banking_core.transaction.entity.Transaction;
import com.personal.banking_core.transaction.entity.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByTransactionReference(String transactionReference);
	List<Transaction> findByTransactionReferenceOrderByIdAsc(String transactionReference);
	Page<Transaction> findByAccountId(Long accountId, Pageable pageable);
	Page<Transaction> findByAccountIdAndTransactionType(Long accountId, TransactionType transactionType, Pageable pageable);
	Page<Transaction> findByAccountIdAndTransactionTypeIn(Long accountId, List<TransactionType> transactionTypes, Pageable pageable);

}
