package com.example.hugbo_team_13.dto;

import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing a game.
 * Contains details about the game including ID, name, platform, and ranks.
 */
@Getter
@Setter
@NoArgsConstructor
public class GameDTO {

    /**
     * Unique identifier for the game.
     */
    private String id;

    /**
     * Name of the game.
     */
    private String name;

    /**
     * Platform on which the game is available (e.g., PC, console).
     */
    private String platform;

    /**
     * Optional description of the game as a String
     */
    private String description;

    /**
     * The profile picture of the user, stored as a byte array.
     * Used for displaying the user's profile image in the UI.
     */
    private byte[] img;
    private String imgType;

    public UserEntity admin;

    /**
     * Mapping of ranks within the game.
     * The key is the rank level, and the value is the description or title of that rank.
     */
    private Map<Integer, String> ranks = new HashMap<>();

    /**
     * Constructs a new GameDTO from a GameEntity from the persistence layer.
     *
     * @param gameEntity  The Game Entity corresponding to the game.
     */
    public GameDTO(GameEntity gameEntity) {
        this.id = String.valueOf(gameEntity.getId());
        this.name = gameEntity.getName();
        this.platform = gameEntity.getPlatform();
        this.ranks = gameEntity.getRanks();
        this.img = gameEntity.getImg();
        this.imgType = gameEntity.getImgType();
        this.description = "No description available yet";
        this.admin = gameEntity.getAdmin();
    }
}
