package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.persistence.entity.EventEntity;
import com.example.hugbo_team_13.service.EventService;
import com.example.hugbo_team_13.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing events in the application.
 * Provides endpoints for viewing, creating, updating, and deleting events.
 */
@Controller
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    /**
     * Constructs an EventController with the specified EventService and UserService.
     *
     * @param eventService the service handling event operations
     * @param userService  the service handling user operations
     */
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to display a list of all events.
     *
     * @param model the model to hold event data
     * @return the name of the view for the event list page
     */
    @GetMapping("/list")
    public String getEvents(Model model) {
        List<EventDTO> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "event/list";
    }

    /**
     * Handles GET requests for the event creation form.
     * Redirects to the login page if no user is logged in.
     *
     * @param session the current HTTP session
     * @param model   the model to hold a new EventDTO
     * @return the name of the view for the event creation page
     */

    @GetMapping("/create")
    public String getEventForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInUser") == null) {
            redirectAttributes.addFlashAttribute("error", "Please login before creating an event.");
            model.addAttribute("prevPage", "/event/create");
            return "redirect:/user/login";
        }
        model.addAttribute("event", new EventDTO());
        return "event/create";
    }

    @GetMapping("/search-event")
    public String getSearchEventForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("event", new EventDTO());
        return "event/search-event";
    }

    @GetMapping("/search-results")
    public String getSearchResults(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);


        List<EventEntity> events = eventService.findEventsByDateRange(startDateTime, endDateTime);

        // Add events to the model
        model.addAttribute("events", events);

        return "event/search-results";  // View name for the search results page
    }

    /**
     * Handles POST requests for creating a new event.
     *
     * @param event the EventDTO representing the new event
     * @return a redirect to the event list page
     */

    @PostMapping("/create")

    public String createEvent(@ModelAttribute("event") EventDTO event) {
        eventService.createEvent(event);
        return "redirect:/event/list";
    }


    /**
     * Handles GET requests to display a specific event by ID.
     *
     * @param id    the ID of the event
     * @param model the model to hold event data
     * @return the name of the view for the event detail page
     */
    @GetMapping("/{id}")
    public String getEventPage(@PathVariable("id") String id, Model model) {
        EventDTO event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        return "event/event-page";
    }

    /**
     * Handles POST requests for a user to attend a specific event by ID.
     * Redirects to the login page if no user is logged in.
     *
     * @param id      the ID of the event
     * @param session the current HTTP session
     * @return a redirect to the event detail page
     */
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

    /**
     * Handles GET requests to display the list of attendees for a specific event.
     *
     * @param id    the ID of the event
     * @param model the model to hold event and attendee data
     * @return the name of the view for the event attendees page
     */
    @GetMapping("/{id}/attending")
    public String showAttendees(@PathVariable String id, Model model) {
        EventDTO event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        model.addAttribute("attendees", event.getAttendees());
        return "event/attending";
    }

    /**
     * Handles GET requests for the event edit form for a specific event by ID.
     *
     * @param id    the ID of the event
     * @param model the model to hold event data
     * @return the name of the view for the event edit page
     */
    @GetMapping("/{id}/edit")
    public String getEditEventPage(@PathVariable("id") String id, Model model) {
        EventDTO event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        return "event/edit-event";
    }

    /**
     * Handles PUT requests to update a specific event by ID.
     *
     * @param id    the ID of the event to update
     * @param event the updated EventDTO
     * @return a redirect to the event list page
     */
    @PutMapping("/{id}")
    public String updateEvent(@PathVariable("id") String id, @ModelAttribute("event") EventDTO event) {
        event.setId(id);
        eventService.saveEvent(event);
        return "redirect:/event/list";
    }

    /**
     * Handles DELETE requests to delete a specific event by ID.
     *
     * @param id the ID of the event to delete
     * @return a redirect to the event list page if successful, or an error page if the event is not found
     */
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
