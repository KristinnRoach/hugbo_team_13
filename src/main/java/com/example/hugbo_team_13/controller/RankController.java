package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.service.GameService;
import com.example.hugbo_team_13.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for managing users in the application.
 * Provides endpoints for creating, retrieving, updating, and deleting user accounts.
 */
@Controller
@RequestMapping("/rank")
public class RankController {

    private final UserService userService;
    private final GameService gameService;

    public RankController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/list")
    public String getHighScoreList(Model model) {
        return "rank/list";
    }
}