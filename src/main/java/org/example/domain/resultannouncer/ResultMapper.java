package org.example.domain.resultannouncer;

import org.example.domain.resultannouncer.dto.ResponseDto;

public class ResultMapper {
    public static ResponseDto mapToDto(ResultResponse resultResponse) {
        return ResponseDto.builder()
                .id(resultResponse.id())
                .numbers(resultResponse.numbers())
                .guessedNumbers(resultResponse.guessedNumbers())
                .drawDate(resultResponse.drawDate())
                .isWinner(resultResponse.isWinner())
                .build();
    }
}
