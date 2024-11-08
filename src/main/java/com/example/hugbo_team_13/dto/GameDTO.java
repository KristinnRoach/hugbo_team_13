package com.example.hugbo_team_13.dto;

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
    private Long id;

    /**
     * Name of the game.
     */
    private String name;

    /**
     * Platform on which the game is available (e.g., PC, console).
     */
    private String platform;

    /**
     * Mapping of ranks within the game.
     * The key is the rank level, and the value is the description or title of that rank.
     */
    private Map<Integer, String> ranks = new HashMap<>();

    /**
     * Constructs a new GameDTO with the specified values.
     *
     * @param id       The unique identifier for the game.
     * @param name     The name of the game.
     * @param platform The platform for the game.
     * @param ranks    A map of ranks within the game.
     */
    public GameDTO(Long id, String name, String platform, Map<Integer, String> ranks) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.ranks = ranks;
    }
}
