package org.example.infrastructure.winningnumbersgenerator.http;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.winningnumbersgenerator.RandomNumbersGenerable;
import org.example.domain.winningnumbersgenerator.dto.SixRandomNumbersDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class RandomNumbersGeneratorRestTemplate implements RandomNumbersGenerable {

    public static final String RANDOM_NUMBER_SERVICE_PATH = "/api/v1.0/random";
    public static final int NUMBER_OF_WINNING_NUMBERS = 6;
    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateNumbers(int numberOfNumbers, int minimumNumber, int maximumNumber) {
        log.info("Started fetching winning numbers using http client");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            final ResponseEntity<List<Integer>> response = makeGetRequest
                    (numberOfNumbers, minimumNumber, maximumNumber, requestEntity);
            Set<Integer> sixDistinctNumbers = getSixRandomDistinctNumbers(response);
            if (sixDistinctNumbers.size() != NUMBER_OF_WINNING_NUMBERS) {
                log.error("Set is less than: {} Have to request one more time", numberOfNumbers);
                return generateNumbers(numberOfNumbers, minimumNumber, maximumNumber);
            }
            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctNumbers)
                    .build();
        } catch (ResourceAccessException e) {
            log.error("Error while fetching winning numbers using http client: " + e.getMessage());
            return SixRandomNumbersDto.builder().build();
        }
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int numberOfNumbers, int minimumNumber, int maximumNumber, HttpEntity<HttpHeaders> requestEntity) {
        final String url = UriComponentsBuilder.fromHttpUrl(getUrlForService())
                .queryParam("min", minimumNumber)
                .queryParam("max", maximumNumber)
                .queryParam("count", numberOfNumbers)
                .toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
    }

    private Set<Integer> getSixRandomDistinctNumbers(ResponseEntity<List<Integer>> response) {
        List<Integer> numbers = response.getBody();
        if (numbers == null) {
            log.error("Response Body was null returning empty collection");
            return Collections.emptySet();
        }
        log.info("Success Response Body Returned: " + response);
        Set<Integer> distinctNumbers = new HashSet<>(numbers);
        return distinctNumbers.stream()
                .limit(NUMBER_OF_WINNING_NUMBERS)
                .collect(Collectors.toSet());
    }

    private String getUrlForService() {
        return uri + ":" + port + RandomNumbersGeneratorRestTemplate.RANDOM_NUMBER_SERVICE_PATH;
    }
}
