package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.model.GameDTO;
import com.example.hugbo_team_13.model.RankDTO;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.entity.RankEntity;
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
        String name = gameDTO.getName();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be empty");
        }
        if (gameRepository.existsByName(name)) {
            throw new RuntimeException("Game name already exists");
        }
        // Create GameEntity
        GameEntity game = gameToEntity(gameDTO);
        GameEntity savedGame = gameRepository.save(game);

        // Return a new GameDTO
        return gameToDto(savedGame);
    }

    public Optional<GameDTO> getGameById(Long id) {
        return gameRepository.findById(id).map(this::gameToDto);
    }

    public GameDTO getGameByName(String name) {
        GameEntity game = gameRepository.findByName(name);
        return game != null ? gameToDto(game) : null;
    }

    public List<GameDTO> getAllGames() {
        List<GameEntity> gameEntities = gameRepository.findAll();
        List<GameDTO> gameDTOs = new ArrayList<>();

        for (GameEntity game : gameEntities) {
            gameDTOs.add(gameToDto(game));
        }
        return gameDTOs;
    }

    public GameDTO saveGame(GameDTO gameDTO) { // (save creates a new entity if ID is not set)
        GameEntity game = gameToEntity(gameDTO);
        GameEntity savedGame = gameRepository.save(game);
        return gameToDto(savedGame);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public void deleteAllGames() {
        gameRepository.deleteAll();
    }

    private GameDTO gameToDto(GameEntity game) {
        RankEntity rankEntity= game.getRankEntity();
        RankDTO rankDTO = rankToDto(rankEntity);
        return new GameDTO(game.getId(), game.getName(), game.getPlatform(), rankDTO);
    }

    private GameEntity gameToEntity(GameDTO dto) {
        GameEntity game = new GameEntity();
        game.setName(dto.getName());
        game.setPlatform(dto.getPlatform());

        if (dto.getRank() != null) {
            game.setRankEntity(rankToEntity(dto.getRank()));
        }
        return game;
    }

    private RankDTO rankToDto(RankEntity entity) {
        if (entity == null ||entity.getId() == null) {
            return null; // ERROR MSG
        }
        RankDTO dto = new RankDTO();
        if (entity.getGame() != null) {
            dto.setGame(gameToDto(entity.getGame()));
        }
        dto.setRanks(entity.getRanks());
        return dto;
    }

    private RankEntity rankToEntity(RankDTO rankDTO) {
        RankEntity dto = new RankEntity();
        dto.setId(rankDTO.getId());
        dto.setGame(gameToEntity(rankDTO.getGame()));
        dto.setRanks(rankDTO.getRanks());
        return dto;
    }
}
