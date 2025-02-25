package org.example.domain.numberreceiver;

import java.util.Set;

class NumberValidator {
    public static final int MINIMUM_NUMBER = 1;
    public static final int MAXIMUM_NUMBER = 99;
    public static final int SIZE = 6;

    public boolean filterNumbers(Set<Integer> numbers) {
        return numbers.stream()
                .filter(number -> number >= MINIMUM_NUMBER)
                .filter(number -> number <= MAXIMUM_NUMBER)
                .count() == SIZE;
    }
}
