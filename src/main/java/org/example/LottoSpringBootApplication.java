package org.example;

import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import org.example.infrastructure.winningnumbersgenerator.http.RandomNumbersGeneratorRestTemplateConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
        WinningNumbersGeneratorFacadeConfigurationProperties.class,
        RandomNumbersGeneratorRestTemplateConfigurationProperties.class
})
@EnableScheduling
@EnableMongoRepositories
public class LottoSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }
}