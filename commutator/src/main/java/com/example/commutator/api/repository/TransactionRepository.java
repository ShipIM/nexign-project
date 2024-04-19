package com.example.commutator.api.repository;

import com.example.commutator.model.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Transaction entities.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
