package org.ck.takeaway.domain.gameaggregate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.ck.takeaway.config.jms.JmsConfig;
import org.ck.takeaway.domain.exception.GameException;
import org.ck.takeaway.domain.gameaggregate.entities.Player;
import org.ck.takeaway.domain.gameaggregate.valueobjects.InputNumber;
import org.ck.takeaway.domain.repository.GameRepository;
import org.ck.takeaway.dto.GameState;
import org.ck.takeaway.dto.JoinGameMessage;
import org.ck.takeaway.support.JmsSender;
import org.ck.takeaway.support.StompSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameDomainServiceImplTest {

    @InjectMocks
    private GameDomainServiceImpl gameDomainService;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private JmsSender jmsSender;
    @Mock
    private StompSender stompSender;

    private Game game;
    private Player player;
    private JoinGameMessage joinGameMessage;

    @BeforeEach
    void setUp() {
        player = new Player(UUID.randomUUID());
        game = new Game(UUID.randomUUID(), UUID.randomUUID(), new InputNumber(10));
        joinGameMessage = new JoinGameMessage("game.join",
                                              game.getId(),
                                              5,
                                              PlayMode.AUTO);
    }

    @Test
    void addPlayer_shouldAddPlayerAndSaveGame() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        gameDomainService.addPlayer(joinGameMessage);

        verify(gameRepository, times(1)).save(game);
        verify(stompSender, times(1))
                .send(eq(StompSender.GAME_STATE_DESTINATION), any(GameState.class));
    }

    @Test
    void addPlayer_shouldStartGameWhenReady() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        gameDomainService.addPlayer(joinGameMessage);
        gameDomainService.addPlayer(joinGameMessage);

        verify(gameRepository, times(2)).save(game);
        verify(jmsSender).sendDelayedEventToQueue(eq(JmsConfig.GAME_STATUS_CHANGED), any(),
                                         eq(Duration.ofSeconds(1)));
    }

    @Test
    void play_shouldPlayMoveAndSaveGame() throws GameException {
        game.addPlayer();
        game.addPlayer();
        game.start();

        gameDomainService.play(game);

        verify(gameRepository, times(1)).save(game);
        verify(stompSender, times(1)).send(eq(StompSender.GAME_STATE_DESTINATION), any(GameState.class));
    }

    @Test
    void play_shouldCompleteGameWhenFinished() throws GameException {
        game.addPlayer();
        game.addPlayer();
        game.start();

        gameDomainService.play(game);

        verify(gameRepository, times(1)).save(game);
        verify(stompSender, times(1)).send(eq(StompSender.GAME_STATE_DESTINATION), any(GameState.class));
    }
}