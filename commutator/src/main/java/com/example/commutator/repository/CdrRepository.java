package com.example.commutator.repository;

import com.example.commutator.model.entity.Cdr;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdrRepository extends CrudRepository<Cdr, Long> {
}
