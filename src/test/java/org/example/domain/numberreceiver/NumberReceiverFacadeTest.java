package org.example.domain.numberreceiver;

import org.example.domain.AdjustableClock;
import org.example.domain.numberreceiver.dto.InputNumbersResultDto;
import org.example.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {
    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2024, 2, 25, 21, 30, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new NumberValidator(),
            new InMemoryNumberReceiverRepositoryTestImpl(),
            clock
    );

    @Test
    public void shouldReturnSuccessWhenUserGaveExactlySixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbers);
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    public void shouldReturnFailedWhenUserGaveLessThanSixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbers);
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void shouldReturnFailedWhenUserGaveMoreThanSixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbers);
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void shouldReturnFailedWhenUserGaveTooBigNumber() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 105);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbers);
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void shouldReturnFailedWhenUserGaveTooSmallNumber() {
        Set<Integer> numbers = Set.of(-1, 2, 3, 4, 5, 6);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbers);
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void shouldReturnSaveToDatabaseWhenUserGaveExactlySixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbers);
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 25, 23, 30, 0, 0);
        List<TicketDto> ticketDtos = numberReceiverFacade.getUserNumbers(drawDate);
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(result.drawDate())
                        .numbers(result.numbers())
                        .build()
        );
    }
}