package org.example.domain.resultchecker.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultDTO(String resultId, Set<Integer> numbers, Set<Integer> guessedNumbers, LocalDateTime drawDate,
                        boolean isWinner) {
}
