package org.example.domain.winningnumbersgenerator;

import lombok.AllArgsConstructor;
import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {
    private WinningNumberValidator winningNumberValidator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerable randomNumbersGenerator;
    private WinningNumbersRepository winningNumbersRepository;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = randomNumbersGenerator.generateNumbers();
        winningNumberValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                .drawDate(nextDrawDate)
                .winningNumbers(winningNumbers)
                .build());
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumberByDate(LocalDateTime date) {
        WinningNumbers winningNumbers = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not Found"));
        return WinningNumbersMapper.mapFromWinningNumbers(winningNumbers);
    }

    public boolean areWinningNumbersGeneratedByDrawDate() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }
}
