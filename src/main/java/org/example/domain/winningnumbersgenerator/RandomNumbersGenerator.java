package org.example.domain.winningnumbersgenerator;

import org.example.domain.winningnumbersgenerator.dto.OneRandomNumberResponseDto;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomNumbersGenerator implements RandomNumbersGenerable {
    public static final int NUMBER_OF_WINNING_NUMBERS = WinningNumbersInfo.SIZE.number;
    private final int RANDOM_NUMBER_BOUND = (WinningNumbersInfo.MAXIMUM_VALUE.number - WinningNumbersInfo.MINIMUM_VALUE.number) + 1;

    private final OneRandomNumberFetcher client;

    RandomNumbersGenerator(OneRandomNumberFetcher client) {
        this.client = client;
    }

    @Override
    public Set<Integer> generateNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        while (!hasEnoughGeneratedWinningNumbers(winningNumbers)) {
            OneRandomNumberResponseDto randomNumberResponseDto = client.retrieveOneRandomNumber(WinningNumbersInfo.MINIMUM_VALUE.number, WinningNumbersInfo.MAXIMUM_VALUE.number);
            int randomNumber = randomNumberResponseDto.number();
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }

    private boolean hasEnoughGeneratedWinningNumbers(Set<Integer> winningNumbers) {
        return winningNumbers.size() >= NUMBER_OF_WINNING_NUMBERS;
    }

    private int generateRandom() {
        Random random = new SecureRandom();
        return random.nextInt(RANDOM_NUMBER_BOUND) + 1;
    }
}
