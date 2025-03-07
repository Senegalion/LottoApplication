package org.example.infrastructure.numberreceiver.controller;

import java.util.Set;

public record InputNumbersRequestDto(
        Set<Integer> inputNumbers
) {
}
