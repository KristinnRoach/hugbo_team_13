package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.model.UserDTO;
import com.example.hugbo_team_13.model.UserSignupDTO;
import com.example.hugbo_team_13.persistence.entity.UserEntity;
import com.example.hugbo_team_13.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserSignupDTO signupDTO) {
        // Todo: Validate input
        String username = signupDTO.getUsername();
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (userRepository.existsByUsername(username)) {
            // logger.warn("Attempted to create account with existing username: {}", username);
            throw new RuntimeException("Username already exists");
        }

        // Create UserEntity
        UserEntity user = new UserEntity(signupDTO.getUsername(), signupDTO.getEmail());
        user.setPasswordHash(signupDTO.getPassword()); // Todo: Hash!

        UserEntity savedUser = userRepository.save(user);

        // Return a new UserDTO
        return convertToDTO(savedUser);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    public UserDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null ? convertToDTO(user) : null;
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (UserEntity user : userEntities) {
            userDTOs.add(convertToDTO(user));
        }
        return userDTOs;
    }


    public UserDTO saveUser(UserDTO userDTO) { // (save creates a new entity if ID is not set)
        UserEntity user = convertToEntity(userDTO);
        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    private UserDTO convertToDTO(UserEntity user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    private UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity(userDTO.getUsername(), userDTO.getEmail());
        user.setId(userDTO.getId());
        return user;
    }

}
