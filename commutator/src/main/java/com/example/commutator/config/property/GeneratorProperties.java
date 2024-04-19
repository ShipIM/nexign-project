package com.example.commutator.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    private boolean generate;

    private String cdrPath;

    private int writersAmount;

    private int cdrCapacity;

    private int year;

    private int monthStart;

    private int monthEnd;

    private int timeGap;

}
