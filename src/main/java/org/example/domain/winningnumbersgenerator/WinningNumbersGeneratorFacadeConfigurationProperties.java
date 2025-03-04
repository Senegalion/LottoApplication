package org.example.domain.winningnumbersgenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.number-generator.facade")
@Builder
public record WinningNumbersGeneratorFacadeConfigurationProperties(
        int numberOfNumbers,
        int minimumNumber,
        int maximumNumber) {
}
