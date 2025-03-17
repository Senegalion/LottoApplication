package org.example.domain.numberreceiver;

import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class NumberReceiverConfiguration {
    @Bean
    NumberReceiverFacade numberReceiverFacade(
            TicketRepository ticketRepository,
            DrawDateRetrieverFacade drawDateRetrieverFacade
    ) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new NumberReceiverFacade(winningNumberValidator, ticketRepository, drawDateRetrieverFacade);
    }
}
