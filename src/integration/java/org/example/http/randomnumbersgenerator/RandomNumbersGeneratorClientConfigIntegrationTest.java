package org.example.http.randomnumbersgenerator;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.domain.winningnumbersgenerator.RandomNumbersGenerable;
import org.example.infrastructure.winningnumbersgenerator.http.RandomNumbersGeneratorClientConfig;
import org.example.infrastructure.winningnumbersgenerator.http.RandomNumbersGeneratorRestTemplate;
import org.example.infrastructure.winningnumbersgenerator.http.RandomNumbersGeneratorRestTemplateConfigurationProperties;
import org.springframework.web.client.RestTemplate;

public class RandomNumbersGeneratorClientConfigIntegrationTest extends RandomNumbersGeneratorClientConfig {
    public static final String WIRE_MOCK_HOST = "http://localhost";
    private final WireMockExtension wireMockServer;

    public RandomNumbersGeneratorClientConfigIntegrationTest(RandomNumbersGeneratorRestTemplateConfigurationProperties properties, WireMockExtension wireMockServer) {
        super(properties);
        this.wireMockServer = wireMockServer;
    }

    public RandomNumbersGenerable remoteNumberGeneratorClient() {
        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler());
        return new RandomNumbersGeneratorRestTemplate(restTemplate, WIRE_MOCK_HOST, wireMockServer.getPort());
    }
}
