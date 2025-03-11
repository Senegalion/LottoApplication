package org.example.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseIntegrationTest;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.WinningNumbersNotFoundException;
import org.example.domain.winningnumbersgenerator.dto.WinningNumbersDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    public static final String WINNING_NUMBERS_ENDPOINT = "/winningNumbers";
    public static final String RESULTS_ENDPOINT = "/results/";
    public static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";
    public static final int NUMBER_OF_WINNING_NUMBERS = 6;

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
        // step 2: system fetched winning numbers for draw date: 8.03.2025 10:00
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
        ResultActions performPostNumber = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                .content("""
                        {
                        "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult mvcResult = performPostNumber.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);

        // then
        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(numberReceiverResponseDto.ticketDto().ticketId()).isNotNull(),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("SUCCESS")
        );

        // step 4: user made GET /results/nonExistingId and system returned 404(NOT_FOUND) and body with (message: Not found for id: nonExistingId and status NOT_FOUND)
        // given
        // when
        String nonExistingId = "nonExistingId";
        ResultActions performGetResultsWithNotExistingId = mockMvc.perform(get(RESULTS_ENDPOINT + nonExistingId));

        //then
        performGetResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message" : "Player with id: [nonExistingId] has not been found in the database",
                        "status": "NOT_FOUND"
                        }
                        """.trim()
                ));

        // step 5: 3 days and 55 minute passed, and it is 5 minute before the draw date (8.03.2025 11:55)
        log.info(clock.toString());
        clock.plusDaysAndMinutes(3, 55);
        log.info(clock.toString());

        // step 6: system generated result for TicketId: sampleTicketId with draw date 8.03.2025 12:00, and saved it with 6 hits
        // given
        // when
        ResultActions performGettingWinningNumbers = mockMvc.perform(get(WINNING_NUMBERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResultGettingWinningNumbers = performGettingWinningNumbers.andExpect(status().isOk()).andReturn();
        String jsonGettingWinningNumbers = mvcResultGettingWinningNumbers.getResponse().getContentAsString();
        WinningNumbersDto winningNumbersDto = objectMapper.readValue(jsonGettingWinningNumbers, WinningNumbersDto.class);

        //then
        assertAll(
                () -> assertThat(winningNumbersDto.drawDate()).isEqualTo(drawDate),
                () -> assertThat(winningNumbersDto.winningNumbers()).hasSize(NUMBER_OF_WINNING_NUMBERS),
                () -> assertThat(winningNumbersDto.winningNumbers()).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6)
        );

        // step 7: 6 minutes passed, and it is 1 minute after announcement time (8.03.2025 12:01)
        // step 8: user made GET /results/sampleTicketId and system returned 200 (OK)

    }
}
