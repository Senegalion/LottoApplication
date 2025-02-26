package org.example.domain.winningnumbersgenerator;

import org.example.domain.numberreceiver.NumberReceiverFacade;

public class WinningNumbersGeneratorConfiguration {
    WinningNumbersGeneratorFacade createForTest(RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(winningNumberValidator, numberReceiverFacade, generator, winningNumbersRepository);
    }
}
