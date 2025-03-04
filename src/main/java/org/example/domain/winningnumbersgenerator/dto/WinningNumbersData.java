package org.example.domain.winningnumbersgenerator.dto;

import lombok.Getter;

@Getter
public enum WinningNumbersData {
    NUMBER_OF_NUMBERS(6),
    MAXIMUM_VALUE(99),
    MINIMUM_VALUE(1);

    private final int number;

    WinningNumbersData(int number) {
        this.number = number;
    }
}
