package org.example.domain.winningnumbersgenerator;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record WinningNumbers(String winningNumbersId, LocalDateTime drawDate, Set<Integer> winningNumbers) {
}
