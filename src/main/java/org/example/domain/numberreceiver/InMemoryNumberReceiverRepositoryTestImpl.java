package org.example.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryNumberReceiverRepositoryTestImpl implements NumberReceiverRepository {
    Map<String, Ticket> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        inMemoryDatabase.put(ticket.ticketId(), ticket);
        return ticket;
    }

    @Override
    public List<Ticket> findAllTicketsByDrawDate(LocalDateTime dateTime) {
        return inMemoryDatabase.values().stream()
                .filter(ticket -> ticket.drawDate().isEqual(dateTime))
                .toList();
    }
}
