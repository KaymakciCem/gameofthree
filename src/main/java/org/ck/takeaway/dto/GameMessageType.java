package org.ck.takeaway.dto;

public enum GameMessageType {
    JOIN_GAME("game.join"),
    MOVE("game.move");

    public final String value;

    public String getValue() {
        return value;
    }

    GameMessageType(String value) {
        this.value = value;
    }


}
