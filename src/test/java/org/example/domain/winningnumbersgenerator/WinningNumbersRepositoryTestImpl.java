package org.example.domain.winningnumbersgenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WinningNumbersRepositoryTestImpl implements WinningNumbersRepository {
    private final Map<LocalDateTime, WinningNumbers> winningNumbers = new ConcurrentHashMap<>();

    @Override
    public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
        return Optional.ofNullable(winningNumbers.get(date));
    }

    @Override
    public boolean existsByDate(LocalDateTime nextDrawDate) {
        return winningNumbers.containsKey(nextDrawDate);
    }

    @Override
    public WinningNumbers save(WinningNumbers winningNumbers) {
        this.winningNumbers.put(winningNumbers.drawDate(), winningNumbers);
        return winningNumbers;
    }
}
