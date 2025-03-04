package org.example.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class NumberReceiverConfiguration {
    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

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
    NumberReceiverFacade numberReceiverFacade(IdGenerable idGenerator, Clock clock, TicketRepository ticketRepository) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(winningNumberValidator, drawDateGenerator, idGenerator, ticketRepository);
    }
}
