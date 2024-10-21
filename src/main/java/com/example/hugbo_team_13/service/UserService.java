package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.persistence.entity.UserEntity;
import com.example.hugbo_team_13.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users in the system.
 * Provides methods to create, retrieve, update, and delete users using a UserRepository.
 * Also supports managing profile pictures.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor to inject the UserRepository dependency.
     * 
     * @param userRepository the repository to handle user data.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user based on the provided UserSignupDTO.
     * Validates the username to ensure it is not empty and does not already exist.
     * 
     * @param signupDTO the data transfer object (DTO) representing the user's sign-up details.
     * @return the created UserDTO object.
     * @throws IllegalArgumentException if the username is empty.
     * @throws RuntimeException if a user with the same username already exists.
     */
    public UserDTO createUser(UserSignupDTO signupDTO) {
        String username = signupDTO.getUsername();
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity user = new UserEntity(signupDTO.getUsername(), signupDTO.getEmail());
        user.setPassword(signupDTO.getPassword()); // Password should be hashed (Todo)

        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // todo: security
    public UserDTO login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return convertToDTO(user);
        }
        return null;
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param id the ID of the user.
     * @return an Optional containing the UserDTO if found, or empty if not.
     */
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * Retrieves a user by their username.
     * 
     * @param username the username of the user.
     * @return the UserDTO if found, or null if the user does not exist.
     */
    public UserDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null ? convertToDTO(user) : null;
    }

    /**
     * Retrieves all users from the repository.
     * 
     * @return a list of UserDTOs representing all users in the system.
     */
    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (UserEntity user : userEntities) {
            userDTOs.add(convertToDTO(user));
        }
        return userDTOs;
    }

    /**
     * Updates an existing user with new data.
     * 
     * @param id the ID of the user to update.
     * @param newUserData the updated user data.
     * @return the updated UserDTO.
     * @throws IllegalArgumentException if the user is not found.
     */
    public UserDTO updateUser(Long id, UserDTO newUserData) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        UserEntity user = userOptional.get();
        user.setUsername(newUserData.getUsername());
        user.setEmail(newUserData.getEmail());

        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Saves a user entity.
     * If the user ID is not set, it creates a new user; otherwise, it updates an existing user.
     * 
     * @param userDTO the data transfer object representing the user.
     * @return the saved UserDTO.
     */
    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity user = convertToEntity(userDTO);
        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Deletes a user by their ID.
     * 
     * @param id the ID of the user to delete.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Deletes all users from the repository.
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    /**
     * Updates the profile picture of a user.
     * 
     * @param id the ID of the user whose profile picture is to be updated.
     * @param profilePicture the new profile picture as a byte array.
     * @throws RuntimeException if the user is not found.
     */
    public void updateProfilePicture(Long id, byte[] profilePicture) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePicture(profilePicture);
        userRepository.save(user);
    }

    /**
     * Retrieves the profile picture of a user.
     * 
     * @param id the ID of the user whose profile picture is to be retrieved.
     * @return the profile picture as a byte array.
     * @throws RuntimeException if the user is not found.
     */
    public byte[] getProfilePicture(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProfilePicture();
    }

    /**
     * Converts a UserEntity to a UserDTO.
     * 
     * @param user the UserEntity to convert.
     * @return the corresponding UserDTO.
     */
    private UserDTO convertToDTO(UserEntity user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getProfilePicture());
    }

    /**
     * Converts a UserDTO to a UserEntity.
     * 
     * @param userDTO the UserDTO to convert.
     * @return the corresponding UserEntity.
     */
    private UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity(userDTO.getUsername(), userDTO.getEmail());
        user.setId(userDTO.getId());
        return user;
    }
}

