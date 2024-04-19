package com.example.commutator.parser;

import com.example.commutator.api.converter.Converter;
import com.example.commutator.config.BaseTest;
import com.example.commutator.model.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class TransactionWriterTest extends BaseTest {

    private static TransactionWriter writer;
    private static Converter<String, Transaction> converter;

    @TempDir
    private Path tempDir;

    @BeforeAll
    public static void setUp() {
        converter = Mockito.mock(Converter.class);
        writer = new TransactionWriter(converter);
    }

    @Test
    public void write() throws IOException {
        Transaction transaction = new Transaction();
        List<Transaction> transactions = List.of(transaction);

        var stringRepresentation = "transaction";
        Mockito.when(converter.convertTo(transaction)).thenReturn(stringRepresentation);

        Path filePath = tempDir.resolve("transactions.csv");

        writer.write(filePath, transactions);

        String fileContent = Files.readString(filePath);
        Assertions.assertEquals(stringRepresentation, fileContent);
    }

    @Test
    public void write_EmptyTransactionsList() throws IOException {
        List<Transaction> transactions = Collections.emptyList();

        Path filePath = tempDir.resolve("transactions.csv");

        writer.write(filePath, transactions);

        String fileContent = Files.readString(filePath);
        Assertions.assertTrue(fileContent.isEmpty());
    }

}
