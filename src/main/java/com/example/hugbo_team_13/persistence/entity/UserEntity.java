package com.example.hugbo_team_13.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity class representing a user in the application.
 * Mapped to the "app_user" table in the database.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    /**
     * Unique identifier for the user.
     * Automatically generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String username;

    /**
     * The email of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String email;

    /**
     * The hashed password of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * A map of game rankings associated with the user.
     * The key is the {@link GameEntity}, and the value is the rank as a string.
     * Stored in a separate table "user_game_rankings".
     */
    @ElementCollection
    @CollectionTable(name = "user_game_rankings", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyJoinColumn(name = "game_id")
    @Column(name = "rank")
    private Map<GameEntity, String> gameRankings = new HashMap<>();

    /**
     * The profile picture of the user.
     * Stored as a byte array.
     */
    @Column
    @Lob
    private byte[] profilePicture;

    /**
     * Parameterized constructor for creating a new user.
     *
     * @param username the username of the user.
     * @param email    the email of the user.
     */
    public UserEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
