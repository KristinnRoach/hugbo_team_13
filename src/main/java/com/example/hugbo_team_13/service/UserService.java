package com.example.hugbo_team_13.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.hugbo_team_13.persistence.entity.User;
import com.example.hugbo_team_13.persistence.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String email) {
        User newUser = new User(username, email);
        return userRepository.save(newUser);
    }
    

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) { // save creates a new entity if ID is not set
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
}
