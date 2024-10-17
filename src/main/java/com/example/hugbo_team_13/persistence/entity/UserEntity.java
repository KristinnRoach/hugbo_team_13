package com.example.hugbo_team_13.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user") 
@Getter @Setter @NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // eða GenerationType.IDENTITY ?
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @ElementCollection
    @CollectionTable(name = "user_game_rankings", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyJoinColumn(name = "game_id")
    @Column(name = "rank")
    private Map<GameEntity, String> gameRankings = new HashMap<>();


    @Column
    @Lob
    private byte[] profilePicture;

    // Parameterized constructor
    public UserEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
