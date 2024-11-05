package com.example.hugbo_team_13.controller;


import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.GameCreateDTO;
import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.dto.RankDTO;
import com.example.hugbo_team_13.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
@SessionAttributes("loggedInUser")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/create")
    public String getCreateGame(Model model) {
        model.addAttribute("game", new GameDTO());
        model.addAttribute("rank", new RankDTO());
        return "game/create";
    }

    @GetMapping("/list")
    public String getEvents(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "game/list";
    }

}