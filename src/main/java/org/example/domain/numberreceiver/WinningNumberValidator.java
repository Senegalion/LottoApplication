package org.example.domain.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class WinningNumberValidator {
    public static final int MINIMUM_NUMBER = 1;
    public static final int MAXIMUM_NUMBER = 99;
    public static final int SIZE = 6;

    List<ValidationResult> errors = new LinkedList<>();

    public List<ValidationResult> filterNumbers(Set<Integer> numbers) {
        if (!isNumbersSizeCorrect(numbers)) {
            errors.add(ValidationResult.NOT_SIX_NUMBERS_GIVEN);
        }
        if (!isNumberInRange(numbers)) {
            errors.add(ValidationResult.NOT_IN_RANGE);
        }
        return errors;
    }

    String createResultMessage() {
        return this.errors
                .stream()
                .map(validationResult -> validationResult.info)
                .collect(Collectors.joining(","));
    }

    private boolean isNumbersSizeCorrect(Set<Integer> numbersFromUser) {
        return numbersFromUser.size() == SIZE;
    }

    boolean isNumberInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .allMatch(number -> number >= MINIMUM_NUMBER && number <= MAXIMUM_NUMBER);
    }
}
