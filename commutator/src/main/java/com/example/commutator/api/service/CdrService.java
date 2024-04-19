package com.example.commutator.api.service;

import com.example.commutator.model.entity.Cdr;

import java.nio.file.Path;

public interface CdrService {

    Cdr processCdr(Path filePath);

}
