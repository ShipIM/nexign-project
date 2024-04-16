package com.example.commutator.producer;

import com.example.commutator.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaTransactionProducer {

    private final KafkaTemplate<String, List<Transaction>> kafkaTemplate;

    @Value("${spring.kafka.topic.outbound-topic}")
    private String topic;

    public void sendTransactions(List<Transaction> transactions) {
        kafkaTemplate.send(topic, transactions);
    }

}
