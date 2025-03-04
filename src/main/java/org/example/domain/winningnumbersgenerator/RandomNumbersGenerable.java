package org.example.domain.winningnumbersgenerator;

import org.example.domain.winningnumbersgenerator.dto.SixRandomNumbersDto;

public interface RandomNumbersGenerable {
    SixRandomNumbersDto generateNumbers(int numberOfNumbers, int minimumNumber, int maximumNumber);
}
