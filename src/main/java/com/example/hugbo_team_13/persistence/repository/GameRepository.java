package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link GameEntity} objects in the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations for games.
 * Includes custom queries for finding games by name and checking for existence by name.
 */
@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    
    /**
     * Finds a {@link GameEntity} by its name.
     * 
     * @param name the name of the game.
     * @return the {@link GameEntity} associated with the given name, or null if no game is found.
     *         Consider returning an {@link Optional} to avoid potential null values.
     */
    GameEntity findByName(String name); // Optional<GameEntity> is a better option to avoid nulls.

    /**
     * Checks if a game exists in the database with the specified name.
     * 
     * @param name the name to check for.
     * @return true if a game with the specified name exists, false otherwise.
     */
    boolean existsByName(String name);
}
