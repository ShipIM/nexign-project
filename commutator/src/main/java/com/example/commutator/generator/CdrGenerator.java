package com.example.commutator.generator;

import com.example.commutator.api.service.CdrService;
import com.example.commutator.config.property.GeneratorProperties;
import com.example.commutator.exception.FileReadException;
import com.example.commutator.model.entity.Customer;
import com.example.commutator.model.entity.Transaction;
import com.example.commutator.parser.TransactionWriter;
import com.example.commutator.api.repository.CustomerRepository;
import com.example.commutator.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class is responsible for generating and processing Call Detail Records (CDRs).
 * It contains methods to either generate CDRs or read them from an existing directory, based on the configuration.
 */
@Component
@RequiredArgsConstructor
public class CdrGenerator {

    private static final String FILE_EXTENSION = ".txt";

    private final GeneratorProperties generatorProperties;

    private final CustomerRepository customerRepository;
    private final TimeUtils timeUtils;

    private final TransactionWriter fileWriter;

    private final CdrService cdrService;

    /**
     * Generates Call Detail Records (CDRs) by using multiple threads to generate transactions
     * within a specific time range and then writing these transactions to files.
     *
     * The method utilizes a priority blocking queue to collect transactions and a buffer list
     * to store and process transactions when the capacity is reached. Transactions are written
     * to files, which are then processed by the CDR service.
     */
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

        var transactionGeneration = Executors.newFixedThreadPool(generatorProperties.getWritersAmount());
        var cdrProcessing = Executors.newSingleThreadExecutor();

        var transactionsBuffer = new ArrayList<Transaction>();
        var cdrCounter = 0;

        try {
            while (timeCurrent < timeEnd) {
                var countDownLatch = new CountDownLatch(generatorProperties.getWritersAmount());
                var bound = timeCurrent + generatorProperties.getTimeGap();
                for (int i = 0; i < generatorProperties.getWritersAmount(); i++) {
                    transactionGeneration.submit(new TransactionGenerator(queue, countDownLatch, readOnlyNumbers,
                            timeCurrent, bound));
                }

                countDownLatch.await();

                while (!queue.isEmpty()) {
                    transactionsBuffer.add(queue.poll());

                    if (transactionsBuffer.size() == generatorProperties.getCdrCapacity()) {
                        var filePath = Path.of(generatorProperties.getCdrPath() + cdrCounter++
                                + FILE_EXTENSION);
                        fileWriter.write(filePath, transactionsBuffer);

                        cdrProcessing.submit(() -> cdrService.processCdr(filePath));

                        transactionsBuffer.clear();
                    }
                }

                timeCurrent = bound;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            transactionGeneration.shutdown();
            cdrProcessing.shutdown();
        }
    }

    /**
     * Reads Call Detail Records (CDRs) from the specified directory and processes them using the CDR service.
     * It retrieves the list of files, sorts them based on their name, and processes each file one by one.
     *
     * @throws FileReadException if an I/O error occurs while reading the directory
     */
    public void readCdr() {
        var directory = Path.of(generatorProperties.getCdrPath());

        try (var fileStream = Files.list(directory)) {
            var files = fileStream
                    .filter(Files::isRegularFile)
                    .sorted(
                            Comparator.comparingInt(a -> Integer.parseInt(a.getFileName().toString()
                                    .replaceAll("\\D", "")))
                    ).toList();

            files.forEach(cdrService::processCdr);
        } catch (IOException e) {
            throw new FileReadException("Unable to read specified directory");
        }
    }

    /**
     * Starts the generation or reading process of CDRs based on the configuration.
     * It is triggered when the application is started.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void startGeneration() {
        try {
            if (generatorProperties.isGenerate()) {
                generateCdr();
            } else {
                readCdr();
            }
        } catch (Exception e) {
            System.out.println("Unable to generate cdr files");
        }
    }

}
