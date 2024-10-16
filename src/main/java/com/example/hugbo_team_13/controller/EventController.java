package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.service.EventService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")

public class EventController {

    private final EventService eventService;


    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
}