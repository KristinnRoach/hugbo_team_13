package com.example.hugbo_team_13.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * Entity class representing an event in the application.
 * Mapped to the "event" table in the database.
 */
@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
public class EventEntity {

    /**
     * Unique identifier for the event.
     * Automatically generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the event.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The game associated with this event.
     * Many events can be associated with one game.
     */
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity starter;

    //  private String game;


    /**
     * The start date and time of the event.
     * Cannot be null.
     */
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    /**
     * The end date and time of the event.
     * Cannot be null.
     */
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @ManyToMany(mappedBy = "attendingEvents", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<UserEntity> attendees = new HashSet<>();

}
