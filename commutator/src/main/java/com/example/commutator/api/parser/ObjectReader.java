package com.example.commutator.api.parser;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Interface defining the contract for reading a collection of objects from a specified file path.
 *
 * @param <T> the type of objects that this reader reads and returns
 */
public interface ObjectReader<T> {

    /**
     * Reads objects from the file located at the specified file path and returns them as a collection.
     *
     * @param filePath the path of the file from which objects should be read
     * @return a collection of objects read from the specified file path
     */
    Collection<T> read(Path filePath);

}
