package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.helper.PictureData;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.repository.GameRepository;
import com.example.hugbo_team_13.persistence.repository.RankRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        return new GameDTO(game);
    }



    /**
     * Retrieves a game by its ID.
     *
     *
     * @param id the ID of the game
     * @return an Optional containing the GameDTO if found, or empty if not.
     */
    public Optional<GameDTO> getGameById(String id) {
        return gameRepository.findById(Long.parseLong(id)).map(this::convertGameToDto);
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
        GameEntity game = gameRepository.findById(Long.valueOf(gameDTO.getId())).orElse(new GameEntity());
        game.setRanks(gameDTO.getRanks());
        if (gameDTO.getImg() != null) {
            game.setImg(gameDTO.getImg());
            game.setImgType(gameDTO.getImgType());
        }
        gameRepository.save(game);
        return true;
    }

    public GameDTO updateGame(GameDTO gameDTO) {
        GameEntity game = gameRepository.findById(Long.valueOf(gameDTO.getId())).orElse(new GameEntity());

        Optional.ofNullable(gameDTO.getName()).ifPresent(game::setName);
        Optional.ofNullable(gameDTO.getPlatform()).ifPresent(game::setPlatform);
        Optional.ofNullable(gameDTO.getRanks()).ifPresent(game::setRanks);
        Optional.ofNullable(gameDTO.getImg()).ifPresent(game::setImg);
        Optional.ofNullable(gameDTO.getImgType()).ifPresent(game::setImgType);

        gameRepository.save(game);
        return convertGameToDto(game);
    }

    public Optional<PictureData> getImg(String id) {
        return gameRepository.findById(Long.parseLong(id))
                .filter(game -> game.getImg() != null)
                .map(game -> new PictureData(game.getImg(),
                        game.getImgType()));
    }

    public void updateImg(String id, MultipartFile file) throws IOException {
        gameRepository.findById(Long.parseLong(id)).ifPresent(game -> {
            try {
                game.setImg(file.getBytes());
                game.setImgType(file.getContentType());
                gameRepository.save(game);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process file", e);
            }
        });
    }

    protected GameEntity convertToEntity(GameDTO dto) {
        GameEntity entity = new GameEntity();
        if (dto.getId() != null) {
            entity.setId(Long.valueOf(dto.getId()));
        }
        entity.setName(dto.getName());
        entity.setPlatform(dto.getPlatform());
        entity.setRanks(dto.getRanks());
        return entity;
    }

    /**
     * Deletes a game by its ID.
     * 
     * @param id the ID of the game to delete.
     */
    public void deleteGame(String id) {
        gameRepository.deleteById(Long.parseLong(id));
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

