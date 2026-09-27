package com.personal.banking_core.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.personal.banking_core.transaction.entity.TransactionType;

public class TransactionHistoryItem {
	private String transactionReference;
	private Long accountId;
	private TransactionType transactionType;
	private BigDecimal amount;
	private BigDecimal balanceAfterTransaction;
	private LocalDateTime transactionDateTime;
	
	public TransactionHistoryItem() {}

	public TransactionHistoryItem(String transactionReference, Long accountId, TransactionType transactionType,
			BigDecimal amount, BigDecimal balanceAfterTransaction, LocalDateTime transactionDateTime) {
		this.transactionReference = transactionReference;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balanceAfterTransaction = balanceAfterTransaction;
		this.transactionDateTime = transactionDateTime;
	}
	
	public String getTransactionReference() {
		return transactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}
	public void setBalanceAfterTransaction(BigDecimal balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}
	public LocalDateTime getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(LocalDateTime transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	
	
}
