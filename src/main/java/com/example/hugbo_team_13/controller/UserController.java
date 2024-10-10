package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.model.UserDTO;
import com.example.hugbo_team_13.model.UserSignupDTO;
import com.example.hugbo_team_13.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Todo: update to work with DTO's
    
    // @PutMapping("/{id}")
    // public ResponseEntity<UserDTO> resetUser(@PathVariable Long id, @RequestBody UserSignupDTO signupDTO) {
    //     return userService.getUserById(id)
    //             .map(existingUser -> {
    //                 user.setId(id);
    //                 UserDTO updatedUser = userService.saveUser(user);
    //                 return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    //             })
    //             .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}



// import org.springframework.ui.Model;
// Model model) { // model.addAttribute("userDTO", userDTO); // for testing in view - remove if not needed