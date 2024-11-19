package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.EventDTO;
import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.service.EventService;
import com.example.hugbo_team_13.service.GameService;
import com.example.hugbo_team_13.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for handling requests to the home page of the application.
 */
@Controller
public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Handles GET requests to the home page.
     *
     * @return the name of the view for the home page
     */
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") != null){
            UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
            long userId = Long.parseLong(user.getId());
            List<EventDTO> events = eventService.getEventsByUserId(userId);
            model.addAttribute("events", events);
        }
        return "home";
    }
}
