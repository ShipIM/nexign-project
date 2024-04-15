package com.example.commutator.thread;

import com.example.commutator.model.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TransactionConsumer implements Runnable {

    private final PriorityBlockingQueue<Transaction> transactions;

    private final Semaphore writeSemaphore;
    private final AtomicInteger readyCounter;

    private final Semaphore readSemaphore;

    @Override
    public void run() {
        try {
            readSemaphore.acquire();

            if (transactions.isEmpty()) {
                return;
            }

            while (!transactions.isEmpty()) {
                transactions.poll();
            }

            readyCounter.set(3);
            writeSemaphore.release(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
