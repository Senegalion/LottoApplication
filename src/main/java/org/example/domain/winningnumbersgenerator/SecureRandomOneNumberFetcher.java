package org.example.domain.winningnumbersgenerator;

import org.example.domain.winningnumbersgenerator.dto.OneRandomNumberResponseDto;

import java.security.SecureRandom;
import java.util.Random;

public class SecureRandomOneNumberFetcher implements OneRandomNumberFetcher {
    @Override
    public OneRandomNumberResponseDto retrieveOneRandomNumber(int minNumber, int maxNumber) {
        Random random = new SecureRandom();
        return OneRandomNumberResponseDto.builder()
                .number(random.nextInt((maxNumber - minNumber) + 1))
                .build();
    }
}
