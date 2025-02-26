package org.example.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository {
    Ticket save(Ticket ticket);

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime dateTime);

    Ticket findByTicketId(String ticketId);
}
