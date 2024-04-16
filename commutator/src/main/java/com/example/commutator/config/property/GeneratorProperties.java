package com.example.commutator.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    private int writersAmount;

    private int cdrSize;

    private int year;

    private int monthStart;

    private int monthEnd;

    private int timeGap;

}
