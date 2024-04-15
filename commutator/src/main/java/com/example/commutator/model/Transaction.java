package com.example.commutator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    private Short type;

    private Integer maintenance;

    private Integer connection;

    private Long start;

    private Long end;

}
