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

    private final Long timeStart;
    private final Long timeLimit;
    private final Integer timeGap;

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

    private Long generateTransaction(Long callStart) {
        var type = (short) ThreadLocalRandom.current().nextInt(1, 2);
        var maintenance = availableCustomers.get(ThreadLocalRandom.current()
                .nextInt(0, availableCustomers.size() + 1));
        var connection = availableCustomers.get(ThreadLocalRandom.current()
                .nextInt(0, availableCustomers.size() + 1));
        while (connection.equals(maintenance)) {
            connection = availableCustomers.get(ThreadLocalRandom.current()
                    .nextInt(0, availableCustomers.size() + 1));
        }
        var callEnd = callStart + ThreadLocalRandom.current().nextInt(0, timeGap);

        transactions.put(new Transaction(type, maintenance.getNumber(), connection.getNumber(), callStart, callEnd));
        if (connection.isCustomer()) {
            transactions.put(new Transaction((short) (type % 2 == 0 ? 1 : 2), connection.getNumber(),
                    maintenance.getNumber(), callStart, callEnd));
        }

        return callEnd;
    }

}
