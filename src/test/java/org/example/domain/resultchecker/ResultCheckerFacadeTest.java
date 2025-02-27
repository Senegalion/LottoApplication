package org.example.domain.resultchecker;

import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.resultchecker.dto.PlayersDto;
import org.example.domain.resultchecker.dto.ResultDTO;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;
import org.junit.jupiter.api.Test;

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
    public void shouldGenerateAllPlayersWithCorrectMessage() {
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                InputData.retrieveAllTicketsByNextDrawDate()
        );

        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        PlayersDto playersDto = resultCheckerFacade.generateWinners();

        List<ResultDTO> results = playersDto.results();
        ResultDTO resultDto = InputData.getResultDto();
        ResultDTO resultDto1 = InputData.getResultDto();
        ResultDTO resultDto2 = InputData.getResultDto();
        assertThat(results).contains(resultDto, resultDto1, resultDto2);
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners succeeded to retrieve");
    }

    @Test
    public void shouldGenerateFailMessageWhenWinningNumbersEqualNull() {
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(null)
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        PlayersDto playersDto = resultCheckerFacade.generateWinners();

        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void shouldGenerateFailMessageWhenWinningNumbersIsEmpty() {
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of())
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration()
                .createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        PlayersDto playersDto = resultCheckerFacade.generateWinners();

        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void shouldGenerateResultByPlayerId() {
        String id = "001";
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                InputData.retrieveAllTicketsByNextDrawDate1(id)
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createForTest(
                numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        resultCheckerFacade.generateWinners();

        ResultDTO resultDto = resultCheckerFacade.findById(id);

        ResultDTO expectedResult = InputData.getResultDto1(id);
        assertThat(resultDto).isEqualTo(expectedResult);
    }

    @Test
    public void shouldThrowPlayerNotFoundExceptionWhenPlayerWithIdHasNotBeenRetrievedCorrectly() {
        String notExistingId = "002";
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                InputData.retrieveAllTicketsByNextDrawDate1(notExistingId));
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createForTest(
                numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);

        assertThrows(PlayerNotFoundException.class, () -> resultCheckerFacade.findById(notExistingId));
    }
}
