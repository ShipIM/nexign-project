package com.example.commutator.producer;

import com.example.commutator.exception.FileWriteException;
import com.example.commutator.model.Transaction;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvTransactionWriter {

    private final static String PATTERN = "0%d,%d,%d,%d,%d";

    public void write(String filename, List<Transaction> transactions) {
        try {
            var path = Paths.get(filename);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            var csvContent = transactions.stream()
                    .map(transaction -> convertTo(transaction) + "\n")
                    .collect(Collectors.joining());

            Files.writeString(path, csvContent);
        } catch (IOException e) {
            throw new FileWriteException("Unable to write CSV to file: " + filename);
        }
    }

    private String convertTo(Transaction transaction) {
        return String.format(PATTERN, transaction.getType(), transaction.getMaintenance(), transaction.getConnection(),
                transaction.getStart(), transaction.getEnd());
    }

}
