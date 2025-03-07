package org.example.domain.drawdateretriever;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class DrawDateRetrieverFacadeTest {
    @Test
    public void should_return_correct_draw_date() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 2, 26, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        DrawDateRetrieverFacade drawDateRetrieverFacade = new DrawDateRetrieverConfiguration().drawDateRetrieverFacade(clock);

        // when
        LocalDateTime testedDrawDate = drawDateRetrieverFacade.retrieveNextDrawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2025, 3, 1, 12, 0, 0);
        assertThat(expectedDrawDate).isEqualTo(testedDrawDate);
    }

    @Test
    public void should_return_next_draw_date_when_there_is_exact_time_of_the_draw() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2025, 3, 8, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        DrawDateRetrieverFacade drawDateRetrieverFacade = new DrawDateRetrieverConfiguration().drawDateRetrieverFacade(clock);

        // when
        LocalDateTime testedDrawDate = drawDateRetrieverFacade.retrieveNextDrawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2025, 3, 15, 12, 0, 0);
        assertThat(expectedDrawDate).isEqualTo(testedDrawDate);
    }
}