package com.example.commutator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "number")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Number {

    @Id
    private Integer number;

    @Column("is_customer")
    private Boolean isCustomer;

}
