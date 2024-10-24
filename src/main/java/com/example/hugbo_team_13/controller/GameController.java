package com.example.hugbo_team_13.controller;


import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.service.EventService;
import com.example.hugbo_team_13.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/create")
    public String getCreateGame(Model model) {
        model.addAttribute("game", new GameDTO());
        return "game/create";
    }

    @GetMapping("/list")
    public String getEvents(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "game/list";
    }

}