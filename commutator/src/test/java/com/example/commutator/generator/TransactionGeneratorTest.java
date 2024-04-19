package com.example.commutator.generator;

import com.example.commutator.config.BaseTest;
import com.example.commutator.model.entity.Customer;
import com.example.commutator.model.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

public class TransactionGeneratorTest extends BaseTest {

    private static final PriorityBlockingQueue<Transaction> queue = new PriorityBlockingQueue<>();
    private static CountDownLatch latch;
    private static final List<Customer> customers = new ArrayList<>();
    private static TransactionGenerator generator;

    @BeforeAll
    private static void init() {
        latch = Mockito.mock(CountDownLatch.class);

        generator = new TransactionGenerator(queue, latch, customers, 1, 3);
    }

    @Test
    public void run_RomashkaCustomers() {
        queue.clear();
        Mockito.clearInvocations(latch);
        customers.clear();

        var firstCustomer = new Customer(1, true);
        var secondCustomer = new Customer(2, true);

        customers.addAll(List.of(firstCustomer, secondCustomer));
        Mockito.doNothing().when(latch).countDown();

        generator.run();

        Mockito.verify(latch, Mockito.times(1)).countDown();
        Assertions.assertEquals(0, queue.size() % 2);

        var firstTransaction = queue.poll();
        var secondTransaction = queue.poll();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(firstTransaction.getType(), secondTransaction.getType()),
                () -> Assertions.assertEquals(firstTransaction.getMaintenance(), secondTransaction.getConnection()),
                () -> Assertions.assertEquals(firstTransaction.getConnection(), secondTransaction.getMaintenance()),
                () -> Assertions.assertEquals(firstTransaction.getStart(), secondTransaction.getStart()),
                () -> Assertions.assertEquals(firstTransaction.getEnd(), secondTransaction.getEnd())
        );
    }

    @Test
    public void run_NotRomashkaCustomers() {
        queue.clear();
        Mockito.clearInvocations(latch);
        customers.clear();

        var firstCustomer = new Customer(1, false);
        var secondCustomer = new Customer(2, false);

        customers.addAll(List.of(firstCustomer, secondCustomer));
        Mockito.doNothing().when(latch).countDown();

        generator.run();

        Mockito.verify(latch, Mockito.times(1)).countDown();
        Assertions.assertFalse(queue.isEmpty());
    }

}
