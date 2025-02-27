package org.example.domain.resultannouncer;

import lombok.AllArgsConstructor;
import org.example.domain.resultannouncer.dto.ResponseDto;
import org.example.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import org.example.domain.resultchecker.ResultCheckerFacade;
import org.example.domain.resultchecker.dto.ResultDTO;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.example.domain.resultannouncer.ResponseMessage.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    public static final LocalTime RESULTS_ANNOUNCEMENT_TIME = LocalTime.of(12, 0).plusMinutes(5);
    private final ResultCheckerFacade resultCheckerFacade;
    private final ResponseRepository responseRepository;
    private final Clock clock;

    public ResultAnnouncerResponseDto checkResult(String id) {
        if (responseRepository.existsById(id)) {
            Optional<ResultResponse> resultResponseCached = responseRepository.findById(id);
            if (resultResponseCached.isPresent()) {
                return new ResultAnnouncerResponseDto(ResultMapper.mapToDto(resultResponseCached.get()), ALREADY_CHECKED.info);
            }
        }
        ResultDTO resultDto = resultCheckerFacade.findById(id);
        if (resultDto == null) {
            return new ResultAnnouncerResponseDto(null, ID_DOES_NOT_EXIST_MESSAGE.info);
        }
        ResponseDto responseDto = buildResponseDto(resultDto);
        responseRepository.save(buildResponse(responseDto));
        if (responseRepository.existsById(id) && !isAfterResultAnnouncementTime(resultDto)) {
            return new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        }
        if (resultCheckerFacade.findById(id).isWinner()) {
            return new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        }
        return new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
    }

    private boolean isAfterResultAnnouncementTime(ResultDTO resultDto) {
        LocalDateTime announcementDateTime = LocalDateTime.of(resultDto.drawDate().toLocalDate(), RESULTS_ANNOUNCEMENT_TIME);
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }

    private ResultResponse buildResponse(ResponseDto responseDto) {
        return ResultResponse.builder()
                .id(responseDto.id())
                .numbers(responseDto.numbers())
                .guessedNumbers(responseDto.guessedNumbers())
                .drawDate(responseDto.drawDate())
                .isWinner(responseDto.isWinner())
                .build();
    }

    private ResponseDto buildResponseDto(ResultDTO resultDto) {
        return ResponseDto.builder()
                .id(resultDto.resultId())
                .numbers(resultDto.numbers())
                .guessedNumbers(resultDto.guessedNumbers())
                .drawDate(resultDto.drawDate())
                .isWinner(resultDto.isWinner())
                .build();
    }
}
