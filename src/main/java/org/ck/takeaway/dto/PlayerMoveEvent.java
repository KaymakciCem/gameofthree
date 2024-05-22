package org.ck.takeaway.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMoveEvent implements Serializable {

    private UUID gameId;

    public PlayerMoveEvent nextEvent() {
        return new PlayerMoveEvent(gameId);
    }
}
