package org.ck.takeaway.domain.gameaggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.ck.takeaway.domain.exception.GameException;
import org.ck.takeaway.domain.gameaggregate.entities.Player;
import org.ck.takeaway.domain.gameaggregate.valueobjects.InputNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpStatusCodeException;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(UUID.randomUUID(), UUID.randomUUID(), new InputNumber(3));
    }

    @Test
    void addPlayer_should_have_one_player_waiting_for_second_user() {
        game.addPlayer();

        assertThat(game.getPlayers().size()).isEqualTo(1);
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_SECOND_USER);
    }

    @Test
    void addPlayer_multiple_users_should_have_two_players_ready_state() {
        game.addPlayer();
        game.addPlayer();

        assertThat(game.getPlayers().size()).isEqualTo(2);
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.READY);
    }

    @Test
    void addPlayer_three_users_should_throw_exception() {
        game.addPlayer();
        game.addPlayer();
        assertThrows(GameException.class, () ->  game.addPlayer());
    }

    @Test
    void start_should_throw_exception_when_no_user() {
        assertThrows(GameException.class, () ->  game.start());
    }

    @Test
    void start_should_throw_exception_when_only_one_user() {
        game.addPlayer();
        assertThrows(GameException.class, () ->  game.start());
    }

    @Test
    void start_should_set_game_status_IN_PROGRESS() {
        game.addPlayer();
        game.addPlayer();
        game.start();
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void play_should_throw_exception_when_game_status_is_not_IN_PROGRESS() {
        assertThrows(GameException.class, () ->  game.play(new Player(UUID.randomUUID()), new InputNumber(3)));
    }

    @Test
    void play_output_number_should_not_be_same_as_input_number() {
        game.addPlayer();
        game.addPlayer();
        game.start();
        game.play(new Player(UUID.randomUUID()), new InputNumber(3));
        assertThat(game.getCurrentNumber()).isNotEqualTo(new InputNumber(3));
    }

    @Test
    void play_game_status_should_be_completed_when_output_number_is_1() {
        game.addPlayer();
        game.addPlayer();
        game.start();
        game.play(new Player(UUID.randomUUID()), new InputNumber(1));
        assertThat(game.getCurrentNumber()).isNotEqualTo(new InputNumber(1));
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.COMPLETED);
    }

    @Test
    void nextPlayer_should_set_current_playerId_to_the_next_player() {
        Player player = game.addPlayer();
        Player player1 = game.addPlayer();

        game.nextPlayer(player.getId());
        assertThat(game.getCurrentPlayerId()).isEqualTo(player1.getId());
    }

    @Test
    void stopGame() {
        game.stopGame();
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.COMPLETED);
    }
}