package com.example.brtservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "brt_service\".\"tariff")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tariff {

    @Id
    @Column("tariff_id")
    private long id;

    private String name;

}
