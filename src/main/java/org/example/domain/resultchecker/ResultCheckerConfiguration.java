package org.example.domain.resultchecker;

import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResultCheckerConfiguration {
    @Bean
    ResultCheckerFacade createForTest(NumberReceiverFacade receiverFacade, WinningNumbersGeneratorFacade generatorFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(receiverFacade, generatorFacade, playerRepository, winnersRetriever);
    }
}
