package org.example.domain.winningnumbersgenerator;

import lombok.AllArgsConstructor;
import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.winningnumbersgenerator.dto.SixRandomNumbersDto;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {
    private WinningNumberValidator winningNumberValidator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;
    private WinningNumbersRepository winningNumbersRepository;
    private final WinningNumbersGeneratorFacadeConfigurationProperties properties;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        SixRandomNumbersDto sixRandomNumbersDto = randomNumbersGenerable
                .generateNumbers(
                        properties.numberOfNumbers(),
                        properties.minimumNumber(),
                        properties.maximumNumber()
                );
        Set<Integer> winningNumbers = sixRandomNumbersDto.numbers();
        winningNumberValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                .drawDate(nextDrawDate)
                .winningNumbers(winningNumbers)
                .build());
        return WinningNumbersDto.builder()
                .drawDate(nextDrawDate)
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
