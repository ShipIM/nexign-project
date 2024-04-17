package com.example.commutator.generator;

import com.example.commutator.config.property.GeneratorProperties;
import com.example.commutator.model.entity.Transaction;
import com.example.commutator.model.entity.Customer;
import com.example.commutator.repository.CustomerRepository;
import com.example.commutator.service.CdrService;
import com.example.commutator.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

@Component
@RequiredArgsConstructor
public class CdrGenerator {

    private final GeneratorProperties generatorProperties;
    private final CdrService cdrService;
    private final CustomerRepository customerRepository;
    private final TimeUtils timeUtils;

    public void generateCdr() {
        var queue = new PriorityBlockingQueue<Transaction>();

        var iterableNumbers = customerRepository.findAll();
        var availableNumbers = new ArrayList<Customer>();
        iterableNumbers.forEach(availableNumbers::add);
        var readOnlyNumbers = Collections.unmodifiableList(availableNumbers);

        var timeCurrent = timeUtils.getStartOfMonthUnixTime(LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1));
        var timeEnd = timeUtils.getEndOfMonthUnixTime(LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthEnd(), 1));

        var executorService = Executors.newFixedThreadPool(generatorProperties.getWritersAmount());

        var transactionsBuffer = new ArrayList<Transaction>();

        try {
            while (timeCurrent < timeEnd) {
                var countDownLatch = new CountDownLatch(generatorProperties.getWritersAmount());
                var bound = timeCurrent + generatorProperties.getTimeGap();
                for (int i = 0; i < generatorProperties.getWritersAmount(); i++) {
                    executorService.submit(new TransactionGenerator(queue, countDownLatch, readOnlyNumbers,
                            timeCurrent, bound));
                }

                countDownLatch.await();

                while (!queue.isEmpty()) {
                    transactionsBuffer.add(queue.poll());

                    if (transactionsBuffer.size() == generatorProperties.getCdrSize()) {
                        cdrService.sendCdr(transactionsBuffer);

                        transactionsBuffer.clear();
                    }
                }

                timeCurrent = bound;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdownNow();
        }
    }

}
