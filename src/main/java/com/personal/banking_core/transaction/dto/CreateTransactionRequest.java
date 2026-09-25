package com.personal.banking_core.transaction.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateTransactionRequest {
	
	@NotBlank
	private String idempotencyKey;
	
	@NotNull
	@Positive
	private Long sourceAccountId;
	
	@Positive
	private Long destinationAccountId;
	
	@NotNull
	private TransactionOperation transactionOperation;
	
	@NotNull
	@Positive
	private BigDecimal amount;

	public CreateTransactionRequest() {
	}

	public CreateTransactionRequest(String idempotencyKey, Long sourceAccountId, Long destinationAccountId, TransactionOperation transactionOperation, BigDecimal amount) {
		this.idempotencyKey = idempotencyKey;
		this.sourceAccountId = sourceAccountId;
		this.destinationAccountId = destinationAccountId;
		this.transactionOperation = transactionOperation;
		this.amount = amount;
	}

	public String getIdempotencyKey() {
		return idempotencyKey;
	}

	public void setIdempotencyKey(String idempotencyKey) {
		this.idempotencyKey = idempotencyKey;
	}

	public Long getSourceAccountId() {
		return sourceAccountId;
	}
	
	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public Long getDestinationAccountId() {
		return destinationAccountId;
	}

	public void setDestinationAccountId(Long destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
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
	
	
}
