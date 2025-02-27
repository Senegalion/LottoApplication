package org.example.domain.winningnumbersgenerator;

import org.example.domain.numberreceiver.NumberReceiverFacade;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NumberGeneratorFacadeTest {
    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final OneRandomNumberFetcher randomOneNumberFetcher = new SecureRandomOneNumberFetcher();

    @Test
    public void shouldReturnSetOfRequiredSize() {
        RandomNumbersGenerable generator = new RandomNumbersGenerator(randomOneNumberFetcher);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade winningNumbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        WinningNumbersDto generatedNumbers = winningNumbersGenerator.generateWinningNumbers();

        assertThat(generatedNumbers.winningNumbers().size()).isEqualTo(WinningNumbersInfo.SIZE.number);
    }

    @Test
    public void shouldReturnSetOfRequiredSizeWithinRequiredRange() {
        RandomNumbersGenerable generator = new RandomNumbersGenerator(randomOneNumberFetcher);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade winningNumbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        WinningNumbersDto generatedWinningNumbersDto = winningNumbersGenerator.generateWinningNumbers();

        Set<Integer> winningNumbers = generatedWinningNumbersDto.winningNumbers();
        boolean numbersInRange = winningNumbers.stream()
                .allMatch(number -> number >= WinningNumbersInfo.MINIMUM_VALUE.number
                        && number <= WinningNumbersInfo.MAXIMUM_VALUE.number);
        assertThat(numbersInRange).isTrue();

    }

    @Test
    public void shouldThrowExceptionWhenNumberNotInRange() {
        Set<Integer> numbers = Set.of(-1, 2, 3, 4, 5, 200);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl(numbers);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade winningNumbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        assertThrows(IllegalStateException.class, winningNumbersGenerator::generateWinningNumbers, "Number out of range!");
    }

    @Test
    public void shouldReturnCollectionOfUniqueValues() {
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        WinningNumbersDto generatedWinningNumbersDto = numbersGenerator.generateWinningNumbers();

        int generatedNumbersSize = new HashSet<>(generatedWinningNumbersDto.winningNumbers()).size();
        assertThat(generatedNumbersSize).isEqualTo(WinningNumbersInfo.SIZE.number);
    }

    @Test
    public void shouldReturnWinningNumbersByGivenDate() {

        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String winningNumbersId = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .winningNumbersId(winningNumbersId)
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        WinningNumbersDto winningNumbersDto = numbersGenerator.retrieveWinningNumberByDate(drawDate);

        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersDto);
    }

    @Test
    public void shouldThrowExceptionWhenFailToRetrieveNumbersByGivenDate() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersGeneratorFacade.retrieveWinningNumberByDate(drawDate), "Not Found");
    }

    @Test
    public void shouldReturnTrueIfNumbersAreGeneratedByGivenDate() {
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String winningNumbersId = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .winningNumbersId(winningNumbersId)
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);

        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDrawDate();

        assertTrue(areWinningNumbersGeneratedByDate);

    }
}
