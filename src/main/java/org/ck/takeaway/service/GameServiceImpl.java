package org.ck.takeaway.service;

import java.util.List;
import java.util.UUID;

import org.ck.takeaway.config.jms.JmsConfig;
import org.ck.takeaway.domain.common.Entity;
import org.ck.takeaway.domain.gameaggregate.GameDomainService;
import org.ck.takeaway.domain.gameaggregate.GameStatus;
import org.ck.takeaway.domain.repository.GameRepository;
import org.ck.takeaway.dto.GameStatusChangedEvent;
import org.ck.takeaway.dto.JoinGameMessage;
import org.ck.takeaway.dto.PlayerMoveEvent;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameDomainService gameDomainService;
    private final GameRepository gameRepository;

    @Override
    public void addPlayer(JoinGameMessage joinGameRequest) {
        gameDomainService.addPlayer(joinGameRequest);
    }

    @Override
    public List<UUID> retrieveAvailableGames() {
        return gameRepository.findAllByGameStatus(GameStatus.WAITING_FOR_SECOND_USER)
                       .stream()
                       .map(Entity::getId)
                       .toList();

    }

    public void play(final UUID gameId) {
        var game = gameRepository.findById(gameId);

        if (game.isEmpty()) {
            return;
        }

        gameDomainService.play(game.get());
    }

    @JmsListener(destination = JmsConfig.GAME_STATUS_CHANGED)
    public void processGameStatusChangedEvent(final GameStatusChangedEvent event) {
        if (GameStatus.READY == event.getGameStatus()) {
            play(event.getGameId());
        }
    }

    @JmsListener(destination = JmsConfig.PLAYER_MOVED)
    public void processPlayerMoveEvent(final PlayerMoveEvent event) {
        play(event.getGameId());
    }
}
