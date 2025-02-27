package org.example.domain.resultannouncer;

import org.example.domain.resultannouncer.dto.ResponseDto;
import org.example.domain.resultchecker.dto.ResultDTO;

import java.time.LocalDateTime;
import java.util.Set;

public class InputData {
    public static ResultDTO getResultDto(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResultDTO.builder()
                .resultId(id)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
    }

    public static ResponseDto getResponseDto(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResponseDto.builder()
                .id(id)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
    }

    public static ResultDTO getResultDto1(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResultDTO.builder()
                .resultId(id)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

    public static ResponseDto getResponseDto1(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResponseDto.builder()
                .id(id)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

    public static ResultDTO getResultDto2(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResultDTO.builder()
                .resultId(id)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

    public static ResponseDto getResponseDto2(String id) {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        return ResponseDto.builder()
                .id(id)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .guessedNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }
}
