package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing games in the application.
 * Provides endpoints for creating and listing games.
 */
@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    /**
     * Constructs a GameController with the specified GameService.
     *
     * @param gameService the service handling game operations
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Delete single game
    @DeleteMapping("/{id}")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id); // Implement delete logic in service
        return "redirect:/game/list";
    }

    @GetMapping("/{id}/edit")
    public String getEditEventPage(@PathVariable("id") String id, Model model) {
        GameDTO game = gameService.getGameById(Long.parseLong(id)).orElseThrow();
        model.addAttribute("game", game);
        return "game/edit-game";
    }

    /**
     * Handles PUT requests to update a specific game by ID.
     *
     * @param id the ID of the game to update
     * @param game the updated GameDTO
     * @return a redirect to the game list page
     */
    @PutMapping("/{id}")
    public String updateGame(@PathVariable("id") String id, @ModelAttribute("game") GameDTO game) {
        game.setId(Long.parseLong(id));
        gameService.saveGame(game);
        return "redirect:/game/list";
    }

    /**
     * Handles GET requests to display the game creation form.
     * Redirects to the login page if no user is logged in.
     *
     * @param session the current HTTP session
     * @param model   the model to hold a new GameDTO
     * @return the name of the view for the game creation page
     */
    @GetMapping("/create")
    public String getCreateGame(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInUser") == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to add a new game.");
            model.addAttribute("prevPage", "/game/create");
            return "redirect:/user/login";
        }
        model.addAttribute("game", new GameDTO());
        return "game/create";
    }

    /**
     * Handles POST requests for creating a new game.
     *
     * @param gameDTO the GameDTO representing the new game
     * @return a redirect to the game list page
     */
    @PostMapping("/create")
    public String createGame(@ModelAttribute("game") GameDTO gameDTO) {
        gameService.createGame(gameDTO);
        return "redirect:/game/list";
    }

    /**
     * Handles GET requests to display a list of all games.
     *
     * @param model the model to hold game data
     * @return the name of the view for the game list page
     */
    @GetMapping("/list")
    public String getGames(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "game/list";
    }

}
