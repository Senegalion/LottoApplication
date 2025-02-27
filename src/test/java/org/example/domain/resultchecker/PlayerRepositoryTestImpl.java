package org.example.domain.resultchecker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRepositoryTestImpl implements PlayerRepository {
    Map<String, Player> players = new ConcurrentHashMap<>();

    @Override
    public List<Player> saveAll(List<Player> players) {
        players.forEach(player -> this.players.put(player.playerId(), player));
        return players;
    }

    @Override
    public Optional<Player> findById(String id) {
        return java.util.Optional.ofNullable(players.get(id));
    }
}
