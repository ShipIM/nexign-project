package com.example.commutator.api.repository;

import com.example.commutator.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository extends CrudRepository<Transaction, Integer> {
}
