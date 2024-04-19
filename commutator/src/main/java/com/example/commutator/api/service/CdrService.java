package com.example.commutator.api.service;

import com.example.commutator.model.entity.Cdr;

import java.nio.file.Path;

/**
 * Interface defining the contract for processing Call Data Records (CDR) from a specified file path.
 */
public interface CdrService {

    /**
     * Processes a Call Detail Record (CDR) from the file located at the specified file path.
     *
     * @param filePath the path of the file from which the CDR should be processed
     * @return the processed Call Detail Record (CDR)
     */
    Cdr processCdr(Path filePath);

}

