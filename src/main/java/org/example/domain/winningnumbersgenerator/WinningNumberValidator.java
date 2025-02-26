package org.example.domain.winningnumbersgenerator;

import java.util.Set;

public class WinningNumberValidator {
    private final int MINIMUM_NUMBER = WinningNumbersInfo.MINIMUM_VALUE.number;
    private final int MAXIMUM_NUMBER = WinningNumbersInfo.MAXIMUM_VALUE.number;

    public Set<Integer> validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            throw new IllegalStateException("Number out of range!");
        }
        return winningNumbers;
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < MINIMUM_NUMBER || number > MAXIMUM_NUMBER);
    }
}
