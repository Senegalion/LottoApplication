package org.example.domain.resultannouncer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseRepositoryTestImpl implements ResponseRepository {
    private final Map<String, ResultResponse> responses = new ConcurrentHashMap<>();

    @Override
    public boolean existsById(String id) {
        return responses.containsKey(id);
    }

    @Override
    public Optional<ResultResponse> findById(String id) {
        return Optional.ofNullable(responses.get(id));
    }

    @Override
    public ResultResponse save(ResultResponse resultResponse) {
        responses.put(resultResponse.id(), resultResponse);
        return resultResponse;
    }
}
