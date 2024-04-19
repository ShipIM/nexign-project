package com.example.commutator.generator;

import com.example.commutator.config.property.GeneratorProperties;
import com.example.commutator.exception.FileReadException;
import com.example.commutator.model.entity.Customer;
import com.example.commutator.model.entity.Transaction;
import com.example.commutator.parser.TransactionWriter;
import com.example.commutator.repository.CustomerRepository;
import com.example.commutator.service.CdrService;
import com.example.commutator.util.TimeUtils;
import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class CdrGenerator {

    private static final String FILE_EXTENSION = ".txt";

    private final GeneratorProperties generatorProperties;

    private final CustomerRepository customerRepository;
    private final TimeUtils timeUtils;

    private final TransactionWriter fileWriter;

    private final CdrService cdrService;

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

}
