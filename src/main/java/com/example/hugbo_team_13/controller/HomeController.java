package com.example.hugbo_team_13.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling requests to the home page of the application.
 */
@Controller
public class HomeController {

    /**
     * Handles GET requests to the home page.
     *
     * @return the name of the view for the home page
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
