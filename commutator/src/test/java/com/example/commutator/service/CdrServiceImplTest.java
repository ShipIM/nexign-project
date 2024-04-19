package com.example.commutator.service;

import com.example.commutator.config.BaseTest;
import com.example.commutator.model.entity.Transaction;
import com.example.commutator.parser.TransactionWriter;
import com.example.commutator.api.repository.CdrRepository;
import com.example.commutator.api.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CdrServiceImplTest extends BaseTest {

    @Autowired
    private CdrServiceImpl cdrService;

    @MockBean
    private KafkaTemplate<String, List<Transaction>> kafkaTemplate;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CdrRepository cdrRepository;

    @Autowired
    private TransactionWriter writer;
    @TempDir
    private Path tempDir;

    @BeforeEach
    public void clear() {
        cdrRepository.deleteAll();
    }

    @Test
    public void processCdr() {
        Transaction transaction = Transaction.builder()
                .type((short) 1)
                .maintenance(72470375485L)
                .connection(79231403499L)
                .start(1)
                .end(1).build();
        List<Transaction> transactions = List.of(transaction);

        Path filePath = tempDir.resolve("transactions.csv");
        writer.write(filePath, transactions);

        var completeExceptionFuture = new CompletableFuture<SendResult<String, List<Transaction>>>();
        completeExceptionFuture.completeExceptionally(new KafkaException("some kafka exception"));
        Mockito.when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(completeExceptionFuture);

        var cdr = cdrService.processCdr(filePath);

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.any(), Mockito.any());
        Assertions.assertAll(
                () -> Assertions.assertEquals(LocalDate.now(), cdr.getCreated()),
                () -> Assertions.assertEquals(filePath.toString(), cdr.getFile()),
                () -> Assertions.assertFalse(cdr.isSent()),
                () -> Assertions.assertEquals(1, transactionRepository.count())
        );
    }

}
