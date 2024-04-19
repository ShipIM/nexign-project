package com.example.commutator.generator;

import com.example.commutator.model.entity.Customer;
import com.example.commutator.model.entity.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class generates transactions and adds them to a priority queue.
 *
 * After generating transactions within the given time range, the class signals
 * the completion of the generation process by decrementing the `countDownLatch`.
 */
@RequiredArgsConstructor
public class TransactionGenerator implements Runnable {

    private final PriorityBlockingQueue<Transaction> transactions;
    private final CountDownLatch countDownLatch;
    private final List<Customer> availableCustomers;
    private final long timeStart;
    private final long timeLimit;

    /**
     * Executes the transaction generation process. It initializes the start time and continues
     * to generate transactions within the given time range until the time limit is reached.
     * Once completed, it decrements the `countDownLatch` to signal completion.
     */
    @Override
    public void run() {
        var callStart = timeStart;

        while (timeLimit > callStart) {
            callStart = generateTransaction(callStart);
        }

        countDownLatch.countDown();
    }

    /**
     * Generates a transaction based on the current call start time.
     *
     * @param callStart the start time for the current transaction
     * @return the end time of the generated transaction
     */
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
