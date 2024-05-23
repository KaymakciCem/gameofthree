package org.ck.takeaway.dto;

import java.util.UUID;

import org.ck.takeaway.domain.gameaggregate.PlayMode;
import org.ck.takeaway.domain.gameaggregate.valueobjects.InputNumber;

public record JoinGameMessage(
        String type,
        UUID gameId,
        Integer startNumber,
        PlayMode playMode) { }
