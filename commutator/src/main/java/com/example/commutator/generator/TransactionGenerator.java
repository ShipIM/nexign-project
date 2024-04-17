package com.example.commutator.generator;

import com.example.commutator.model.entity.Customer;
import com.example.commutator.model.entity.Transaction;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class TransactionGenerator implements Runnable {

    private final PriorityBlockingQueue<Transaction> transactions;
    private final CountDownLatch countDownLatch;
    private final List<Customer> availableCustomers;
    private final long timeStart;
    private final long timeLimit;

    @Override
    public void run() {
        var callStart = timeStart;

        while (timeLimit > callStart) {
            callStart = generateTransaction(callStart);
        }

        countDownLatch.countDown();
    }

    private long generateTransaction(long callStart) {
        var customersAmount = availableCustomers.size() - 1;

        var type = (short) ThreadLocalRandom.current().nextInt(1, 2);
        var maintenance = ThreadLocalRandom.current().nextInt(0, customersAmount);
        var connection = ThreadLocalRandom.current().nextInt(0, customersAmount);
        while (connection == maintenance) {
            connection = ThreadLocalRandom.current().nextInt(0, customersAmount + 1);
        }
        var callEnd = callStart + ThreadLocalRandom.current().nextLong(1, timeLimit - timeStart);

        var maintenanceCustomer = availableCustomers.get(maintenance);
        var connectionCustomer = availableCustomers.get(connection);
        transactions.put(
                Transaction.builder()
                        .type(type)
                        .maintenance(maintenanceCustomer.getNumber())
                        .connection(connectionCustomer.getNumber())
                        .start(callStart)
                        .end(callEnd).build()
        );
        if (connectionCustomer.isRomashka()) {
            transactions.put(
                    Transaction.builder()
                            .type((short) (type % 2 == 0 ? 1 : 2))
                            .maintenance(connectionCustomer.getNumber())
                            .connection(maintenanceCustomer.getNumber())
                            .start(callStart)
                            .end(callEnd).build()
            );
        }

        return callEnd;
    }

}
