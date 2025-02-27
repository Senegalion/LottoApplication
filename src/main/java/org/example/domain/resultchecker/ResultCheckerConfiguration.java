package org.example.domain.resultchecker;

import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;

public class ResultCheckerConfiguration {
    ResultCheckerFacade createForTest(NumberReceiverFacade receiverFacade, WinningNumbersGeneratorFacade generatorFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(receiverFacade, generatorFacade, playerRepository, winnersRetriever);
    }
}
