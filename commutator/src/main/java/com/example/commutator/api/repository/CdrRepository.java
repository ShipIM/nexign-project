package com.example.commutator.api.repository;

import com.example.commutator.model.entity.Cdr;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Cdr entities.
 */
@Repository
public interface CdrRepository extends CrudRepository<Cdr, Long> {

    /**
     * Updates the sent status of a Call Data Record (CDR) based on the provided CDR id.
     *
     * @param cdrId the id of the Call Detail Record (CDR) to be updated
     * @param sent the new sent status to be set
     */
    @Modifying
    @Query("update CDR " +
            "set IS_SENT = :sent " +
            "where ID = :id")
    void updateStatusByCdrId(@Param("id") long cdrId, @Param("sent") boolean sent);

}
