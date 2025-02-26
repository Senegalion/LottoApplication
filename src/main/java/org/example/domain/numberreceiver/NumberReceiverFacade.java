package org.example.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.domain.numberreceiver.ValidationResult.INPUT_SUCCESS;

@AllArgsConstructor
public class NumberReceiverFacade {
    private NumberValidator numberValidator;
    private final DrawDateGenerator drawDateGenerator;
    private final IdGenerable idGenerator;
    private TicketRepository ticketRepository;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbers) {
        List<ValidationResult> correctNumbers = numberValidator.filterNumbers(numbers);
        if (!correctNumbers.isEmpty()) {
            String resultMessage = numberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }

        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        String ticketId = idGenerator.getId();

        TicketDto generatedTicket = TicketDto.builder()
                .ticketId(ticketId)
                .drawDate(drawDate)
                .numbers(numbers)
                .build();

        Ticket savedTicket = Ticket.builder()
                .ticketId(generatedTicket.ticketId())
                .drawDate(generatedTicket.drawDate())
                .numbers(generatedTicket.numbers())
                .build();

        ticketRepository.save(savedTicket);

        return NumberReceiverResponseDto.builder()
                .ticketDto(generatedTicket)
                .message(INPUT_SUCCESS.info)
                .build();
    }

    public List<TicketDto> getUserNumbers(LocalDateTime dateTime) {
        List<Ticket> foundTickets = ticketRepository.findAllTicketsByDrawDate(dateTime);
        return foundTickets.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retrieveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(ticket -> TicketDto.builder()
                        .ticketId(ticket.ticketId())
                        .drawDate(ticket.drawDate())
                        .numbers(ticket.numbers())
                        .build())
                .collect(Collectors.toList());
    }

    public TicketDto findByTicketId(String ticketId) {
        try {
            Ticket ticket = ticketRepository.findByTicketId(ticketId);
            return TicketMapper.mapFromTicket(ticket);
        } catch (Exception e) {
            throw new RuntimeException(String.format("[Ticket with id [%s] does not exist]", ticketId));
        }
    }
}
