package org.example.domain.numberreceiver;

import java.time.Clock;

public class NumberReceiverConfiguration {
    NumberReceiverFacade createForTest(IdGenerable idGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, drawDateGenerator, idGenerator, ticketRepository);
    }
}
