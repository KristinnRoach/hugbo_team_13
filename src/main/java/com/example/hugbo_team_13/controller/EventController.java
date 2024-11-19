package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.service.EventService;
import com.example.hugbo_team_13.service.GameService;
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
    private final GameService gameService;

    /**
     * Constructs an EventController with the specified EventService and UserService.
     *
     * @param eventService the service handling event operations
     * @param userService  the service handling user operations
     */
    public EventController(EventService eventService, UserService userService, GameService gameService) {
        this.eventService = eventService;
        this.userService = userService;
        this.gameService = gameService;
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
        EventDTO event = new EventDTO();
        GameDTO game = new GameDTO();
        // if redirecting from game/create, auto fill the game name
        if (session.getAttribute("gameDoesNotExist") != null) {
            game = (GameDTO) session.getAttribute("gameDoesNotExist");
            session.removeAttribute("gameDoesNotExist");
        }
        event.setGame(game);
        model.addAttribute("event", event);
        return "event/create";
    }

    /**
     * Displays the search event form, allowing users to filter events by date range or game.
     *
     * @param session the {@link HttpSession} object used to check if a user is logged in.
     * @param model   the {@link Model} object used to add attributes to the view.
     * @return the name of the view template for the search event form:
     * <ul>
     *     <li>If the user is not logged in, redirects to the login page (`/user/login`).</li>
     *     <li>Otherwise, returns the view name for the search event form (`event/search-event`).</li>
     * </ul>
     */
    @GetMapping("/search-event")
    public String getSearchEventForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("event", new EventDTO());

        List<GameDTO> games = gameService.getAllGames();
        model.addAttribute("games", games);

        return "event/search-event";
    }

    /**
     * Retrieves search results based on optional filters such as date range and game ID.
     * Processes the input parameters to generate a list of matching events and passes the results to the view.
     *
     * @param startDate the start date for the search filter (optional); can be {@code null}.
     * @param endDate   the end date for the search filter (optional); can be {@code null}.
     * @param id        the game ID as a {@link String} for filtering events (optional); can be {@code null} or empty.
     * @param model     the {@link Model} object used to add attributes to the view.
     * @return the name of the view template for the search results page (`event/search-results`).
     */
    @GetMapping("/search-results")
    public String getSearchResults(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "gameId", required = false) String id,
            Model model) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();  // Set start of the day if startDate is not null
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);  // Set end of the day if endDate is not null
        }
        Long gameIdLong = (id != null && !id.isEmpty()) ? Long.parseLong(id) : null;

        List<EventDTO> events = eventService.findEvents(startDateTime, endDateTime, gameIdLong);

        // Add events and games to the model

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
    public String createEvent(@ModelAttribute("event") EventDTO event, HttpSession session, Model model) {
        // check if game exists in db
        if (gameService.getGameByName(event.getGame().getName()) == null) {
            session.setAttribute("gameDoesNotExist", event.getGame());
            return "redirect:/game/create";
        }
        try {
            eventService.createEvent(event);
            session.removeAttribute("gameDoesNotExist"); // remove if exists
            return "redirect:/event/list";
        } catch (Exception ex) { // ResourceAlreadyExistsException
            model.addAttribute("error", ex); // getMessage()
            return "event/create";  // Stay on form with error message
        }
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
    public String updateEvent(@PathVariable("id") String id, @ModelAttribute("event") EventDTO event, Model model) {
        event.setId(id);
        try {
            eventService.saveEvent(event);
            return "redirect:/event/list";
        } catch (Exception ex) { // (ResourceAlreadyExistsException ex) {
            model.addAttribute("error", ex.getMessage());
            return "event/edit-event";  // Stay on form with error message
        }
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
