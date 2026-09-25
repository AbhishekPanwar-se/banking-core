package com.personal.banking_core.transaction.dto;

import java.math.BigDecimal;

import com.personal.banking_core.transaction.entity.TransactionType;

public class TransactionSummary {
	private Long transactionId;
	private Long accountId;
	private TransactionType transactionType;
	private BigDecimal balanceAfterTransaction;
	
	public TransactionSummary() {
	}

	public TransactionSummary(Long transactionId, Long accountId, TransactionType transactionType,
			BigDecimal balanceAfterTransaction) {
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.balanceAfterTransaction = balanceAfterTransaction;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}

	public void setBalanceAfterTransaction(BigDecimal balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}
	
	

}
