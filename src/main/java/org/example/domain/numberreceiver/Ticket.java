package org.example.domain.numberreceiver;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record Ticket(
        @Id
        String ticketId,
        LocalDateTime drawDate,
        Set<Integer> numbers
) {
}
