package com.personal.banking_core.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="account")
public class Account {
	
	@Id
	@GeneratedValue
	private Long id ;
	
	@Column(nullable=false,unique = true)
	private String accountNumber;
	
	@Column(nullable=false)
	private Long customerId;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@Column(nullable=false, precision = 19 , scale = 2)
	private BigDecimal balance;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	@CreatedDate
	@Column(nullable=false, updatable=false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	public Account() {}

	public Account(String accountNumber, Long customerId, AccountType accountType, Currency currency) {
		this.accountNumber = accountNumber;
		this.customerId = customerId;
		this.accountType = accountType;
		this.currency = currency;
		this.balance = BigDecimal.ZERO;
		this.status = AccountStatus.ACTIVE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	

}
