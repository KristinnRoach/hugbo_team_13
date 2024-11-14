package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

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


    @GetMapping("/{id}")
    public String getGamePage(Model model, @PathVariable String id) {
        GameDTO game = gameService.getGameById(id).orElseThrow();
        model.addAttribute("game", game);
        return "game/game-page";
    }

    @GetMapping("/{id}/edit")
    public String getEditEventPage(@PathVariable("id") String id, Model model) {
        GameDTO game =
                gameService.getGameById(id).orElseThrow();
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
        gameService.updateGame(game);
        return "redirect:/game/" + id;
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
        GameDTO game = new GameDTO();
        String headerText = "Add a new game:";

        if (session.getAttribute("gameDoesNotExist") != null) {
            game = (GameDTO) session.getAttribute("gameDoesNotExist");
            // session.removeAttribute("gameDoesNotExist"); // uncomment if needed
            headerText = String.format("The game %s has not yet been added to Skill-Share.\nPlease add the game to continue.", game.getName());
        }
        model.addAttribute("game", game);
        model.addAttribute("headerText", headerText);

        return "game/create";
    }

    /**
     * Handles POST requests for creating a new game.
     *
     * @param gameDTO the GameDTO representing the new game
     * @return a redirect to the game list page
     */
    @PostMapping("/create")
    public String createGame(@ModelAttribute("game") GameDTO gameDTO, HttpSession session, Model model) {
        GameDTO savedGame = gameService.createGame(gameDTO);
        model.addAttribute("game", savedGame);

        if (session.getAttribute("gameDoesNotExist") != null) {
            return "redirect:/event/create";
        }
        return "redirect:/game/list";
    }

    // Delete single game
    @DeleteMapping("/{id}")
    public String deleteGame(@PathVariable String id) {
        gameService.deleteGame(id); // Implement delete logic in service
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

    @GetMapping(value = "/{id}/img",  produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getImg(@PathVariable String id) {
        GameDTO game = gameService.getGameById(id).orElseThrow();
        if (game.getImg() != null) {
            // Get the stored content type, or default to JPEG if not available
            MediaType mediaType = MediaType.IMAGE_JPEG;
            if (game.getImgType() != null) {
                mediaType = MediaType.parseMediaType(game.getImgType());
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(game.getImg());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/img")
    public String updateGameImage(@PathVariable String id,
                                       @RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {
        try {
            gameService.updateImg(id, file);
            redirectAttributes.addFlashAttribute("message", "Game image updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update game image.");
        }
        return "redirect:/game/" + id + "/edit";
    }

}
