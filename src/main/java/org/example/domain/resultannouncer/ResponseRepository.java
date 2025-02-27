package org.example.domain.resultannouncer;

import java.util.Optional;

public interface ResponseRepository {
    boolean existsById(String id);

    Optional<ResultResponse> findById(String id);

    ResultResponse save(ResultResponse resultResponse);
}
