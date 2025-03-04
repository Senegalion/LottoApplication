package org.example.infrastructure.winningnumbersgenerator.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.number-generator.http.client.config")
@Builder
public record RandomNumberGeneratorRestTemplateConfigurationProperties(
        String uri,
        int port,
        int connectionTimeout,
        int readTimeout) {
}
