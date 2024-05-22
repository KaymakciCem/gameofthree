package org.ck.takeaway.dto;

import java.io.Serializable;
import java.util.UUID;

import org.ck.takeaway.domain.gameaggregate.GameStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStatusChangedEvent implements Serializable {

    private UUID gameId;
    private GameStatus gameStatus;

    public GameStatusChangedEvent nextEvent() {
        return new GameStatusChangedEvent(gameId, gameStatus);
    }
}
