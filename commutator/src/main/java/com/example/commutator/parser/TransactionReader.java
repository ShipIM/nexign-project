package com.example.commutator.parser;

import com.example.commutator.api.converter.Converter;
import com.example.commutator.api.parser.ObjectReader;
import com.example.commutator.exception.FileReadException;
import com.example.commutator.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionReader implements ObjectReader<Transaction> {

    private final Converter<String, Transaction> converter;

    @Override
    public List<Transaction> read(Path filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            return br.lines().map(converter::convertFrom).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileReadException("Unable to read from file: " + filePath);
        }
    }

}
