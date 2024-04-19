package com.example.commutator.api.parser;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface defining the contract for writing a list of objects to a specified file path.
 *
 * @param <T> the type of objects that this writer writes to the file
 */
public interface ObjectWriter<T> {

    /**
     * Writes the given list of objects to the file located at the specified file path.
     *
     * @param filePath the path of the file to which objects should be written
     * @param objects the list of objects to be written to the file
     */
    void write(Path filePath, List<T> objects);

}

