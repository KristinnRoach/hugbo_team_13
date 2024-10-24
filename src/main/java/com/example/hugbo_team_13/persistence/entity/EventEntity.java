package com.example.hugbo_team_13.persistence.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToMany(mappedBy = "attendingEvents")
    private Set<UserEntity> attendees = new HashSet<>();

}
