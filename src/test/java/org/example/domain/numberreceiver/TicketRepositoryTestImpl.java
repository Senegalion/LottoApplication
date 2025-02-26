package org.example.domain.numberreceiver;

import org.example.domain.numberreceiver.Ticket;
import org.example.domain.numberreceiver.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TicketRepositoryTestImpl implements TicketRepository {
    Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        tickets.put(ticket.ticketId(), ticket);
        return ticket;
    }

    @Override
    public List<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate) {
        return tickets.values().stream()
                .filter(ticket -> ticket.drawDate().isEqual(drawDate))
                .toList();
    }

    @Override
    public Ticket findByTicketId(String ticketId) {
        return tickets.get(ticketId);
    }
}
