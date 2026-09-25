package com.personal.banking_core.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

public class TransactionResponse {

	private String transactionReference;
	private TransactionOperation transactionOperation;
	private BigDecimal amount;
	private List<TransactionSummary> transactions;
	
	public TransactionResponse() {
	}

	public TransactionResponse(String transactionReference, TransactionOperation transactionOperation,
			BigDecimal amount, List<TransactionSummary> transactions) {
		this.transactionReference = transactionReference;
		this.transactionOperation = transactionOperation;
		this.amount = amount;
		this.transactions = transactions;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public TransactionOperation getTransactionOperation() {
		return transactionOperation;
	}

	public void setTransactionOperation(TransactionOperation transactionOperation) {
		this.transactionOperation = transactionOperation;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<TransactionSummary> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionSummary> transactions) {
		this.transactions = transactions;
	}
	
	
	
	
}
