package org.example.domain.winningnumbersgenerator;

import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Configuration
public class WinningNumbersGeneratorConfiguration {
    @Bean
    WinningNumbersRepository repository() {
        return new WinningNumbersRepository() {
            @Override
            public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
                return Optional.of(WinningNumbers.builder()
                        .winningNumbersId("1")
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .drawDate(LocalDateTime.of(2025, 3, 8, 12, 0, 0))
                        .build());
            }

            @Override
            public boolean existsByDate(LocalDateTime nextDrawDate) {
                return false;
            }

            @Override
            public WinningNumbers save(WinningNumbers winningNumbers) {
                return null;
            }
        };
    }

    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(
            RandomNumbersGenerable generator,
            WinningNumbersRepository winningNumbersRepository,
            NumberReceiverFacade numberReceiverFacade,
            WinningNumbersGeneratorFacadeConfigurationProperties properties
    ) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(
                winningNumberValidator, numberReceiverFacade, generator, winningNumbersRepository, properties);
    }

    WinningNumbersGeneratorFacade createForTest(
            RandomNumbersGenerable generator,
            WinningNumbersRepository winningNumbersRepository,
            NumberReceiverFacade numberReceiverFacade
    ) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties
                .builder()
                .numberOfNumbers(6)
                .minimumNumber(1)
                .maximumNumber(99)
                .build();
        return winningNumbersGeneratorFacade(
                generator,
                winningNumbersRepository,
                numberReceiverFacade,
                properties
        );
    }
}
