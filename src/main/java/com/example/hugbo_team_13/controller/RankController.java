package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.service.GameService;
import com.example.hugbo_team_13.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for managing rank-related functionality in the application.
 * Provides endpoints for viewing high scores or rankings.
 */
@Controller
@RequestMapping("/rank")
public class RankController {

    private final UserService userService;
    private final GameService gameService;

    /**
     * Constructs a RankController with the specified UserService and GameService.
     *
     * @param userService the service handling user operations
     * @param gameService the service handling game operations
     */
    public RankController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    /**
     * Handles GET requests to display a list of high scores or rankings.
     *
     * @param model the model to hold rank or high score data
     * @return the name of the view for the rank list page
     */
    @GetMapping("/list")
    public String getHighScoreList(Model model) {
        return "rank/list";
    }
}
