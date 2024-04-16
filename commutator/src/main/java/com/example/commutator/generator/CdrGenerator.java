package com.example.commutator.generator;

import com.example.commutator.config.property.GeneratorProperties;
import com.example.commutator.model.Transaction;
import com.example.commutator.model.entity.Customer;
import com.example.commutator.producer.CsvTransactionWriter;
import com.example.commutator.producer.KafkaTransactionProducer;
import com.example.commutator.repository.CustomerRepository;
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

    private final static String PATH = "cdr/%d.txt";

    private final GeneratorProperties generatorProperties;

    private final KafkaTransactionProducer kafkaTransactionProducer;
    private final CsvTransactionWriter csvTransactionWriter;
    private final CustomerRepository customerRepository;
    private final TimeUtils timeUtils;

    public void generateCdr() throws InterruptedException {
        var queue = new PriorityBlockingQueue<Transaction>();

        var iterableNumbers = customerRepository.findAll();
        var availableNumbers = new ArrayList<Customer>();
        iterableNumbers.forEach(availableNumbers::add);
        var readOnlyNumbers = Collections.unmodifiableList(availableNumbers);

        var timeCurrent = timeUtils.getStartOfMonthUnixTime(LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1));
        var timeEnd = timeUtils.getEndOfMonthUnixTime(LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthEnd(), 1));

        var transactionsBuffer = new ArrayList<Transaction>();
        var cdrCounter = 1;

        var executorService = Executors.newFixedThreadPool(generatorProperties.getWritersAmount());

        while (timeCurrent < timeEnd) {
            var countDownLatch = new CountDownLatch(generatorProperties.getWritersAmount());
            var bound = timeCurrent + generatorProperties.getTimeGap();
            for (int i = 0; i < generatorProperties.getWritersAmount(); i++) {
                executorService.submit(new TransactionGenerator(queue, countDownLatch, readOnlyNumbers, timeCurrent,
                        bound));
            }

            countDownLatch.await();

            while (!queue.isEmpty()) {
                transactionsBuffer.add(queue.poll());

                if (transactionsBuffer.size() == generatorProperties.getCdrSize()) {
                    csvTransactionWriter.write(String.format(PATH, cdrCounter++), transactionsBuffer);
                    kafkaTransactionProducer.sendTransactions(transactionsBuffer);

                    transactionsBuffer.clear();
                }
            }

            timeCurrent = bound;
        }

        executorService.shutdown();
    }

}
