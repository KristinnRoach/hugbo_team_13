package com.example.hugbo_team_13.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("loggedInUser")
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
