package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.dto.UserLoginDTO;
import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing users in the application.
 * Provides endpoints for creating, retrieving, updating, and deleting user accounts.
 */
@Controller
@RequestMapping("/user")
@SessionAttributes("loggedInUser")
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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new UserLoginDTO());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDTO") UserLoginDTO loginDTO,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        try {
            UserDTO user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            if (user != null) {
                session.setAttribute("loggedInUser", user);
                // model.addAttribute("loggedInUser", user); // unnecessary?
                return "redirect:/user/" + user.getId();
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "user/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "user/login";
        }
    }


    @PostMapping("/signup")
    public String createUser(@Valid @ModelAttribute("signupDTO") UserSignupDTO signupDTO, BindingResult bindingResult,  HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        try {
            UserDTO user = userService.createUser(signupDTO);
            session.setAttribute("loggedInUser", user);
            // model.addAttribute("loggedInUser", user); // unnecessary?
            return "redirect:/user/created";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "user/signup";
        }
    }

    @GetMapping("/created")
    public String showCreated(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);
        return "user/created";
    }

    @GetMapping("/list")
    public String getAllUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable String id, Model model) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/profile";
        } else {
            return "user/notFound";
        }
    }

    @GetMapping("/edit")
    public String getUpdateForm(Model model, HttpSession session) {
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");
        Optional<UserDTO> user = userService.getUserById(loggedInUser.getId());

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "editProfile";
        } else {
            return "user/notFound"; // todo: userNotLoggedIn
        }
    }

    @PutMapping("/{id}")
    public String updateProfile(@PathVariable String id, @ModelAttribute UserDTO user, HttpSession session, Model model) {
        try {
            Optional<UserDTO> existingUser = userService.getUserById(id);
            if (existingUser.isEmpty()) {
                return "user/notFound";
            }
            UserDTO updatedUser = userService.updateUser(id, user);
            session.setAttribute("loggedInUser", updatedUser);
            return "redirect:/user/" + updatedUser.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session, SessionStatus sessionStatus, RedirectAttributes attrs) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return "user/notFound";
        }
        session.setAttribute("loggedInUser", null);
        sessionStatus.setComplete();
        session.invalidate();

        userService.deleteUser(id);

        attrs.addFlashAttribute("message", "User deleted");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus) {
        session.setAttribute("loggedInUser", null);
        sessionStatus.setComplete();
        session.invalidate();
        return "redirect:/";
    }


    @PostMapping("/{id}/profile-picture")
    public String uploadProfilePicture(@PathVariable String id,
                                       @RequestParam("file") MultipartFile file,
                                       Model model) throws IOException {
        byte[] imageBytes = file.getBytes();
        userService.updateProfilePicture(id, imageBytes);
        model.addAttribute("message", "Profile picture updated successfully");
        return "user/profilePictureUpdated";
    }

    /*
    @GetMapping("/{id}/profile-picture")
    public String getProfilePicture(@PathVariable String id, Model model) {
        byte[] image = userService.getProfilePicture(id);
        model.addAttribute("profilePicture", image);
        return "user/profilePicture";
    }

        @PostMapping("/delete-all")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "redirect:/";
    }
    */

}