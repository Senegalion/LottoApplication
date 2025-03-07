package org.example.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.BaseIntegrationTest;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.WinningNumbersNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Test
    public void should_user_win_and_system_should_generate_winners() throws Exception {
        // step 1: external service returns 6 random numbers (1,2,3,4,5,6)
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                          """.trim()
                        )));
        // step 2: system fetched winning numbers for draw date: 8.03.2025 12:00
        LocalDateTime drawDate = LocalDateTime.of(2025, 3, 8, 12, 0, 0);
        await()
                .atMost(20, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !winningNumbersGeneratorFacade
                                .retrieveWinningNumberByDate(drawDate)
                                .winningNumbers()
                                .isEmpty();
                    } catch (WinningNumbersNotFoundException exception) {
                        return false;
                    }
                });

        // step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 5-03-2025 12:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:8.03.2025 12:00 (Saturday), TicketId: sampleTicketId)
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);

        // then
        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(numberReceiverResponseDto.ticketDto().ticketId()).isNotNull(),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("SUCCESS")
        );

        // step 4: 3 days and 1 minute passed, and it is 1 minute after the draw date (8.03.2025 12:01)
        clock.plusDaysAndMinutes(3, 1);

        // step 5: system generated result for TicketId: sampleTicketId with draw date 8.03.2025 12:00, and saved it with 6 hits
        // step 6: 3 hours passed, and it is 1 minute after announcement time (8.03.2025 15:01)
        // step 7: user made GET /results/sampleTicketId and system returned 200 (OK)

    }
}
