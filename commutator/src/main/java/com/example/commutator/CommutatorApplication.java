package com.example.commutator;

import com.example.commutator.api.repository.CustomerRepository;
import com.example.commutator.model.Transaction;
import com.example.commutator.model.entity.Customer;
import com.example.commutator.thread.TransactionGenerator;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class CommutatorApplication implements CommandLineRunner, ApplicationContextAware {

    private final Integer WRITERS_AMOUNT = 3;

    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(CommutatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var queue = new PriorityBlockingQueue<Transaction>();
        var counter = new AtomicInteger(WRITERS_AMOUNT);

        var writeSemaphore = new Semaphore(WRITERS_AMOUNT, true);
        var readSemaphore = new Semaphore(0);

        var timeStart = 1L;
        var timeLimit = 1000L;
        var timeGap = 100;

        var numbersRepository = context.getBean(CustomerRepository.class);
        var iterableNumbers = numbersRepository.findAll();
        var availableNumbers = new ArrayList<Customer>();
        iterableNumbers.forEach(availableNumbers::add);
        var readOnlyNumbers = Collections.unmodifiableList(availableNumbers);

        ExecutorService executorService = Executors.newFixedThreadPool(WRITERS_AMOUNT);
        for (int i = 0; i < WRITERS_AMOUNT; i++) {
            executorService.submit(new TransactionGenerator(queue, writeSemaphore, counter, readSemaphore, timeStart,
                    timeLimit, timeGap, readOnlyNumbers));
        }

        while (true) {
            try {
                readSemaphore.acquire();

                if (queue.isEmpty()) {
                    executorService.shutdownNow();

                    break;
                }

                while (!queue.isEmpty()) {
                    System.out.println(queue.poll());
                }

                counter.set(WRITERS_AMOUNT);
                writeSemaphore.release(WRITERS_AMOUNT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
