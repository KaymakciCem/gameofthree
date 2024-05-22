package org.ck.takeaway.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.ck.takeaway.domain.gameaggregate.Game;
import org.ck.takeaway.domain.gameaggregate.GameStatus;
import org.ck.takeaway.domain.repository.GameRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class InMemoryStorage implements GameRepository {

    private static Map<UUID, Game> games = new HashMap<>();

    private InMemoryStorage(){ }

    private static class Holder {
        private static final InMemoryStorage INSTANCE = new InMemoryStorage();
    }

    public static InMemoryStorage getInstance() {
        return Holder.INSTANCE;
    }

    public Map<UUID, Game> getGames() {
        return games;
    }

    @Override
    public List<Game> findAllByGameStatus(GameStatus gameStatus) {
        return games.values()
                    .stream()
                    .filter(game -> game.getGameStatus().equals(gameStatus))
                    .toList();
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return games.values()
                    .stream()
                    .filter(game -> game.getId().equals(id))
                    .findFirst();
    }

    @Override
    public List<Game> findAll() {
        return getGames().values().stream().toList();
    }

    @Override
    public Game save(Game game) {
        games.put(game.getId(), game);
        return game;
    }
}
