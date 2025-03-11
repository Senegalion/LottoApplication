package org.example.infrastructure.resultchecker.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.resultchecker.ResultCheckerFacade;
import org.example.domain.resultchecker.dto.PlayersDto;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ResultCheckerScheduler {
    private final ResultCheckerFacade resultCheckerFacade;
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Scheduled(cron = "${lotto.result-checker.lotteryRunOccurrence}")
    public PlayersDto getResults() {
        if (!winningNumbersGeneratorFacade.areWinningNumbersGeneratedByDrawDate()) {
            log.error("Winning numbers are not generated");
            throw new RuntimeException("Winning numbers are not generated");
        }
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        log.info(playersDto.toString());
        return playersDto;
    }
}
