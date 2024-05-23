package org.ck.takeaway.domain.gameaggregate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.ck.takeaway.domain.common.AggregateRoot;
import org.ck.takeaway.domain.exception.GameException;
import org.ck.takeaway.domain.gameaggregate.entities.Player;
import org.ck.takeaway.domain.gameaggregate.valueobjects.InputNumber;
import org.ck.takeaway.domain.gameaggregate.valueobjects.OutputNumber;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Game extends AggregateRoot {

    private GameStatus gameStatus;
    private InputNumber currentNumber;
    private Map<UUID, Player> players;
    private UUID currentPlayerId;


    public Game(UUID id, UUID currentPlayerId, InputNumber currentNumber) {
        super(id);

        this.gameStatus = GameStatus.NOT_STARTED;
        this.players = new LinkedHashMap<>(2);
        this.currentNumber = currentNumber;
        this.currentPlayerId = currentPlayerId;
    }

    public Player addPlayer() throws GameException {
        var playerId = UUID.randomUUID();
        currentPlayerId = playerId;

        Player player = new Player(playerId);

        if (players.size() == 2) {
            throw new GameException("Game is full of players");
        }

        players.put(player.getId(), player);
        if (players.size() == 1) {
            gameStatus = GameStatus.WAITING_FOR_SECOND_USER;
        } else {
            gameStatus = GameStatus.READY;
        }

        return player;
    }

    private boolean canStart() {
        return gameStatus == GameStatus.READY;
    }

    public void start() {
        if (!canStart()) {
            log.error("Game can't start");
            throw new GameException("Game can't start");
        }

        gameStatus = GameStatus.IN_PROGRESS;
    }

    public void play(final Player player, final InputNumber inputNumber) {
        if (gameStatus != GameStatus.IN_PROGRESS) {
            log.error("Game should be in running state");
            throw new GameException("Game should be in running state");
        }

        final OutputNumber outputNumber = player.move(inputNumber);
        this.currentNumber = new InputNumber(outputNumber.getValue());

        if (currentNumber.getValue() <= 1) {
            gameStatus = GameStatus.COMPLETED;
        }

    }

    public void nextPlayer(final UUID id) {
        this.currentPlayerId = players.values()
                                      .stream()
                                      .filter(p -> !Objects.equals(p.getId(), id))
                                      .findFirst()
                                      .map(Player::getId)
                                      .orElseThrow(() -> new GameException("No player found"));
    }

    public void stopGame() {
        this.currentNumber = new InputNumber(1);
        this.gameStatus = GameStatus.COMPLETED;
    }
}
