package org.example.domain.numberreceiver;

import org.example.domain.drawdateretriever.DrawDateRetrieverFacade;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NumberReceiverFacadeTest {
    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    DrawDateRetrieverFacade drawDateRetrieverFacade = mock(DrawDateRetrieverFacade.class);
    NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(
            ticketRepository,
            drawDateRetrieverFacade
    );

    @Test
    public void should_return_success_response_when_user_gave_exactly_six_correct_numbers() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.of(2025, 3, 8, 12, 0, 0);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        // then
        TicketDto generatedTicket = TicketDto.builder()
                .ticketId(response.ticketDto().ticketId())
                .drawDate(response.ticketDto().drawDate())
                .numbers(numbers)
                .build();
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_too_big_number() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 105);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_when_user_gave_too_small_number() {
        // given
        Set<Integer> numbers = Set.of(-1, 2, 3, 4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_save_to_database_when_user_gave_exactly_six_numbers() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.of(2025, 3, 8, 12, 0, 0);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);

        // when
        NumberReceiverResponseDto result = numberReceiverFacade.inputNumbers(numbers);
        List<TicketDto> ticketDtos = numberReceiverFacade.getUserNumbers(drawDate);

        // then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketDto().ticketId())
                        .drawDate(result.ticketDto().drawDate())
                        .numbers(result.ticketDto().numbers())
                        .build()
        );
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        // given
        Instant fixedInstant = LocalDateTime.of(2025, 3, 5, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        LocalDateTime correctDrawDate = LocalDateTime.of(2025, 3, 8, 12, 0, 0);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(correctDrawDate);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(ticketRepository, drawDateRetrieverFacade);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        correctDrawDate = LocalDateTime.of(2025, 3, 15, 12, 0, 0);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(correctDrawDate);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        TicketDto ticketDto2 = numberReceiverResponseDto2.ticketDto();
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1, ticketDto2);
    }

    @Test
    public void should_return_tickets_for_the_next_draw_date() {
        // given
        Instant fixedInstant = LocalDateTime.of(2025, 2, 26, 11, 50, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        LocalDateTime drawDate = LocalDateTime.now(clock);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(ticketRepository, drawDateRetrieverFacade);
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

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();

        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1, ticketDto2, ticketDto3);
    }

    @Test
    public void should_return_empty_collections_if_there_are_no_tickets() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 3, 8, 12, 0, 0);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(ticketRepository, drawDateRetrieverFacade);

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void should_return_empty_collections_if_date_is_after_next_draw_date() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 3, 9, 12, 0, 0);
        when(drawDateRetrieverFacade.retrieveNextDrawDate()).thenReturn(date);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(ticketRepository, drawDateRetrieverFacade);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        // when
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));

        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void should_return_particular_ticket_by_the_ticket_id() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);

        // when
        TicketDto expectedTicketDto = numberReceiverFacade.inputNumbers(numbers).ticketDto();
        String ticketId = expectedTicketDto.ticketId();
        TicketDto ticketDto = numberReceiverFacade.findByTicketId(ticketId);

        // then
        assertThat(expectedTicketDto).isEqualTo(ticketDto);
    }

    @Test
    public void should_throw_exception_when_ticket_is_null() {
        // given
        TicketDto expectedTicketDto = null;

        // when
        // then
        Assertions.assertThrows(RuntimeException.class, () -> numberReceiverFacade.findByTicketId(expectedTicketDto.ticketId()));
    }

    @Test
    public void should_throw_exception_when_ticket_does_not_exist() {
        // given
        String ticketId = "Some ticket Id";

        // when
        // then
        Assertions.assertThrows(RuntimeException.class, () -> numberReceiverFacade.findByTicketId(ticketId));
    }
}
