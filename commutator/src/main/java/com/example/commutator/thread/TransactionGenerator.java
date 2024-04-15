package com.example.commutator.thread;

import com.example.commutator.model.Transaction;
import com.example.commutator.model.entity.Number;
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

    private final List<Number> availableNumbers;

    @Override
    public void run() {
        try {
            var timeStartCurrentGap = timeStart;
            var callStart = timeStartCurrentGap;

            while (timeStartCurrentGap + timeGap < timeLimit) {
                writeSemaphore.acquire();

                var type = (short) ThreadLocalRandom.current().nextInt(1, 2);
                var maintenance = ThreadLocalRandom.current().nextInt(0, availableNumbers.size() + 1);
                var connection = ThreadLocalRandom.current().nextInt(0, availableNumbers.size() + 1);
                while (connection == maintenance) {
                    connection = ThreadLocalRandom.current().nextInt(0, availableNumbers.size() + 1);
                }
                var callEnd = callStart + ThreadLocalRandom.current().nextInt(0, timeGap);
                var transaction = new Transaction(type, maintenance, connection, callStart, callEnd);

                transactions.add(transaction);
                callStart = callEnd;

                if (timeStartCurrentGap + timeGap < callStart) {
                    if (readyCounter.decrementAndGet() == 0) {
                        readSemaphore.release();

                        timeStartCurrentGap += timeGap;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
