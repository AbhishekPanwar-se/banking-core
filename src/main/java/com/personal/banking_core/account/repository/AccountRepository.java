package com.personal.banking_core.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.banking_core.account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	List<Account> findByCustomerId(Long customerId);
}
