package org.ck.takeaway.support;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompSender {
    private final SimpMessagingTemplate messagingTemplate;

    public static final String GAME_STATE_DESTINATION = "/topic/game.state";

    public void send(String destination, Object payload) {
        log.info("Sending message to game");
        messagingTemplate.convertAndSend(destination, payload);
    }
}
