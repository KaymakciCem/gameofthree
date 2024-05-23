package org.ck.takeaway.domain.gameaggregate;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import org.ck.takeaway.config.jms.JmsConfig;
import org.ck.takeaway.domain.exception.GameException;
import org.ck.takeaway.domain.gameaggregate.valueobjects.InputNumber;
import org.ck.takeaway.domain.gameaggregate.valueobjects.OutputNumber;
import org.ck.takeaway.domain.repository.GameRepository;
import org.ck.takeaway.dto.GameState;
import org.ck.takeaway.dto.GameStatusChangedEvent;
import org.ck.takeaway.dto.JoinGameMessage;
import org.ck.takeaway.dto.GameMessageType;
import org.ck.takeaway.dto.PlayerMoveEvent;
import org.ck.takeaway.support.JmsSender;
import org.ck.takeaway.support.StompSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameDomainServiceImpl implements GameDomainService {

    private final GameRepository gameRepository;
    private final JmsSender jmsSender;
    private final StompSender stompSender;

    public GameDomainServiceImpl(final GameRepository gameRepository,
                                 final JmsSender jmsSender,
                                 final StompSender stompSender) {
        this.gameRepository = gameRepository;
        this.jmsSender = jmsSender;
        this.stompSender = stompSender;
    }

    @Override
    public void addPlayer(JoinGameMessage joinGameRequest) {
        final Game game;
        if (Objects.isNull(joinGameRequest.gameId())) {
            game = gameRepository.save(new Game(UUID.randomUUID(), null,
                                                joinGameRequest.playMode() == PlayMode.AUTO ? new InputNumber() :
                                                        new InputNumber(joinGameRequest.startNumber())));
        } else {
            var maybeGame = gameRepository.findById(joinGameRequest.gameId());
            game = maybeGame.get();
        }

        game.addPlayer();

        if (game.getGameStatus() == GameStatus.READY) {
            var gameStatusChangedEvent = new GameStatusChangedEvent(game.getId(), game.getGameStatus());

            game.start();

            gameRepository.save(game);

            jmsSender.sendDelayedEventToQueue(JmsConfig.GAME_STATUS_CHANGED,
                                              gameStatusChangedEvent.nextEvent(),
                                              Duration.ofSeconds(1));

            return;
        }

        gameRepository.save(game);
        sendStompMessage(StompSender.GAME_STATE_DESTINATION, game, GameMessageType.JOIN_GAME);
    }

    @Override
    public void play(final Game game) throws GameException {
        var currentPlayer = game.getPlayers().values()
                                .stream()
                                .filter(player -> player.getId().equals(game.getCurrentPlayerId()))
                                .toList();

        game.play(currentPlayer.get(0), game.getCurrentNumber());
        sendStompMessage(StompSender.GAME_STATE_DESTINATION, game, GameMessageType.MOVE);
        game.nextPlayer(currentPlayer.get(0).getId());

        if (game.getGameStatus() == GameStatus.COMPLETED) {
            game.stopGame();
            gameRepository.save(game);
            sendStompMessage(StompSender.GAME_STATE_DESTINATION, game, GameMessageType.MOVE);

            return;
        }

        gameRepository.save(game);
        sendJmsEvent(game.getId(), JmsConfig.PLAYER_MOVED);
    }

    private void sendStompMessage(final String destination,
                                  final Game game,
                                  final GameMessageType messageType) {
        var gameState = new GameState(messageType.getValue(),
                                      game.getId(),
                                      game.getCurrentPlayerId(),
                                      game.getGameStatus(),
                                      new OutputNumber(game.getCurrentNumber().getValue()),
                                      game.getPlayers().get(game.getCurrentPlayerId()).getAddedNumber(),
                                      game.getGameStatus() == GameStatus.COMPLETED ? true : null);


        this.stompSender.send(destination, gameState);
    }

    private void sendJmsEvent(final UUID gameId, final String queueName) {
        var playerMoveEvent = new PlayerMoveEvent(gameId);

        jmsSender.sendDelayedEventToQueue(queueName,
                                          playerMoveEvent.nextEvent(),
                                          Duration.ofSeconds(5));
    }
}
