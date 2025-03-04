package org.example.infrastructure.winningnumbersgenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class WinningNumbersGeneratorScheduler {
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Scheduled(cron = "*/10 * * * * *")
    public void f() {
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        log.info(winningNumbersDto.winningNumbers().toString());
        log.info(winningNumbersDto.drawDate().toString());
    }
}
