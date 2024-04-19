package com.example.commutator.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the generator.
 * These properties specify the parameters for generating data.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    /**
     * Indicates whether the generation process should be executed.
     */
    private boolean generate;

    /**
     * The file path for the Call Detail Records (CDR).
     */
    private String cdrPath;

    /**
     * The number of writers to be used for writing data.
     */
    private int writersAmount;

    /**
     * The capacity limit for Call Detail Records (CDR).
     */
    private int cdrCapacity;

    /**
     * The year for the CDR data.
     */
    private int year;

    /**
     * The starting month for the CDR data.
     */
    private int monthStart;

    /**
     * The ending month for the CDR data.
     */
    private int monthEnd;

    /**
     * The time gap for generating CDR data.
     */
    private int timeGap;

}
