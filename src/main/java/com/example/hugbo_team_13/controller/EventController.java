package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/event")
@SessionAttributes("loggedInUser")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/create")
    public String getCreateEvent(Model model) {
        model.addAttribute("event", new EventDTO());
        return "event/create";
    }

    @GetMapping("/list")
    public String getEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "event/list";
    }

}
