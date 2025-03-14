package org.example.infrastructure.winningnumbersgenerator.http;

import lombok.AllArgsConstructor;
import org.example.domain.winningnumbersgenerator.RandomNumbersGenerable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@AllArgsConstructor
public class RandomNumbersGeneratorClientConfig {
    private final RandomNumbersGeneratorRestTemplateConfigurationProperties properties;

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .build();
    }

    @Bean
    public RandomNumbersGenerable remoteNumberGeneratorClient(RestTemplate restTemplate) {
        return new RandomNumbersGeneratorRestTemplate(restTemplate, properties.uri(), properties.port());
    }
}
