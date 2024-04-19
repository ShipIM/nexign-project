package com.example.commutator.api.parser;

import java.nio.file.Path;
import java.util.Collection;

public interface ObjectReader<T> {

    Collection<T> read(Path filePath);

}
