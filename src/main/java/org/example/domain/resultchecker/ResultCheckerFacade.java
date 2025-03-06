package org.example.domain.resultchecker;

import lombok.AllArgsConstructor;
import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.numberreceiver.dto.TicketDto;
import org.example.domain.resultchecker.dto.PlayersDto;
import org.example.domain.resultchecker.dto.ResultDTO;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ResultCheckerFacade {
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    private final PlayerRepository playerRepository;
    private final WinnersRetriever winnersRetriever;

    public PlayersDto generateWinners() {
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDto(allTicketsByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();

        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayersDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }

        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);
        return PlayersDto.builder()
                .results(ResultCheckerMapper.mapPlayersToResults(players))
                .message("Winners succeeded to retrieve")
                .build();
    }

    public ResultDTO findById(String id) {
        Player player = playerRepository.findByPlayerId(id)
                .orElseThrow(() -> new PlayerNotFoundException(
                        "Player with id: [%s] has not been found in the database", id)
                );

        return ResultDTO.builder()
                .resultId(player.playerId())
                .numbers(player.numbers())
                .guessedNumbers(player.guessedNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }
}
