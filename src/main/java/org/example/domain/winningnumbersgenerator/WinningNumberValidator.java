package org.example.domain.winningnumbersgenerator;

import java.util.Set;

public class WinningNumberValidator {
    private final int MINIMUM_NUMBER = 1;
    private final int MAXIMUM_NUMBER = 99;

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
