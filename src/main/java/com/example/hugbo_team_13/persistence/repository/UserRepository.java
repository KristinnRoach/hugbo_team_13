package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing {@link UserEntity} objects in the database.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 * Includes custom queries for finding users by username and checking for existence by username.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * Finds a {@link UserEntity} by its username.
     * 
     * @param username the username of the user.
     * @return the {@link UserEntity} associated with the given username, or null if no user is found.
     */
    UserEntity findByUsername(String username); 

    /**
     * Checks if a user exists in the database with the specified username.
     * 
     * @param username the username to check for.
     * @return true if a user with the specified username exists, false otherwise.
     */
    boolean existsByUsername(String username);

}

