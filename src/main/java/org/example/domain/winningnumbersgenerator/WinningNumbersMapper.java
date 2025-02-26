package org.example.domain.winningnumbersgenerator;

import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;

public class WinningNumbersMapper {
    public static WinningNumbersDto mapFromWinningNumbers(WinningNumbers winningNumbers) {
        return WinningNumbersDto.builder()
                .drawDate(winningNumbers.drawDate())
                .winningNumbers(winningNumbers.winningNumbers())
                .build();
    }
}
