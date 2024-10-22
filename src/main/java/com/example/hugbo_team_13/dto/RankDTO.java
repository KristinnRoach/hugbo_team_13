package com.example.hugbo_team_13.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object (DTO) representing the ranking system for a game.
 * This class is used to transfer rank-related data between different layers of the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankDTO {
    
    /**
     * Unique identifier for the rank system.
     */
    private Long id;
    /**
     * The ID of the game associated with this ranking system.
     */
    private Long gameId;

    /**
     * A map representing ranks, where the key is the rank level (e.g., 1, 2, 3) 
     * and the value is the name of the rank (e.g., "Novice", "Expert").
     */
    private Map<Integer, String> ranks = new HashMap<>();
}

// private int numRanks;
// subRanks ?