package com.example.hugbo_team_13.controller;

import com.example.hugbo_team_13.model.UserDTO;
import com.example.hugbo_team_13.model.UserSignupDTO;
import com.example.hugbo_team_13.persistence.entity.UserEntity;
import com.example.hugbo_team_13.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long id) {
        byte[] image = userService.getProfilePicture(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @RequestMapping(value = "/userHome", method = RequestMethod.GET)
    public String userhomeGET(HttpSession session, Model model){
        UserEntity sessionUser = (UserEntity) session.getAttribute("LoggedInUser");
        if(sessionUser != null){
            model.addAttribute("LoggedInUser", sessionUser);
            return "userHome";
        }
        return "redirect:/";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String loginGET(UserEntity user){
        return "login";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String loginPOST(UserEntity user, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            return "login";
        }
        UserEntity exists = userService.login(user);
        if(exists != null){
            session.setAttribute("LoggedInUser", exists);
            model.addAttribute("LoggedInUser", exists);
            return "userHome";
        }
        return "redirect:";
    }

    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public String signupPOST(UserEntity user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "redirect:/signup";
        }
        UserDTO exists = userService.getUserByUsername(user.getUsername());
        if(exists == null){
            userService.saveUser(exists);
        }
        return "redirect:/";
    }
}


// import org.springframework.ui.Model;
// Model model) { // model.addAttribute("userDTO", userDTO); // for testing in view - remove if not needed
