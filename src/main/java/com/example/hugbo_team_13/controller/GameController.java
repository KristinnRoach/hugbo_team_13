package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.model.GameDTO;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping // todo: bæta við allstaðar --> ("/create")
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO dto) {

        GameDTO gameDTO = gameService.createGame(dto);
        return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id)
                .map(game -> new ResponseEntity<>(game, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        return gameService.getGameById(id)
                .map(game -> {
                    gameService.deleteGame(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllGames() {
        gameService.deleteAllGames();
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
