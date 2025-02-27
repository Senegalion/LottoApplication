package org.example.domain.resultannouncer;

import org.example.domain.resultannouncer.dto.ResponseDto;
import org.example.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import org.example.domain.resultchecker.ResultCheckerFacade;
import org.example.domain.resultchecker.dto.ResultDTO;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.domain.resultannouncer.ResponseMessage.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultAnnouncerFacadeTest {
    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void shouldReturnResponseWithLoseMessageIfTicketIsNotWinningTicket() {
        String id = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDTO resultDto = InputData.getResultDto(id);
        when(resultCheckerFacade.findById(id)).thenReturn(resultDto);

        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(id);

        ResponseDto responseDto = InputData.getResponseDto(id);
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnResponseWithWinMessageIfTicketIsWinningTicket() {
        String id = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDTO resultDto = InputData.getResultDto1(id);
        when(resultCheckerFacade.findById(id)).thenReturn(resultDto);

        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(id);

        ResponseDto responseDto = InputData.getResponseDto1(id);
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnResponseWithWaitMessageIfDateIsBeforeAnnouncementTime() {
        String id = "123";
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 2, 1, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .createForTest(resultCheckerFacade, responseRepository, clock);
        ResultDTO resultDto = InputData.getResultDto2(id);
        when(resultCheckerFacade.findById(id)).thenReturn(resultDto);

        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(id);

        ResponseDto responseDto = InputData.getResponseDto2(id);
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnResponseWithIdDoesNotExistMessageIfIdDoesNotExist() {
        String id = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        when(resultCheckerFacade.findById(id)).thenReturn(null);

        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(id);

        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, ID_DOES_NOT_EXIST_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnResponseWithIdDoesNotExistMessageIfResponseHasNotBeenSavedToTheDatabaseYet() {
        String id = "123";
        ResultDTO resultDto = InputData.getResultDto2(id);
        when(resultCheckerFacade.findById(id)).thenReturn(resultDto);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultAnnouncerResponseDto resultAnnouncerResponseDto1 = resultAnnouncerFacade.checkResult(id);
        String checkedId = resultAnnouncerResponseDto1.responseDto().id();

        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(checkedId);

        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(
                resultAnnouncerResponseDto.responseDto(), ALREADY_CHECKED.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }
}