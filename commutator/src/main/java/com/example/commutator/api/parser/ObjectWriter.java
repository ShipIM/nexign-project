package com.example.commutator.api.parser;

import java.nio.file.Path;
import java.util.List;

public interface ObjectWriter<T> {

    void write(Path filePath, List<T> objects);

}
