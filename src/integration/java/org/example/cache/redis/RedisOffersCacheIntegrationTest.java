package org.example.cache.redis;

import org.example.BaseIntegrationTest;
import org.example.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.example.domain.resultannouncer.ResultAnnouncerFacade;
import org.example.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RedisOffersCacheIntegrationTest extends BaseIntegrationTest {
    public static final String RESULTS_ENDPOINT = "/results/";
    public static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";
    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_offers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: someUser was registered with somePassword
        // given
        // when
        ResultActions registerAction = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        registerAction.andExpect(status().isCreated());


        // step 2: login
        // given
        // when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(json, JwtResponseDto.class);
        String jwtToken = jwtResponse.token();

        // step 4: should generate ticket
        // given
        ResultActions performPostNumber = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                .header("Authorization", "Bearer " + jwtToken)
                .content("""
                        {
                        "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult resultPostNumbers = performPostNumber.andExpect(status().isOk()).andReturn();
        String jsonPostNumbers = resultPostNumbers.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(jsonPostNumbers, NumberReceiverResponseDto.class);
        String ticketId = numberReceiverResponseDto.ticketDto().ticketId();

        // step 4: should save to cache results by id request
        // given && when
        mockMvc.perform(get(RESULTS_ENDPOINT + ticketId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        verify(resultAnnouncerFacade, times(1)).checkResult(ticketId);
        assertThat(cacheManager.getCacheNames().contains("jobOffers")).isTrue();


        // step 5: cache should be invalidated
        // given && when && then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                            mockMvc.perform(get(RESULTS_ENDPOINT + ticketId)
                                    .header("Authorization", "Bearer " + jwtToken)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                            );
                            verify(resultAnnouncerFacade, atLeast(2)).checkResult(ticketId);
                        }
                );

    }
}
