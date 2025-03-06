package org.example.domain.resultannouncer;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record ResultResponse(
        @Id
        String id,
        Set<Integer> numbers,
        Set<Integer> guessedNumbers,
        LocalDateTime drawDate,
        boolean isWinner
) {
}
