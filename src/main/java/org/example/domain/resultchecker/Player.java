package org.example.domain.resultchecker;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record Player(String playerId, Set<Integer> numbers, Set<Integer> guessedNumbers, LocalDateTime drawDate,
                     boolean isWinner) {
}
