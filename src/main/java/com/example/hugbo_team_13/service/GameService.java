package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.model.GameDTO;
import com.example.hugbo_team_13.model.RankDTO;
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

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final RankRepository rankRepository;

    public GameService(GameRepository gameRepository, RankRepository rankRepository) {
        this.gameRepository = gameRepository;
        this.rankRepository = rankRepository;
    }

    public GameDTO createGame(GameDTO gameDTO) {
        String name = gameDTO.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Missing game name");
        }
        if (gameRepository.existsByName(name)) {
            throw new RuntimeException("A game with the name of: " + name + " already exists");
        }
        // Create GameEntity
        GameEntity game = createGameEntity(gameDTO);
        GameEntity savedGame = gameRepository.save(game);

        // Return a new GameDTO
        return convertGameToDto(savedGame);
    }

    public Optional<GameDTO> getGameById(Long id) {
        return gameRepository.findById(id).map(this::convertGameToDto);
    }

    public GameDTO getGameByName(String name) {
        GameEntity game = gameRepository.findByName(name);
        return game != null ? convertGameToDto(game) : null;
    }

    public List<GameDTO> getAllGames() {
        List<GameEntity> gameEntities = gameRepository.findAll();
        return gameEntities.stream().map(this::convertGameToDto).collect(Collectors.toList());
    }


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

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public void deleteAllGames() {
        gameRepository.deleteAll();
    }

    private GameDTO convertGameToDto(GameEntity game) {
        RankEntity rankEntity= game.getRankEntity();
        RankDTO rankDTO = rankEntity != null ? convertRankToDto(rankEntity) : null;
        return new GameDTO(game.getId(), game.getName(), game.getPlatform(), rankDTO);
    }

    private GameEntity createGameEntity(GameDTO dto) {
        GameEntity game = new GameEntity();
        game.setName(dto.getName());
        game.setPlatform(dto.getPlatform());

        if (dto.getRank() != null) {
            RankEntity rankEntity = new RankEntity();
            rankEntity.setRanks(dto.getRank().getRanks());
            game.setRankEntity(rankEntity);
        }
        return game;
    }

    public RankDTO createRankForGame(Long gameId, RankDTO rankDTO) {
        GameEntity game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameId));

        if (game.getRankEntity() != null) {
            throw new IllegalStateException("Game already has a ranking system");
        }

        RankEntity rankEntity = createRankEntity(rankDTO);
        rankEntity.setGame(game);
        game.setRankEntity(rankEntity);

        rankEntity = rankRepository.save(rankEntity);
        gameRepository.save(game);  // might not be necessary due to cascading, but safer to do it explicitly

        return convertRankToDto(rankEntity);
    }

    private RankEntity createRankEntity(RankDTO dto) {
        RankEntity rankEntity = new RankEntity();
        rankEntity.setRanks(dto.getRanks());
        return rankEntity;
    }

    private RankDTO convertRankToDto(RankEntity entity) {
        return new RankDTO(entity.getId(), entity.getGame().getId(), entity.getRanks());
    }
}
