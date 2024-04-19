package com.example.commutator.parser;

import com.example.commutator.config.BaseTest;
import com.example.commutator.exception.FileReadException;
import com.example.commutator.model.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class TransactionReaderTest extends BaseTest {

    @Autowired
    private TransactionWriter writer;
    @Autowired
    private TransactionReader reader;

    @TempDir
    private Path tempDir;

    @Test
    public void read() {
        Transaction transaction = Transaction.builder()
                .type((short) 1)
                .maintenance(1)
                .connection(2)
                .start(1)
                .end(1).build();
        List<Transaction> transactions = List.of(transaction);

        Path filePath = tempDir.resolve("transactions.csv");
        writer.write(filePath, transactions);

        List<Transaction> readTransactions = reader.read(filePath);

        Assertions.assertEquals(transactions, readTransactions);
    }

    @Test
    public void read_EmptyTransactionsList() {
        List<Transaction> transactions = Collections.emptyList();

        Path filePath = tempDir.resolve("transactions.csv");
        writer.write(filePath, transactions);

        List<Transaction> readTransactions = reader.read(filePath);

        Assertions.assertTrue(readTransactions.isEmpty());
    }

    @Test
    public void read_FileNotExists() {
        Path filePath = tempDir.resolve("transactions.csv");

        Assertions.assertThrows(FileReadException.class, () -> reader.read(filePath));
    }

}
