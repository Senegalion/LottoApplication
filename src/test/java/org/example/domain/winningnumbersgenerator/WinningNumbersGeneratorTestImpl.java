package org.example.domain.winningnumbersgenerator;

import org.example.domain.winningnumbersgenerator.dto.SixRandomNumbersDto;

import java.util.Set;

public class WinningNumbersGeneratorTestImpl implements RandomNumbersGenerable {

    private final Set<Integer> generatedNumbers;

    WinningNumbersGeneratorTestImpl() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    WinningNumbersGeneratorTestImpl(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public SixRandomNumbersDto generateNumbers(int numberOfNumbers, int minimumNumber, int maximumNumber) {
        return SixRandomNumbersDto.builder()
                .numbers(generatedNumbers)
                .build();
    }
}
