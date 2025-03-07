package org.example.domain.winningnumbersgenerator;

import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
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
    DrawDateRetrieverFacade drawDateRetrieverFacade = mock(DrawDateRetrieverFacade.class);

    @Test
    public void should_return_set_of_required_size() {
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade winningNumbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        WinningNumbersDto generatedNumbers = winningNumbersGenerator.generateWinningNumbers();

        assertThat(generatedNumbers.winningNumbers().size()).isEqualTo(WinningNumbersInfo.SIZE.number);
    }

    @Test
    public void should_return_set_of_required_size_within_required_range() {
        // given
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade winningNumbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        // when
        WinningNumbersDto generatedWinningNumbersDto = winningNumbersGenerator.generateWinningNumbers();

        // then
        Set<Integer> winningNumbers = generatedWinningNumbersDto.winningNumbers();
        boolean numbersInRange = winningNumbers.stream()
                .allMatch(number -> number >= WinningNumbersInfo.MINIMUM_VALUE.number
                        && number <= WinningNumbersInfo.MAXIMUM_VALUE.number);
        assertThat(numbersInRange).isTrue();

    }

    @Test
    public void should_throw_exception_when_number_not_in_range() {
        // given
        Set<Integer> numbers = Set.of(-1, 2, 3, 4, 5, 200);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl(numbers);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade winningNumbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        // when
        // then
        assertThrows(IllegalStateException.class, winningNumbersGenerator::generateWinningNumbers, "Number out of range!");
    }

    @Test
    public void should_return_collection_of_unique_values() {
        // given
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        // when
        WinningNumbersDto generatedWinningNumbersDto = numbersGenerator.generateWinningNumbers();

        // then
        int generatedNumbersSize = new HashSet<>(generatedWinningNumbersDto.winningNumbers()).size();
        assertThat(generatedNumbersSize).isEqualTo(WinningNumbersInfo.SIZE.number);
    }

    @Test
    public void should_return_winning_numbers_by_given_date() {
        // given
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
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        // when
        WinningNumbersDto winningNumbersDto = numbersGenerator.retrieveWinningNumberByDate(drawDate);

        // then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersDto);
    }

    @Test
    public void should_throw_exception_when_fail_to_retrieve_numbers_by_given_date() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 2, 27, 12, 0, 0);
        RandomNumbersGenerable generator = new WinningNumbersGeneratorTestImpl();
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        // when
        // then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersGeneratorFacade.retrieveWinningNumberByDate(drawDate), "Not Found");
    }

    @Test
    public void should_return_true_if_numbers_are_generated_by_given_date() {
        // given
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
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new WinningNumbersGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateRetrieverFacade);

        // when
        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDrawDate();

        // then
        assertTrue(areWinningNumbersGeneratedByDate);

    }
}
