package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users in the application.
 * Provides endpoints for creating, retrieving, updating, and deleting user accounts.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the specified UserService.
     *
     * @param userService the service used to manage users
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user account.
     *
     * @param signupDTO the details of the user to be created
     * @return a ResponseEntity containing the created UserDTO and a status of CREATED
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserSignupDTO signupDTO) {
        UserDTO userDTO = userService.createUser(signupDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves all user accounts.
     *
     * @return a ResponseEntity containing a list of UserDTOs and a status of OK
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieves a user account by its ID.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the UserDTO if found, or a NOT FOUND status if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Updates the information of an existing user account.
     *
     * @param id the ID of the user to update
     * @param userDTO the updated user information
     * @return a ResponseEntity containing the updated UserDTO, or NOT FOUND if the user does not exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserInfo(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            Optional<UserDTO> existingUser = userService.getUserById(id);

            if (existingUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            UserDTO updatedUser = userService.updateUser(id, userDTO);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a user account by its ID.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity with a NO CONTENT status if the user was deleted, or NOT FOUND if the user does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes all user accounts.
     *
     * @return a ResponseEntity with a NO CONTENT status after deleting all users
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Uploads a profile picture for a user.
     *
     * @param id the ID of the user whose profile picture is being uploaded
     * @param file the profile picture file to upload
     * @return a ResponseEntity with an OK status if the upload is successful
     * @throws IOException if an error occurs while processing the uploaded file
     */
    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<Void> uploadProfilePicture(@PathVariable Long id,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        userService.updateProfilePicture(id, imageBytes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves the profile picture of a user.
     *
     * @param id the ID of the user whose profile picture is to be retrieved
     * @return a ResponseEntity containing the profile picture as a byte array and an OK status
     */
    @GetMapping("/{id}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long id) {
        byte[] image = userService.getProfilePicture(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}


// import org.springframework.ui.Model;
// Model model) { // model.addAttribute("userDTO", userDTO); // for testing in view - remove if not needed
