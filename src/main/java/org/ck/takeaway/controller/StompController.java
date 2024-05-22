package org.ck.takeaway.controller;

import org.ck.takeaway.dto.JoinGameMessage;
import org.ck.takeaway.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class StompController {

    private final GameService gameService;

    @MessageMapping("/game.join")
    @SendTo("/topic/game.state")
    public void joinGame(@Payload JoinGameMessage participateRequest) {
        gameService.addPlayer(participateRequest);
    }

}
