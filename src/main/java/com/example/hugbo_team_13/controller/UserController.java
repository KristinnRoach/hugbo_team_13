package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing users in the application.
 * Provides endpoints for creating, retrieving, updating, and deleting user accounts.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupDTO", new UserSignupDTO());
        return "user/signup";
    }


    @PostMapping("/signup")
    public String createUser(@Valid @ModelAttribute("signupDTO") UserSignupDTO signupDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        try {
            UserDTO userDTO = userService.createUser(signupDTO);
            model.addAttribute("user", userDTO);
            return "user/created";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "user/signup";
        }
    }


        /*
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserSignupDTO signupDTO) {
        try {
            UserDTO userDTO = userService.createUser(signupDTO);
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    } */

    @GetMapping("/list")
    public String getAllUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/details";
        } else {
            return "user/notFound";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUserInfo(@PathVariable Long id, @ModelAttribute UserDTO userDTO, Model model) {
        try {
            Optional<UserDTO> existingUser = userService.getUserById(id);
            if (existingUser.isEmpty()) {
                return "user/notFound";
            }
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            model.addAttribute("user", updatedUser);
            return "user/updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return "user/notFound";
        }
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "redirect:/users";
    }

    @PostMapping("/{id}/profile-picture")
    public String uploadProfilePicture(@PathVariable Long id,
                                       @RequestParam("file") MultipartFile file,
                                       Model model) throws IOException {
        byte[] imageBytes = file.getBytes();
        userService.updateProfilePicture(id, imageBytes);
        model.addAttribute("message", "Profile picture updated successfully");
        return "user/profilePictureUpdated";
    }

    /*
    @GetMapping("/{id}/profile-picture")
    public String getProfilePicture(@PathVariable Long id, Model model) {
        byte[] image = userService.getProfilePicture(id);
        model.addAttribute("profilePicture", image);
        return "user/profilePicture";
    }
    */

}