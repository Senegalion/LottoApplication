package org.example.domain.numberreceiver;

import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberReceiverConfiguration {
    @Bean
    IdGenerable idGenerable() {
        return new IdGenerator();
    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(
            IdGenerable idGenerator,
            TicketRepository ticketRepository,
            DrawDateRetrieverFacade drawDateRetrieverFacade
    ) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new NumberReceiverFacade(winningNumberValidator, idGenerator, ticketRepository, drawDateRetrieverFacade);
    }
}
