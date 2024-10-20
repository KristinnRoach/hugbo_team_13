package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST controller for managing games in the application.
 * Provides endpoints for creating, retrieving, and deleting games.
 */
@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    /**
     * Constructs a GameController with the specified GameService.
     *
     * @param gameService the service used to manage games
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * A test endpoint to demonstrate exception handling.
     *
     * @return a test message
     * @throws RuntimeException always, to trigger exception handling
     */
    @GetMapping("/test")
    public String test() {
        throw new RuntimeException("Test exception occurred!");
    }

    /**
     * Creates a new game.
     *
     * @param dto the details of the game to be created
     * @return a ResponseEntity containing the created GameDTO and a status of CREATED
     */
    @PostMapping
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO dto) {
        GameDTO gameDTO = gameService.createGame(dto);
        return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all games.
     *
     * @return a ResponseEntity containing a list of GameDTOs and a status of OK
     */
    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param id the ID of the game to retrieve
     * @return a ResponseEntity containing the GameDTO if found, or NOT FOUND status if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id)
                .map(game -> new ResponseEntity<>(game, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes a game by its ID.
     *
     * @param id the ID of the game to delete
     * @return a ResponseEntity with a NO CONTENT status if the game was deleted, or NOT FOUND status if the game does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        return gameService.getGameById(id)
                .map(game -> {
                    gameService.deleteGame(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes all games.
     *
     * @return a ResponseEntity with a NO CONTENT status after deleting all games
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllGames() {
        gameService.deleteAllGames();
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
