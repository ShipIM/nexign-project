package com.example.commutator.parser;

import com.example.commutator.api.converter.Converter;
import com.example.commutator.api.parser.ObjectWriter;
import com.example.commutator.exception.FileWriteException;
import com.example.commutator.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionWriter implements ObjectWriter<Transaction> {

    private final Converter<String, Transaction> converter;

    public void write(Path filePath, List<Transaction> transactions) {
        try {
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            var csvContent = transactions.stream()
                    .map(transaction -> converter.convertTo(transaction) + "\n")
                    .collect(Collectors.joining());

            Files.writeString(filePath, csvContent);
        } catch (IOException e) {
            throw new FileWriteException("Unable to write to file: " + filePath);
        }
    }

}
