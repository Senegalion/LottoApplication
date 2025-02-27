package org.example.domain.resultannouncer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultResponse(String id, Set<Integer> numbers, Set<Integer> guessedNumbers, LocalDateTime drawDate,
                             boolean isWinner) {
}
