package org.example.domain.winningnumbersgenerator;

import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WinningNumbersGeneratorConfiguration {
    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(
            RandomNumbersGenerable generator,
            WinningNumbersRepository winningNumbersRepository,
            DrawDateRetrieverFacade drawDateRetrieverFacade,
            WinningNumbersGeneratorFacadeConfigurationProperties properties
    ) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(
                winningNumberValidator, drawDateRetrieverFacade, generator, winningNumbersRepository, properties);
    }

    WinningNumbersGeneratorFacade createForTest(
            RandomNumbersGenerable generator,
            WinningNumbersRepository winningNumbersRepository,
            DrawDateRetrieverFacade drawDateRetrieverFacade
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
                drawDateRetrieverFacade,
                properties
        );
    }
}
