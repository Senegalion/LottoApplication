package org.example.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.example.domain.numberreceiver.dto.InputNumbersResultDto;
import org.example.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class NumberReceiverFacade {
    private NumberValidator numberValidator;
    private NumberReceiverRepository repository;
    private Clock clock;

    public InputNumbersResultDto inputNumbers(Set<Integer> numbers) {
        boolean areAllNumbersCorrect = numberValidator.filterNumbers(numbers);
        if (areAllNumbersCorrect) {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now(clock);
            Ticket ticket = repository.save(new Ticket(ticketId, drawDate, numbers));
            return InputNumbersResultDto.builder()
                    .drawDate(ticket.drawDate())
                    .ticketId(ticket.ticketId())
                    .numbers(numbers)
                    .message("success")
                    .build();
        }
        return InputNumbersResultDto.builder()
                .message("failed")
                .build();
    }

    public List<TicketDto> getUserNumbers(LocalDateTime dateTime) {
        List<Ticket> foundTickets = repository.findAllTicketsByDrawDate(dateTime);
        return foundTickets.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }
}
