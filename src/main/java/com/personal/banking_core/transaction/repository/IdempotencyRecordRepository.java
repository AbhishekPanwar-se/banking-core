package com.personal.banking_core.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.banking_core.transaction.entity.IdempotencyRecord;

@Repository
public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, Long> {
	Optional<IdempotencyRecord> findByIdempotencyKey(String idempotencyKey);
}
