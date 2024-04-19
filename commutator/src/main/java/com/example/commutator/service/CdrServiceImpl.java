package com.example.commutator.service;

import com.example.commutator.api.service.CdrService;
import com.example.commutator.model.entity.Cdr;
import com.example.commutator.model.entity.Transaction;
import com.example.commutator.parser.TransactionReader;
import com.example.commutator.api.repository.CdrRepository;
import com.example.commutator.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CdrServiceImpl implements CdrService {

    @Value("${spring.kafka.topic.outbound-topic}")
    private String topic;

    private final KafkaTemplate<String, List<Transaction>> kafkaTemplate;

    private final TransactionReader transactionReader;

    private final TransactionRepository transactionRepository;
    private final CdrRepository cdrRepository;

    @Transactional
    public Cdr processCdr(Path filePath) {
        var transactions = transactionReader.read(filePath);

        var cdr = cdrRepository.save(new Cdr(filePath.toString()));

        transactions.forEach(transaction -> transaction.setCdr(cdr.getId()));
        transactionRepository.saveAll(transactions);

        kafkaTemplate.send(topic, transactions)
                .whenComplete((result, exception) -> cdrRepository
                        .updateStatusByCdrId(cdr.getId(), Objects.isNull(exception)));

        return cdr;
    }

}
