package com.example.hugbo_team_13.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) representing an event in the application.
 * This class is used to transfer event-related data between different layers of the application.
 */
@Getter
@Setter
@NoArgsConstructor
public class EventDTO {

    /**
     * Unique identifier for the event.
     */
    private Long id;

    /**
     * The name of the event.
     */
    private String name;

    /**
     * The start date of the event.
     */
    private LocalDate startDate;

    /**
     * The end date of the event.
     */
    private LocalDate endDate;

    /**
     * The start time of the event.
     */
    private LocalTime startTime;

    /**
     * The end time of the event.
     */
    private LocalTime endTime;

    /**
     * Parameterized constructor for creating an EventDTO with all properties.
     *
     * @param id        The unique identifier for the event.
     * @param name      The name of the event.
     * @param startDate The start date of the event.
     * @param endDate   The end date of the event.
     * @param startTime The start time of the event.
     * @param endTime   The end time of the event.
     */
    public EventDTO(Long id, String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
