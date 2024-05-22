package org.ck.takeaway.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ck.takeaway.domain.gameaggregate.Game;
import org.ck.takeaway.domain.gameaggregate.GameStatus;

public interface GameRepository {

    List<Game> findAllByGameStatus(GameStatus gameStatus);

    Optional<Game> findById(UUID id);

    List<Game> findAll();

    Game save(Game game);

}
