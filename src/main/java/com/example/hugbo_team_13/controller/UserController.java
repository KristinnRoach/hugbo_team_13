package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.dto.UserLoginDTO;
import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * Provides endpoints for user signup, login, profile viewing, editing, and deletion.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the specified UserService.
     *
     * @param userService the service handling user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the signup form for new users.
     *
     * @param model the model to hold a new UserSignupDTO
     * @return the view name for the signup page
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupDTO", new UserSignupDTO());
        return "user/signup";
    }

    /**
     * Displays the login form.
     *
     * @param model the model to hold a new UserLoginDTO
     * @return the view name for the login page
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new UserLoginDTO());
        return "user/login";
    }

    /**
     * Processes login requests.
     *
     * @param loginDTO      the UserLoginDTO containing login credentials
     * @param bindingResult the result of binding the login data
     * @param session       the current HTTP session
     * @param model         the model to hold error messages
     * @return a redirect to the user profile page or back to the login page if login fails
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDTO") UserLoginDTO loginDTO,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        try {
            UserDTO user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            if (user != null) {
                session.setAttribute("loggedInUser", user);
                return "redirect:/";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "user/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "user/login";
        }
    }

    /**
     * Processes signup requests for creating a new user.
     *
     * @param signupDTO     the UserSignupDTO containing signup information
     * @param bindingResult the result of binding the signup data
     * @param session       the current HTTP session
     * @param model         the model to hold error messages
     * @return a redirect to the user-created page if successful
     */
    @PostMapping("/signup")
    public String createUser(@Valid @ModelAttribute("signupDTO") UserSignupDTO signupDTO,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        try {
            UserDTO user = userService.createUser(signupDTO);
            if (user != null) {
                session.setAttribute("loggedInUser", user);
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "user/signup";
        }
        return "redirect:/user/created";
    }

    /**
     * Displays a confirmation page for a successfully created user.
     *
     * @param session the current HTTP session
     * @param model   the model to hold logged-in user data
     * @return the view name for the user-created page
     */
    @GetMapping("/created")
    public String showCreated(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);
        return "user/created";
    }

    /**
     * Displays a list of all users.
     *
     * @param session the current HTTP session
     * @param model   the model to hold user data
     * @return the view name for the user list page
     */
    @GetMapping("/list")
    public String getAllUsers(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user/login";
        }
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    /**
     * Displays a specific user's profile page.
     *
     * @param id    the ID of the user
     * @param model the model to hold user data
     * @return the view name for the user profile page or not found page if user does not exist
     */
    @GetMapping("/{id}")
    public String getUserPage(@PathVariable String id, Model model) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(value -> {
            model.addAttribute("user", value);
            return "user/profile";
        }).orElse("user/notFound");
    }

    /**
     * Displays the edit profile form for a user.
     *
     * @param id    the ID of the user
     * @param model the model to hold user data
     * @return the view name for the edit profile page or not found page if user does not exist
     */
    @GetMapping("/{id}/edit")
    public String getEditProfileForm(@PathVariable String id, Model model) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(value -> {
            model.addAttribute("user", value);
            return "user/edit-profile";
        }).orElse("user/notFound");
    }

    /**
     * Updates a user's profile.
     *
     * @param id      the ID of the user
     * @param user    the updated UserDTO
     * @param session the current HTTP session
     * @param model   the model to hold error messages
     * @return a redirect to the user's profile page or the error page if update fails
     */
    @PutMapping("/{id}")
    public String updateProfile(@PathVariable String id,
                                @ModelAttribute UserDTO user,
                                HttpSession session,
                                Model model) {
        try {
            UserDTO updatedUser = userService.updateUser(id, user);
            session.setAttribute("loggedInUser", updatedUser);
            return "redirect:/user/" + updatedUser.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


    /**
     * Deletes a user account.
     *
     * @param id             the ID of the user
     * @param session        the current HTTP session
     * @param sessionStatus  the session status for marking the session complete
     * @param attrs          redirect attributes for success messages
     * @return a redirect to the home page
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id,
                             HttpSession session,
                             SessionStatus sessionStatus,
                             RedirectAttributes attrs) {
        if (userService.getUserById(id).isEmpty()) {
            return "user/notFound";
        }
        session.invalidate();
        sessionStatus.setComplete();
        session.invalidate();

        userService.deleteUser(id);

        attrs.addFlashAttribute("message", "User deleted");
        return "redirect:/";
    }

    /**
     * Logs out the currently logged-in user by invalidating the session.
     *
     * @param session       the current HTTP session
     * @param sessionStatus the session status for marking the session as complete
     * @return a redirect to the home page
     */
        @PostMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus) {
        session.setAttribute("loggedInUser", null);
        sessionStatus.setComplete();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping(value = "/{id}/profile-picture",  produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String id) {
        UserDTO user = userService.getUserById(id).orElseThrow();
        if (user.getProfilePicture() != null) {
            // Get the stored content type, or default to JPEG if not available
            MediaType mediaType = MediaType.IMAGE_JPEG;
           if (user.getProfilePictureType() != null) {
               mediaType = MediaType.parseMediaType(user.getProfilePictureType());
           }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(user.getProfilePicture());
    }
    return ResponseEntity.notFound().build();
}

    @PostMapping("/{id}/profile-picture")
    public String updateProfilePicture(@PathVariable String id,
                                       @RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfilePicture(id, file);
            redirectAttributes.addFlashAttribute("message", "Profile picture updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile picture.");
        }
        return "redirect:/user/" + id + "/edit";
    }

}
