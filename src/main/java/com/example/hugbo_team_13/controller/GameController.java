package com.example.hugbo_team_13.controller;


import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.GameCreateDTO;
import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.dto.RankDTO;
import com.example.hugbo_team_13.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/create")
    public String getCreateGame(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("game", new GameDTO());
        return "game/create";
    }

    @PostMapping("/create")
    public String createGame(@ModelAttribute("game") GameDTO gameDTO) {
        gameService.createGame(gameDTO);
        return "redirect:/game/list";
    }

    @GetMapping("/list")
    public String getEvents(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "game/list";
    }

}