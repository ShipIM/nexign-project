package com.example.commutator.converter;

import com.example.commutator.config.BaseTest;
import com.example.commutator.exception.IncorrectFormatException;
import com.example.commutator.model.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CsvTransactionConverterTest extends BaseTest {

    @Autowired
    private CsvTransactionConverter converter;

    private static Transaction transaction;
    private static String csvString;

    @BeforeAll
    private static void init() {
        var type = (short) 1;
        var maintenance = 1;
        var connection = 2;
        var start = 1;
        var end = 1;

        transaction = Transaction.builder()
                .type(type)
                .maintenance(maintenance)
                .connection(connection)
                .start(start)
                .end(end).build();
        csvString = "0" + type + "," + maintenance + "," + connection + "," + start + "," + end;
    }

    @Test
    public void convertTo() {
        String resultCsvString = converter.convertTo(transaction);

        Assertions.assertEquals(csvString, resultCsvString);
    }

    @Test
    public void convertFrom() {
        Transaction resultTransaction = converter.convertFrom(csvString);

        Assertions.assertEquals(transaction, resultTransaction);
    }

    @Test
    public void convertFrom_IncorrectStringFormat_LessPartsThanExpected() {
        var csvString = "";

        Assertions.assertThrows(IncorrectFormatException.class, () -> converter.convertFrom(csvString));
    }

    @Test
    public void convertFrom_IncorrectStringFormat_IncorrectNumberFormat() {
        var csvString = "ab, cd, ef, gh, ij";

        Assertions.assertThrows(IncorrectFormatException.class, () -> converter.convertFrom(csvString));
    }

}
