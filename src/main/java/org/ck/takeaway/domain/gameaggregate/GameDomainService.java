package org.ck.takeaway.domain.gameaggregate;

import org.ck.takeaway.domain.exception.GameException;
import org.ck.takeaway.dto.GameState;
import org.ck.takeaway.dto.JoinGameMessage;

public interface GameDomainService {
    void addPlayer(JoinGameMessage joinGameRequest);
    void play(Game game) throws GameException;
}
