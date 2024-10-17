package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.model.GameDTO;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDTO createGame(GameDTO gameDTO) {
        // Todo: Validate input
        String name = gameDTO.getName();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be empty");
        }
        if (gameRepository.existsByName(name)) {
            // logger.warn("Attempted to create game with existing game name: {}", name);
            throw new RuntimeException("Game name already exists");
        }

        // Create GameEntity
        GameEntity game = new GameEntity();
        game.setName(gameDTO.getName());
        game.setPlatform(gameDTO.getPlatform());
        game.setRanks(gameDTO.getRank());

        GameEntity savedGame = gameRepository.save(game);

        // Return a new GameDTO
        return convertToDTO(savedGame);
    }

    public Optional<GameDTO> getGameById(Long id) {
        return gameRepository.findById(id).map(this::convertToDTO);
    }

    public GameDTO getGameByName(String name) {
        GameEntity game = gameRepository.findByName(name);
        return game != null ? convertToDTO(game) : null;
    }

    public List<GameDTO> getAllGames() {
        List<GameEntity> gameEntities = gameRepository.findAll();
        List<GameDTO> gameDTOs = new ArrayList<>();

        for (GameEntity game : gameEntities) {
            gameDTOs.add(convertToDTO(game));
        }
        return gameDTOs;
    }

    public GameDTO saveGame(GameDTO gameDTO) { // (save creates a new entity if ID is not set)
        GameEntity game = convertToEntity(gameDTO);
        GameEntity savedGame = gameRepository.save(game);
        return convertToDTO(savedGame);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public void deleteAllGames() {
        gameRepository.deleteAll();
    }

    private GameDTO convertToDTO(GameEntity game) {
        return new GameDTO(game.getId(), game.getName(), game.getPlatform(), game.getRanks());
    }

    private GameEntity convertToEntity(GameDTO gameDTO) {
        GameEntity game = new GameEntity(gameDTO.getName());
        game.setId(gameDTO.getId());
        return game;
    }
}
