package org.ck.takeaway.controller;

import org.ck.takeaway.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GameOfThreeController {

    private final GameService gameService;

    @GetMapping
    public ModelAndView index() {
        return gameOfThree();
    }

    @RequestMapping("/index")
    public ModelAndView gameOfThree() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("gameIds", gameService.retrieveAvailableGames());
        modelAndView.addObject("firstAvailableGame", gameService.retrieveAvailableGames().size() == 0 ? null : gameService.retrieveAvailableGames().get(0));
        return modelAndView;
    }
}
