package org.example.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseIntegrationTest;
import org.example.domain.loginandregister.dto.RegistrationResultDto;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import org.example.domain.resultchecker.PlayerNotFoundException;
import org.example.domain.resultchecker.ResultCheckerFacade;
import org.example.domain.winningnumbersgenerator.WinningNumbersGeneratorFacade;
import org.example.domain.winningnumbersgenerator.WinningNumbersNotFoundException;
import org.example.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {
    public static final String RESULTS_ENDPOINT = "/results/";
    public static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";
    public static final String TOKEN = "/token";
    public static final String REGISTER = "/register";

    @Autowired
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    @Autowired
    ResultCheckerFacade resultCheckerFacade;
    @Container
    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("lotto.number-generator.http.client.config.port", () -> wireMockServer.getPort());
        registry.add("lotto.number-generator.http.client.config.uri", () -> WIRE_MOCK_HOST);
    }

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

        // step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given
        // when
        ResultActions failedPerformToGetJWTToken = mockMvc.perform(post(TOKEN)
                .content("""
                        {
                        "username" : "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        failedPerformToGetJWTToken
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                        "message" : "Bad credentials",
                        "status": "UNAUTHORIZED"
                        }
                        """.trim()
                ));

        // step 4: user made /results/ticketId with no jwt token and system returned UNAUTHORIZED(401)
        // given
        String someTicketId = "someId";

        // when
        // then
        mockMvc.perform(get(RESULTS_ENDPOINT + someTicketId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        // given
        // when
        ResultActions performRegister = mockMvc.perform(post(REGISTER)
                .content("""
                        {
                        "username" : "someUser",
                        "password": "somePassword"
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResultRegister = performRegister.andExpect(status().isCreated()).andReturn();
        String jsonRegister = mvcResultRegister.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(jsonRegister, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.userId()).isNotNull(),
                () -> assertThat(registrationResultDto.wasCreated()).isTrue(),
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser")
        );

        // step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given
        // when
        ResultActions performSuccessLogin = mockMvc.perform(post(TOKEN)
                .content("""
                        {
                        "username" : "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult resultSuccessLogin = performSuccessLogin
                .andExpect(status().isOk())
                .andReturn();

        String jsonSuccessLogin = resultSuccessLogin.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(jsonSuccessLogin, JwtResponseDto.class);
        String token = jwtResponseDto.token();
        assertAll(
                () -> assertThat(jwtResponseDto.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(
                        Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$")
                )
        );

        // step 7: user made POST /inputNumbers with header “Authorization: Bearer AAAA.BBBB.CCC”
        // with six numbers (1, 2, 3, 4, 5, 6) at 5-03-2025 12:00 and system returned OK(200) with message:
        // “success” and Ticket (DrawDate:8.03.2025 12:00 (Saturday), TicketId: sampleTicketId)
        // given
        // when
        ResultActions performPostNumber = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                .header("Authorization", "Bearer " + token)
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
        String ticketId = numberReceiverResponseDto.ticketDto().ticketId();

        // then
        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("SUCCESS")
        );

        // step 8: user made GET /results/nonExistingId with header “Authorization: Bearer AAAA.BBBB.CCC”
        // and system returned 404(NOT_FOUND) and body with
        // (message: Not found for id: nonExistingId and status NOT_FOUND)
        // given
        // when
        String nonExistingId = "nonExistingId";
        ResultActions performGetResultsWithNotExistingId =
                mockMvc.perform(get(RESULTS_ENDPOINT + nonExistingId)
                        .header("Authorization", "Bearer " + token));

        //then
        performGetResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message" : "Player with id: [nonExistingId] has not been found in the database",
                        "status": "NOT_FOUND"
                        }
                        """.trim()
                ));

        // step 5: 3 days and 55 minute passed, and it is 5 minute before the draw date (8.03.2025 10:55)
        log.info(clock.toString());
        clock.plusDaysAndHourAndMinutes(3, 0, 55);
        log.info(clock.toString());

        // step 9: system generated result for TicketId: sampleTicketId with draw date 8.03.2025 12:00,
        // and saved it with 6 hits
        // given
        // when
        await()
                .atMost(20, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !resultCheckerFacade.findById(ticketId).resultId().isEmpty();
                    } catch (PlayerNotFoundException exception) {
                        return false;
                    }
                });

        // step 10: 1 hour and 6 minutes passed, and it is 1 minute after announcement time (8.03.2025 12:01)
        clock.plusDaysAndHourAndMinutes(0, 1, 6);
        log.info(clock.toString());

        // step 8: user made GET /results/sampleTicketId with header “Authorization: Bearer AAAA.BBBB.CCC”
        // and system returned 200 (OK)
        // given
        // when
        ResultActions performGetResultsForUniqueId =
                mockMvc.perform(get(RESULTS_ENDPOINT + ticketId)
                        .header("Authorization", "Bearer " + token)
                );
        MvcResult mvcResultForUniqueId = performGetResultsForUniqueId.andExpect(status().isOk()).andReturn();
        String jsonForUniqueId = mvcResultForUniqueId.getResponse().getContentAsString();
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = objectMapper.readValue(jsonForUniqueId, ResultAnnouncerResponseDto.class);

        // then
        assertAll(
                () -> assertThat(resultAnnouncerResponseDto.responseDto().id()).isEqualTo(ticketId),
                () -> assertThat(resultAnnouncerResponseDto.responseDto().numbers()).isEqualTo(Set.of(1, 2, 3, 4, 5, 6)),
                () -> assertThat(resultAnnouncerResponseDto.responseDto().guessedNumbers()).isEqualTo(Set.of(1, 2, 3, 4, 5, 6)),
                () -> assertThat(resultAnnouncerResponseDto.responseDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(resultAnnouncerResponseDto.responseDto().isWinner()).isEqualTo(true),
                () -> assertThat(resultAnnouncerResponseDto.message()).isEqualTo("Congratulations, you won!")
        );
    }
}
