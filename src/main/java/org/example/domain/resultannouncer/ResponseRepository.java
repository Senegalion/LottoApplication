package org.example.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ResponseRepository extends MongoRepository<ResultResponse, String> {
}
