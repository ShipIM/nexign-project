package com.example.commutator;

import com.example.commutator.config.property.GeneratorProperties;
import com.example.commutator.generator.CdrGenerator;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableConfigurationProperties(value = GeneratorProperties.class)
@EnableJdbcAuditing
public class CommutatorApplication implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(CommutatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var cdrGenerator = context.getBean(CdrGenerator.class);

        cdrGenerator.generateCdr();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
