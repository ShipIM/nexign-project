package com.example.commutator.converter;

import com.example.commutator.api.converter.Converter;
import com.example.commutator.exception.IncorrectFormatException;
import com.example.commutator.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class CsvTransactionConverter implements Converter<String, Transaction> {

    private final static String PATTERN = "%02d,%d,%d,%d,%d";

    @Override
    public String convertTo(Transaction transaction) {
        return String.format(PATTERN, transaction.getType(), transaction.getMaintenance(), transaction.getConnection(),
                transaction.getStart(), transaction.getEnd());
    }

    @Override
    public Transaction convertFrom(String line) {
        var parts = line.split(",");

        var transaction = new Transaction();

        try {
            transaction.setType(Short.parseShort(parts[0]));
            transaction.setMaintenance(Long.parseLong(parts[1]));
            transaction.setConnection(Long.parseLong(parts[2]));
            transaction.setStart(Long.parseLong(parts[3]));
            transaction.setEnd(Long.parseLong(parts[4]));

            return transaction;
        } catch (RuntimeException e) {
            throw new IncorrectFormatException("Problems with transaction conversion");
        }
    }

}
