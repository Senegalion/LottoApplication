package org.example.domain.resultchecker;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    List<Player> saveAll(List<Player> players);

    Optional<Player> findById(String id);
}
