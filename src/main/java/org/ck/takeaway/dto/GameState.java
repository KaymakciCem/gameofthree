package org.ck.takeaway.dto;

import java.util.UUID;

import org.ck.takeaway.domain.gameaggregate.GameStatus;
import org.ck.takeaway.domain.gameaggregate.valueobjects.OutputNumber;

public record GameState(String type,
                        UUID gameId,
                        UUID playerId,
                        GameStatus gameStatus,
                        OutputNumber outputNumber,
                        Integer addedNumber,
                        Boolean winner) { }
