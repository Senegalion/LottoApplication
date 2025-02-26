package org.example.domain.numberreceiver;

import java.time.Clock;

public class NumberReceiverConfiguration {
    NumberReceiverFacade createForTest(IdGenerable idGenerator, Clock clock, TicketRepository ticketRepository) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(winningNumberValidator, drawDateGenerator, idGenerator, ticketRepository);
    }
}
