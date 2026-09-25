package com.personal.banking_core.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.banking_core.transaction.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByTransactionReference(String transactionReference);
	List<Transaction> findByTransactionReferenceOrderByIdAsc(String transactionReference);

}
