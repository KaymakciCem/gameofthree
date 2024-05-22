package org.ck.takeaway.service;

import java.util.List;
import java.util.UUID;

import org.ck.takeaway.dto.JoinGameMessage;

public interface GameService {
    void addPlayer(JoinGameMessage joinGameRequest);
    List<UUID> retrieveAvailableGames();
}
