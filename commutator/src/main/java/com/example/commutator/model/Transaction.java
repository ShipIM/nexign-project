package com.example.commutator.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transaction implements Comparable<Transaction>{

    private short type;

    private long maintenance;

    private long connection;

    private long start;

    private long end;

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(this.start, o.start);
    }

}
