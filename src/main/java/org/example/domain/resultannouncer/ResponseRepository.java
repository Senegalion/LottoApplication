package org.example.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ResponseRepository extends MongoRepository<ResultResponse, String> {
    boolean existsById(String id);

    Optional<ResultResponse> findById(String id);

    ResultResponse save(ResultResponse resultResponse);
}
