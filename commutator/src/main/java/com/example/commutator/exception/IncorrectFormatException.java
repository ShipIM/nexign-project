package com.example.commutator.exception;

/**
 * Exception thrown when an incorrect format is encountered during processing.
 */
public class IncorrectFormatException extends RuntimeException {

    /**
     * Constructs a new IncorrectFormatException with the specified detail message.
     *
     * @param message the detail message
     */
    public IncorrectFormatException(String message) {
        super(message);
    }

}
