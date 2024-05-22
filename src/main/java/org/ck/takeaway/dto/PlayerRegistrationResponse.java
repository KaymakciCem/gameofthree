package org.ck.takeaway.dto;

import java.util.UUID;

import org.ck.takeaway.domain.gameaggregate.GameStatus;
import org.ck.takeaway.domain.gameaggregate.entities.Player;

public record PlayerRegistrationResponse(
    UUID gameId,
    GameStatus gameStatus,
    Player player) { }
