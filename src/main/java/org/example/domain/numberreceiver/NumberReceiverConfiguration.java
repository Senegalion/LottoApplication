package org.example.domain.numberreceiver;

import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class NumberReceiverConfiguration {
    @Bean
    IdGenerable idGenerable() {
        return new IdGenerator();
    }

    @Bean
    TicketRepository ticketRepository() {
        return new TicketRepository() {
            @Override
            public Ticket save(Ticket ticket) {
                return null;
            }

            @Override
            public List<Ticket> findAllTicketsByDrawDate(LocalDateTime dateTime) {
                return null;
            }

            @Override
            public Ticket findByTicketId(String ticketId) {
                return null;
            }
        };
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
