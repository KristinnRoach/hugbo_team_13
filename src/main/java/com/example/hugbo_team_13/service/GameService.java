package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.entity.RankEntity;
import com.example.hugbo_team_13.persistence.repository.GameRepository;
import com.example.hugbo_team_13.persistence.repository.RankRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing games and their rankings.
 * Provides methods to create, retrieve, update, and delete games using a GameRepository.
 * Handles ranking systems for games using a RankRepository.
 */
@Service
public class GameService {
    private final GameRepository gameRepository;

    /**
     * Constructor to inject the repositories.
     * 
     * @param gameRepository the repository to handle game data.
     * @param rankRepository the repository to handle rank data.
     */
    public GameService(GameRepository gameRepository, RankRepository rankRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Creates a new game from the provided GameDTO.
     * Validates the game name to ensure it is not null or empty and that it does not already exist.
     * 
     * @param gameDTO the data transfer object (DTO) representing the game.
     * @return the created GameDTO object.
     * @throws IllegalArgumentException if the game name is missing.
     * @throws RuntimeException if a game with the same name already exists.
     */
    public GameDTO createGame(GameDTO gameDTO) {
        String name = gameDTO.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Missing game name");
        }
        if (gameRepository.existsByName(name)) {
            throw new RuntimeException("A game with the name of: " + name + " already exists");
        }

        GameEntity game = createGameEntity(gameDTO);
        GameEntity savedGame = gameRepository.save(game);
        return convertGameToDto(savedGame);
    }

    private GameDTO convertGameToDto(GameEntity game) {
        return new GameDTO(game.getId(), game.getName(), game.getPlatform(), game.getRanks());
    }

    /**
     * Retrieves a game by its ID.
     *
     * 
     * @param id the ID of the game
     * @return an Optional containing the GameDTO if found, or empty if not.
     */
    public Optional<GameDTO> getGameById(Long id) {
        return gameRepository.findById(id).map(this::convertGameToDto);
    }

    /**
     * Retrieves a game by its name.
     * 
     * @param name the name of the game.
     * @return the GameDTO if found, or null if no game is found.
     */
    public GameDTO getGameByName(String name) {
        GameEntity game = gameRepository.findByName(name);
        return game != null ? convertGameToDto(game) : null;
    }

    /**
     * Retrieves all games.
     * 
     * @return a list of GameDTOs representing all games.
     */
    public List<GameDTO> getAllGames() {
        List<GameEntity> gameEntities = gameRepository.findAll();
        return gameEntities.stream().map(this::convertGameToDto).collect(Collectors.toList());
    }

    /**
     * Updates an existing game if it exists.
     * 
     * @param gameDTO the data transfer object (DTO) representing the game.
     * @return true if the game was updated successfully, false otherwise.
     * @throws IllegalArgumentException if the game name is missing or no game is found with that name.
     */
    public boolean saveGame(GameDTO gameDTO) {
        String name = gameDTO.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Missing game name");
        }

        if (!gameRepository.existsByName(name)) {
            throw new IllegalArgumentException("No game found with name: " + name);
        }

        GameEntity existingGame = gameRepository.findByName(name);
        if (existingGame == null) { return false; }

        gameRepository.save(existingGame);
        return true;
    }

    /**
     * Deletes a game by its ID.
     * 
     * @param id the ID of the game to delete.
     */
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    /**
     * Deletes all games from the database.
     */
    public void deleteAllGames() {
        gameRepository.deleteAll();
    }

    /**
     * Creates a GameEntity from a GameDTO.
     * 
     * @param dto the GameDTO to convert.
     * @return the corresponding GameEntity.
     */
    private GameEntity createGameEntity(GameDTO dto) {
        GameEntity game = new GameEntity();
        game.setName(dto.getName());
        game.setPlatform(dto.getPlatform());
        game.setRanks(dto.getRanks());
        return game;
    }
}

