package com.example.hugbo_team_13.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing an event in the application.
 * This class is used to transfer event-related data between different layers of the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    /**
     * Unique identifier for the event.
     */
    public String id;

    /**
     * The name of the event.
     */
    public String name;

    /**
     * The start date of the event.
     */
    public LocalDate startDate;

    /**
     * The end date of the event.
     */
    public LocalDate endDate;

    /**
     * The start time of the event.
     */
    public LocalTime startTime;

    /**
     * The end time of the event.
     */
    public LocalTime endTime;

    private Set<UserDTO> attendees = new HashSet<>();  // Add this


}
