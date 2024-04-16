package com.example.commutator.thread;

import com.example.commutator.model.Transaction;
import com.example.commutator.model.entity.Customer;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class TransactionGenerator implements Runnable {

    private final PriorityBlockingQueue<Transaction> transactions;

    private final Semaphore writeSemaphore;
    private final AtomicInteger readyCounter;

    private final Semaphore readSemaphore;

    private final long timeStart;
    private final long timeLimit;
    private final int timeGap;

    private final List<Customer> availableCustomers;

    @Override
    public void run() {
        try {
            var timeStartCurrentGap = timeStart;
            var callStart = timeStartCurrentGap;

            while (timeStartCurrentGap + timeGap < timeLimit) {
                writeSemaphore.acquire();

                while (timeStartCurrentGap + timeGap > callStart) {
                    callStart = generateTransaction(callStart);
                }

                if (readyCounter.decrementAndGet() == 0) {
                    readSemaphore.release();
                }

                timeStartCurrentGap += timeGap;
            }

            writeSemaphore.acquire();
            if (readyCounter.decrementAndGet() == 0) {
                readSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long generateTransaction(long callStart) {
        var customersAmount = availableCustomers.size();

        var type = (short) ThreadLocalRandom.current().nextInt(1, 2);
        var maintenance = ThreadLocalRandom.current().nextInt(0, customersAmount);
        var connection = ThreadLocalRandom.current().nextInt(0, customersAmount);
        while (connection == maintenance) {
            connection = ThreadLocalRandom.current().nextInt(0, customersAmount + 1);
        }
        var callEnd = callStart + ThreadLocalRandom.current().nextInt(0, timeGap);

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
