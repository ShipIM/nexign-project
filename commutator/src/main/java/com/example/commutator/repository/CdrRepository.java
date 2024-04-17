package com.example.commutator.repository;

import com.example.commutator.model.entity.Cdr;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CdrRepository extends CrudRepository<Cdr, Long> {

    @Modifying
    @Query("update CDR " +
            "set IS_SENT = :sent " +
            "where ID = :id")
    void updateStatusByCdrId(@Param("id") long cdrId, @Param("sent") boolean sent);

}
