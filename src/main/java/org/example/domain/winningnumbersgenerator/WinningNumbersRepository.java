package org.example.domain.winningnumbersgenerator;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumbersRepository {
    Optional<WinningNumbers> findNumbersByDate(LocalDateTime date);

    boolean existsByDate(LocalDateTime nextDrawDate);

    WinningNumbers save(WinningNumbers winningNumbers);
}
