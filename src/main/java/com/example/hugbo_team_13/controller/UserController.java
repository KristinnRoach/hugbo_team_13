package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.model.UserDTO;
import com.example.hugbo_team_13.model.UserSignupDTO;
import com.example.hugbo_team_13.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserSignupDTO signupDTO) {
        UserDTO userDTO = userService.createUser(signupDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


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


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<Void> uploadProfilePicture(@PathVariable Long id,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        userService.updateProfilePicture(id, imageBytes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}


// import org.springframework.ui.Model;
// Model model) { // model.addAttribute("userDTO", userDTO); // for testing in view - remove if not needed
