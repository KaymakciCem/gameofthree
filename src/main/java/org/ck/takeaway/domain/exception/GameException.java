package org.ck.takeaway.domain.exception;

public class GameException extends RuntimeException {
    public GameException() {
        super();
    }

    public GameException(String message) {
        super(message);
    }
}

