package org.example.domain.numberreceiver;

import org.example.domain.AdjustableClock;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {
    Clock clock = Clock.systemUTC();
    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();

    IdGenerable idGenerator = new IdGenerator();
    NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(
            idGenerator,
            clock,
            ticketRepository
    );
    DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);

    @Test
    public void shouldReturnSuccessResponseWhenUserGaveExactlySixCorrectNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        TicketDto generatedTicket = TicketDto.builder()
                .ticketId(response.ticketDto().ticketId())
                .drawDate(nextDrawDate)
                .numbers(numbers)
                .build();

        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFailedWhenUserGaveLessThanSixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFailedWhenUserGaveMoreThanSixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFailedWhenUserGaveTooBigNumber() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 105);

        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFailedWhenUserGaveTooSmallNumber() {
        Set<Integer> numbers = Set.of(-1, 2, 3, 4, 5, 6);

        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnSaveToDatabaseWhenUserGaveExactlySixNumbers() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverResponseDto result = numberReceiverFacade.inputNumbers(numbers);
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        List<TicketDto> ticketDtos = numberReceiverFacade.getUserNumbers(drawDate);
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketDto().ticketId())
                        .drawDate(result.ticketDto().drawDate())
                        .numbers(result.ticketDto().numbers())
                        .build()
        );
    }

    @Test
    public void shouldReturnCorrectDrawDate() {
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 2, 26, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(idGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2025, 3, 1, 12, 0, 0);
        assertThat(expectedDrawDate).isEqualTo(testedDrawDate);
    }

    @Test
    public void shouldReturnNextDrawDateWhenThereIsExactTimeOfTheDraw() {
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 3, 1, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(idGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2025, 3, 8, 12, 0, 0);

        assertThat(expectedDrawDate).isEqualTo(testedDrawDate);
    }

    @Test
    public void shouldReturnTicketsWithCorrectDrawDate() {
        Instant fixedInstant = LocalDateTime.of(2025, 2, 26, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(idGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        TicketDto ticketDto2 = numberReceiverResponseDto2.ticketDto();
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1, ticketDto2);
    }

    @Test
    public void shouldReturnTicketsForTheNextDrawDate() {
        Instant fixedInstant = LocalDateTime.of(2025, 2, 26, 11, 50, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(idGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        TicketDto ticketDto2 = numberReceiverResponseDto2.ticketDto();
        TicketDto ticketDto3 = numberReceiverResponseDto3.ticketDto();

        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();

        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1, ticketDto2, ticketDto3);
    }

    @Test
    public void shouldReturnEmptyCollectionsIfThereAreNoTickets() {
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 2, 26, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(idGenerator, clock, ticketRepository);
        LocalDateTime drawDate = LocalDateTime.now(clock);

        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void shouldReturnEmptyCollectionsIfDateIsAfterNextDrawDate() {
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 3, 2, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(idGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));

        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void shouldReturnParticularTicketByTheTicketId() {
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);

        TicketDto expectedTicketDto = numberReceiverFacade.inputNumbers(numbers).ticketDto();
        String ticketId = expectedTicketDto.ticketId();
        TicketDto ticketDto = numberReceiverFacade.findByTicketId(ticketId);

        assertThat(expectedTicketDto).isEqualTo(ticketDto);
    }

    @Test
    public void shouldThrowExceptionWhenTicketIsNull() {
        TicketDto expectedTicketDto = null;

        Assertions.assertThrows(RuntimeException.class, () -> numberReceiverFacade.findByTicketId(expectedTicketDto.ticketId()));
    }

    @Test
    public void shouldThrowExceptionWhenTicketDoesNotExist() {
        String ticketId = "Some ticket Id";

        Assertions.assertThrows(RuntimeException.class, () -> numberReceiverFacade.findByTicketId(ticketId));
    }
}