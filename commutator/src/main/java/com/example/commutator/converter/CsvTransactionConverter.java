package com.example.commutator.converter;

import com.example.commutator.api.converter.Converter;
import com.example.commutator.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class CsvTransactionConverter implements Converter<String, Transaction> {

    private final static String PATTERN = "0%d,%d,%d,%d,%d";

    @Override
    public String convertTo(Transaction element) {
        return String.format(PATTERN, element.getType(), element.getMaintenance(), element.getConnection(),
                element.getStart(), element.getEnd());
    }

    @Override
    public Transaction convertFrom(String element) {
        var parts = element.split(",");

        var transaction = new Transaction();

        transaction.setType(Short.parseShort(parts[0]));
        transaction.setMaintenance(Integer.parseInt(parts[1]));
        transaction.setConnection(Integer.parseInt(parts[2]));
        transaction.setStart(Long.parseLong(parts[3]));
        transaction.setEnd(Long.parseLong(parts[4]));

        return transaction;
    }

}
