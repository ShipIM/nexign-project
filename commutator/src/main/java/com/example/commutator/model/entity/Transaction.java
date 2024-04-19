package com.example.commutator.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSACTION")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction implements Comparable<Transaction> {

    @JsonIgnore
    @Id
    private long id;

    @Column("CALL_TYPE")
    private short type;

    @Column("MAINTENANCE_NUMBER")
    private long maintenance;

    @Column("CONNECTION_NUMBER")
    private long connection;

    @Column("CALL_START")
    private long start;

    @Column("CALL_END")
    private long end;

    @JsonIgnore
    @Column("CDR_ID")
    private Long cdr;

    @Override
    public int compareTo(Transaction transaction) {
        return Long.compare(this.start, transaction.start);
    }

}
