package org.example.domain.winningnumbersgenerator;

import org.example.domain.winningnumbersgenerator.dto.OneRandomNumberResponseDto;

public interface OneRandomNumberFetcher {
    OneRandomNumberResponseDto retrieveOneRandomNumber(int lowerBand, int upperBand);
}
