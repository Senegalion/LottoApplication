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
    public static final String EXAMPLE_ID = "123";
    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void should_return_response_with_lose_message_if_ticket_is_not_winning_ticket() {
        // given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDTO resultDto = InputData.getResultDto(EXAMPLE_ID);
        when(resultCheckerFacade.findById(EXAMPLE_ID)).thenReturn(resultDto);

        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(EXAMPLE_ID);

        // then
        ResponseDto responseDto = InputData.getResponseDto(EXAMPLE_ID);
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_win_message_if_ticket_is_winning_ticket() {
        // given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDTO resultDto = InputData.getResultDto1(EXAMPLE_ID);
        when(resultCheckerFacade.findById(EXAMPLE_ID)).thenReturn(resultDto);

        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(EXAMPLE_ID);

        // then
        ResponseDto responseDto = InputData.getResponseDto1(EXAMPLE_ID);
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_wait_message_if_date_is_before_announcement_time() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 2, 1, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade, responseRepository, clock);
        ResultDTO resultDto = InputData.getResultDto2(EXAMPLE_ID);
        when(resultCheckerFacade.findById(EXAMPLE_ID)).thenReturn(resultDto);

        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(EXAMPLE_ID);

        // then
        ResponseDto responseDto = InputData.getResponseDto2(EXAMPLE_ID);
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_id_does_not_exist_message_if_id_does_not_exist() {
        // given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        when(resultCheckerFacade.findById(EXAMPLE_ID)).thenReturn(null);

        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(EXAMPLE_ID);

        // then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, ID_DOES_NOT_EXIST_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_id_does_not_exist_message_if_response_has_not_been_saved_to_the_database_yet() {
        // given
        ResultDTO resultDto = InputData.getResultDto2(EXAMPLE_ID);
        when(resultCheckerFacade.findById(EXAMPLE_ID)).thenReturn(resultDto);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration()
                .resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultAnnouncerResponseDto resultAnnouncerResponseDto1 = resultAnnouncerFacade.checkResult(EXAMPLE_ID);
        String checkedId = resultAnnouncerResponseDto1.responseDto().id();

        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(checkedId);

        // then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(
                resultAnnouncerResponseDto.responseDto(), ALREADY_CHECKED.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }
}