package com.example.commutator.api.repository;

import com.example.commutator.model.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Customer entities.
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
