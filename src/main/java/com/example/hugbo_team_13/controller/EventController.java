package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/event")
// @SessionAttributes("loggedInUser")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/list")
    public String getEvents(Model model) {
        List<EventDTO> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "event/list";
    }


    @GetMapping("/create")
    public String getEventForm(Model model) {
        model.addAttribute("event", new EventDTO());
        return "event/create";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute("event") EventDTO eventDTO) {
        eventService.createEvent(eventDTO);
        return "redirect:/event/list";
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable("id") long id) {
        Optional<EventDTO> event = eventService.getEventById(id);
        if (event.isEmpty()) {
            return "event/notFound";
        }
        eventService.deleteEvent(id);
        return "redirect:/event/list";
    }


}
