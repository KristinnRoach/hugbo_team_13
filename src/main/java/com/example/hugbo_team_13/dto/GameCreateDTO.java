package com.example.hugbo_team_13.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for creating a new game.
 * Includes validation constraints for required fields such as name, platform, and skill level.
 */
@Getter
@Setter
@NoArgsConstructor
public class GameCreateDTO {

    /**
     * The name of the game.
     * Must not be blank.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * The platform on which the game is available (e.g., PC, console).
     * Must not be blank.
     */
    @NotBlank(message = "Platform is required")
    private String platform;

    /**
     * The skill level required or relevant to the game.
     * Must not be blank.
     */
    @NotBlank(message = "At least 1 skill level is required")
    private String skill;
}
