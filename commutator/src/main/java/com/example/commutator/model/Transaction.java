package com.example.commutator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction implements Comparable<Transaction> {

    private short type;

    private long maintenance;

    private long connection;

    private long start;

    private long end;

    @Override
    public int compareTo(Transaction transaction) {
        return Long.compare(this.start, transaction.start);
    }

}
