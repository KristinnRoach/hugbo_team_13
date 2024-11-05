package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.service.EventService;
import com.example.hugbo_team_13.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }


    @GetMapping("/list")
    public String getEvents(Model model) {
        List<EventDTO> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "event/list";
    }


    @GetMapping("/create")
    public String getEventForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("event", new EventDTO());
        return "event/create";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute("event") EventDTO event) {
        eventService.createEvent(event);
        return "redirect:/event/list";
    }

    @GetMapping("/{id}")
    public String getEventPage(@PathVariable("id") String id, Model model) {
        EventDTO event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        return "event/event-page";
    }

    @PostMapping("/{id}/attend")
    public String attendEvent(@PathVariable String id, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        EventDTO event = eventService.getEventById(id).orElseThrow();
        userService.attendEvent(user.getId(), event.getId());

        return "redirect:/event/" + id;
    }

    @GetMapping("/{id}/attending")
    public String showAttendees(@PathVariable String id, Model model) {
        EventDTO event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        model.addAttribute("attendees", event.getAttendees());
        return "event/attending";
    }

    @GetMapping("/{id}/edit")
    public String getEditEventPage(@PathVariable("id") String id, Model model) {
        EventDTO event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        return "event/edit-event";
    }


    @PutMapping("/{id}")
    public String updateEvent(@PathVariable("id") String id, @ModelAttribute("event") EventDTO event) {
        event.setId(id);
        eventService.saveEvent(event);
        return "redirect:/event/list";
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable("id") String id) {
        Optional<EventDTO> event = eventService.getEventById(id);
        if (event.isEmpty()) {
            return "event/notFound";
        }
        eventService.deleteEvent(id);
        return "redirect:/event/list";
    }
}
