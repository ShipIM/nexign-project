package com.example.commutator.api.parser;

import java.util.Optional;

public interface ObjectWriter<T> {

    Optional<String> write(String filename, T... objects);

}