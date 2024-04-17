package com.example.commutator.service;

import com.example.commutator.exception.EntityNotFoundException;
import com.example.commutator.model.entity.Cdr;
import com.example.commutator.model.entity.Transaction;
import com.example.commutator.repository.CdrRepository;
import com.example.commutator.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CdrService {

    @Value("${spring.kafka.topic.outbound-topic}")
    private String topic;

    private final KafkaTemplate<String, List<Transaction>> kafkaTemplate;

    private final TransactionRepository transactionRepository;
    private final CdrRepository cdrRepository;

    @Transactional
    public void sendCdr(List<Transaction> transactions) {
        var cdr = cdrRepository.save(new Cdr());

        transactionRepository.saveAll(
                transactions.stream()
                        .peek(transaction -> transaction.setCdr(cdr.getId()))
                        .collect(Collectors.toList())
        );

        kafkaTemplate.send(topic, transactions)
                .whenComplete((result, exception) -> updateCdrStatus(cdr.getId(), Objects.isNull(exception)));
    }

    private void updateCdrStatus(long id, boolean status) {
        var cdr = getCdrById(id);
        cdr.setSent(status);

        cdrRepository.save(cdr);
    }

    public Cdr getCdrById(long id) {
        return cdrRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no cdr with this id"));
    }

}
