package org.example.domain.resultchecker;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WinnersRetriever {
    public List<Player> retrieveWinners(List<Ticket> tickets, Set<Integer> winningNumbers) {
        return tickets.stream()
                .map(ticket -> {
                    Set<Integer> guessedNumbers = calculateHits(ticket.numbers(), winningNumbers);
                    return buildPlayer(ticket, guessedNumbers);
                })
                .toList();
    }

    private Player buildPlayer(Ticket ticket, Set<Integer> guessedNumbers) {
        return Player.builder()
                .playerId(ticket.ticketId())
                .numbers(ticket.numbers())
                .guessedNumbers(guessedNumbers)
                .drawDate(ticket.drawDate())
                .isWinner(isPlayerWinner(guessedNumbers))
                .build();
    }

    private boolean isPlayerWinner(Set<Integer> guessedNumbers) {
        return guessedNumbers.size() >= WinnerInfo.NUMBERS_WHEN_PLAYER_WON.number;
    }

    private Set<Integer> calculateHits(Set<Integer> userNumbers, Set<Integer> winningNumbers) {
        return userNumbers.stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
    }
}
