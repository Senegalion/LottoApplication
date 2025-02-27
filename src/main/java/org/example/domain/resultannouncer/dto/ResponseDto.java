package org.example.domain.resultannouncer.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResponseDto(String id, Set<Integer> numbers, Set<Integer> guessedNumbers, LocalDateTime drawDate,
                          boolean isWinner) {
}
