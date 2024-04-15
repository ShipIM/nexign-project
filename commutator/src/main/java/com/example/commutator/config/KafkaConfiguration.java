package com.example.commutator.config;

import com.example.commutator.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties properties;

    @Value("${spring.kafka.topic.outbound-topic}")
    private String outbound;
    @Value("${spring.kafka.topic.partitions-number}")
    private Integer numPartitions;
    @Value("${spring.kafka.topic.replication-factor}")
    private Short replicationFactor;

    @Bean
    public NewTopic topicCdr() {
        return new NewTopic(outbound, numPartitions, replicationFactor);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(adminProps());
    }

    @Bean
    public KafkaTemplate<String, Transaction[]> kafkaTemplate() {
        return new KafkaTemplate<>(cdrProducerFactory());
    }

    private ProducerFactory<String, Transaction[]> cdrProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private Map<String, Object> adminProps() {
        return properties.buildAdminProperties(null);
    }

    private Map<String, Object> producerProps() {
        var props = properties.buildProducerProperties(null);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(ProducerConfig.ACKS_CONFIG, "all");

        props.put(ProducerConfig.RETRIES_CONFIG, 1);

        return props;
    }

}
