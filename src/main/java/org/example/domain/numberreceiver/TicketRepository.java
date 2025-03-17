package org.example.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime dateTime);

    Ticket findByTicketId(String ticketId);
}
