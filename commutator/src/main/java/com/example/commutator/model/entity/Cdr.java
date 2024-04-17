package com.example.commutator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "CDR")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cdr {

    @Id
    private long id;

    @Column("IS_SENT")
    private boolean sent;

    @Column("CREATED_DATE")
    @CreatedDate
    private LocalDate created;

}
