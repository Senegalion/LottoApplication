package org.example.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.domain.numberreceiver.ValidationResult.INPUT_SUCCESS;
import static org.example.domain.numberreceiver.ValidationResult.TICKET_DOES_NOT_EXIST;

@AllArgsConstructor
public class NumberReceiverFacade {
    private WinningNumberValidator winningNumberValidator;
    private TicketRepository ticketRepository;
    private final DrawDateRetrieverFacade drawDateRetrieverFacade;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbers) {
        NumberReceiverResponseDto resultMessage = validateNumbers(numbers);
        if (resultMessage != null) return resultMessage;

        Ticket ticket = saveTicketForNumbers(numbers);
        TicketDto ticketDto = TicketMapper.mapFromTicket(ticket);

        return NumberReceiverResponseDto.builder()
                .ticketDto(ticketDto)
                .message(INPUT_SUCCESS.info)
                .build();
    }

    private Ticket saveTicketForNumbers(Set<Integer> numbers) {
        LocalDateTime drawDate = retrieveNextDrawDate();
        Ticket savedTicket = Ticket.builder()
                .drawDate(drawDate)
                .numbers(numbers)
                .build();

        return ticketRepository.save(savedTicket);
    }

    private NumberReceiverResponseDto validateNumbers(Set<Integer> numbers) {
        List<ValidationResult> validationResults = winningNumberValidator.filterNumbers(numbers);
        if (!validationResults.isEmpty()) {
            String resultMessage = winningNumberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        return null;
    }

    public List<TicketDto> getUserNumbers(LocalDateTime dateTime) {
        List<Ticket> foundTickets = ticketRepository.findAllTicketsByDrawDate(dateTime);
        return foundTickets.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateRetrieverFacade.retrieveNextDrawDate();
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = retrieveNextDrawDate();
        return retrieveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = retrieveNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(TicketMapper::mapFromTicket)
                .collect(Collectors.toList());
    }

    public TicketDto findByTicketId(String ticketId) {
        try {
            Ticket ticket = ticketRepository.findByTicketId(ticketId);
            return TicketMapper.mapFromTicket(ticket);
        } catch (Exception e) {
            throw new RuntimeException(TICKET_DOES_NOT_EXIST.info);
        }
    }
}
