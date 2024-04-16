package com.example.commutator.thread;

import com.example.commutator.model.Transaction;
import com.example.commutator.model.entity.Customer;
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
        var callEnd = callStart + ThreadLocalRandom.current().nextLong(0, timeLimit - timeStart);

        var maintenanceCustomer = availableCustomers.get(maintenance);
        var connectionCustomer = availableCustomers.get(connection);
        transactions.put(new Transaction(type, maintenanceCustomer.getNumber(), connectionCustomer.getNumber(),
                callStart, callEnd));
        if (connectionCustomer.isRomashka()) {
            transactions.put(new Transaction((short) (type % 2 == 0 ? 1 : 2), connectionCustomer.getNumber(),
                    maintenanceCustomer.getNumber(), callStart, callEnd));
        }

        return callEnd;
    }

}
