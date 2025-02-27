package org.example.domain.resultchecker;

import org.example.domain.numberreceiver.dto.TicketDto;
import org.example.domain.resultchecker.dto.ResultDTO;

import java.util.List;

public class ResultCheckerMapper {
    public static List<Ticket> mapFromTicketDto(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticketDto -> Ticket.builder()
                        .ticketId(ticketDto.ticketId())
                        .drawDate(ticketDto.drawDate())
                        .numbers(ticketDto.numbers())
                        .build()
                )
                .toList();
    }

    public static List<ResultDTO> mapPlayersToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultDTO.builder()
                        .resultId(player.playerId())
                        .numbers(player.numbers())
                        .guessedNumbers(player.guessedNumbers())
                        .drawDate(player.drawDate())
                        .isWinner(player.isWinner())
                        .build()
                ).toList();
    }
}
