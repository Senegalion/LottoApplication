package org.example.domain.winningnumbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
    Optional<WinningNumbers> findNumbersByDrawDate(LocalDateTime date);

    boolean existsByDrawDate(LocalDateTime nextDrawDate);
}
