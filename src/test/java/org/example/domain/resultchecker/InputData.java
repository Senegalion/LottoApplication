package org.example.domain.resultchecker;

import org.example.domain.numberreceiver.dto.TicketDto;
import org.example.domain.resultchecker.dto.ResultDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class InputData {
    public static List<TicketDto> retrieveAllTicketsByNextDrawDate() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return List.of(TicketDto.builder()
                        .ticketId("001")
                        .numbers(Set.of(1, 2, 3, 4, 5, 6))
                        .drawDate(drawDate)
                        .build(),
                TicketDto.builder()
                        .ticketId("002")
                        .numbers(Set.of(1, 2, 7, 8, 9, 10))
                        .drawDate(drawDate)
                        .build(),
                TicketDto.builder()
                        .ticketId("003")
                        .numbers(Set.of(7, 8, 9, 10, 11, 12))
                        .drawDate(drawDate)
                        .build());
    }

    public static ResultDTO getResultDto() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResultDTO.builder()
                .resultId("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

    public static ResultDTO getResultDto1(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResultDTO.builder()
                .resultId(id)
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .guessedNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
    }

    public static List<TicketDto> retrieveAllTicketsByNextDrawDate1(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return List.of(TicketDto.builder()
                        .ticketId(id)
                        .numbers(Set.of(7, 8, 9, 10, 11, 12))
                        .drawDate(drawDate)
                        .build(),
                TicketDto.builder()
                        .ticketId("002")
                        .numbers(Set.of(7, 8, 9, 10, 11, 13))
                        .drawDate(drawDate)
                        .build(),
                TicketDto.builder()
                        .ticketId("003")
                        .numbers(Set.of(7, 8, 9, 10, 11, 14))
                        .drawDate(drawDate)
                        .build());
    }
}
