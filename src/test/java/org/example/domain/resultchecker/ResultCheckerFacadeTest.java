package org.example.domain.resultchecker;

import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.resultchecker.dto.PlayersDto;
import org.example.domain.resultchecker.dto.ResultDTO;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResultCheckerFacadeTest {
    private final PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = mock(WinningNumbersGeneratorFacade.class);
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_generate_all_players_with_correct_message() {
        // given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                InputData.retrieveAllTicketsByNextDrawDate()
        );

        // when
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .resultCheckerFacade(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        PlayersDto playersDto = resultCheckerFacade.generateWinners();

        // then
        List<ResultDTO> results = playersDto.results();
        ResultDTO resultDto = InputData.getResultDto();
        ResultDTO resultDto1 = InputData.getResultDto();
        ResultDTO resultDto2 = InputData.getResultDto();
        assertThat(results).contains(resultDto, resultDto1, resultDto2);
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners succeeded to retrieve");
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_equal_null() {
        // given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(null)
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .resultCheckerFacade(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        // when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();

        // then
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_is_empty() {
        // given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of())
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .resultCheckerFacade(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        // when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();

        // then
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void should_generate_result_by_player_id() {
        // given
        String id = "001";
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(LocalDateTime.of(2025, 3, 8, 12, 0, 0))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                InputData.retrieveAllTicketsByNextDrawDate1(id)
        );

        // when
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(
                numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        resultCheckerFacade.generateWinners();

        // then
        ResultDTO resultDto = resultCheckerFacade.findById(id);
        ResultDTO expectedResult = InputData.getResultDto1(id);
        assertThat(resultDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_throw_player_not_found_exception_when_player_with_id_has_not_been_retrieved_correctly() {
        // given
        String notExistingId = "002";
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                InputData.retrieveAllTicketsByNextDrawDate1(notExistingId));
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().resultCheckerFacade(
                numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        // when
        // then
        assertThrows(PlayerNotFoundException.class, () -> resultCheckerFacade.findById(notExistingId));
    }
}
