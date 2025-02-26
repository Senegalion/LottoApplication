package org.example.domain.winningnumbersgenerator;

enum WinningNumbersInfo {
    SIZE(6),
    MINIMUM_VALUE(1),
    MAXIMUM_VALUE(99);

    final int number;

    WinningNumbersInfo(int number) {
        this.number = number;
    }
}
