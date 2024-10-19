package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.model.EventDTO;
import com.example.hugbo_team_13.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing events in the application.
 * Provides endpoints for creating, retrieving, and deleting events.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructs an EventController with the specified EventService.
     *
     * @param eventService the service used to manage events
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new event.
     *
     * @param eventEntity the details of the event to be created
     * @return a ResponseEntity containing the created EventDTO and a status of CREATED
     */
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventEntity) {
        EventDTO eventDTO = eventService.createEvent(eventEntity);
        return new ResponseEntity<>(eventDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all events.
     *
     * @return a ResponseEntity containing a list of EventDTOs and a status of OK
     */
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param id the ID of the event to retrieve
     * @return a ResponseEntity containing the EventDTO if found, or NOT FOUND status if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id the ID of the event to delete
     * @return a ResponseEntity with a NO CONTENT status if the event was deleted, or NOT FOUND status if the event does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> {
                    eventService.deleteEvent(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes all events.
     *
     * @return a ResponseEntity with a NO CONTENT status after deleting all events
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllEvents() {
        eventService.deleteAllEvents();
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}

