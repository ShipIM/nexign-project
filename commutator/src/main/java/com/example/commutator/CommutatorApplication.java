package com.example.commutator;

import com.example.commutator.config.property.GeneratorProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableConfigurationProperties(value = GeneratorProperties.class)
@EnableJdbcAuditing
public class CommutatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommutatorApplication.class, args);
    }

}
