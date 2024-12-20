package com.example.hugbo_team_13.service;

import com.example.hugbo_team_13.dto.GameDTO;
import com.example.hugbo_team_13.dto.UserDTO;
import com.example.hugbo_team_13.dto.UserSignupDTO;
import com.example.hugbo_team_13.helper.PictureData;
import com.example.hugbo_team_13.persistence.entity.EventEntity;
import com.example.hugbo_team_13.persistence.entity.GameEntity;
import com.example.hugbo_team_13.persistence.entity.UserEntity;
import com.example.hugbo_team_13.persistence.repository.EventRepository;
import com.example.hugbo_team_13.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
    private final EventRepository eventRepository;

    /**
     * Constructor to inject the UserRepository dependency.
     *
     * @param userRepository the repository to handle user data.
     */
    public UserService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Creates a new user based on the provided UserSignupDTO.
     * Validates the username to ensure it is not empty and does not already exist.
     *
     * @param signupDTO the data transfer object (DTO) representing the user's sign-up details.
     * @return the created UserDTO object.
     * @throws IllegalArgumentException if the username is empty.
     * @throws RuntimeException         if a user with the same username already exists.
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
    public Optional<UserDTO> getUserById(String id) {
        return userRepository.findById(Long.parseLong(id)).map(this::convertToDTO);
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
     * @param id     the ID of the user to update.
     * @param newDTO the updated user data.
     * @return the updated UserDTO.
     * @throws IllegalArgumentException if the user is not found.
     */
    public UserDTO updateUser(String id, UserDTO newDTO) {

        UserEntity user = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (newDTO.getUsername() != null) {
            user.setUsername(newDTO.getUsername());
        }
        if (newDTO.getEmail() != null) {
            user.setEmail(newDTO.getEmail());
        }
        if (newDTO.getPassword() != null) {
            user.setPassword(newDTO.getPassword());
        }


        // note: the profile picture is handled in updateProfilePicture

        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public Optional<PictureData> getProfilePicture(String id) {
        return userRepository.findById(Long.parseLong(id))
                .filter(user -> user.getProfilePicture() != null)
                .map(user -> new PictureData(user.getProfilePicture(),
                        user.getProfilePictureType()));
    }

    public void updateProfilePicture(String id, MultipartFile file) throws IOException {
        userRepository.findById(Long.parseLong(id)).ifPresent(user -> {
            try {
                user.setProfilePicture(file.getBytes());
                user.setProfilePictureType(file.getContentType());
                userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process file", e);
            }
        });
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
     * Deletes every reference other users have to this one as their friend
     *
     * @param id the ID of the user to delete.
     */
    @Transactional
    public void deleteUser(String id) {
        UserEntity user = userRepository.findById(Long.parseLong(id)).orElseThrow();
        for (UserEntity friend : new HashSet<>(user.getFriends())) {
            friend.getFriends().remove(user); // Remove this user from each friend's list
        }
        user.getFriends().clear(); // Clear this user's list of friends
        userRepository.deleteById(Long.parseLong(id));
    }


    /**
     * Deletes all users from the repository.
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }



    public void attendEvent(String userID, String eventID) {
        if (userID.isEmpty() || eventID.isEmpty()) { return; }

        UserEntity user = userRepository.findById(Long.parseLong(userID)).orElseThrow();
        EventEntity event = eventRepository.findById(Long.parseLong(eventID)).orElseThrow();

        user.attendEvent(event);
        userRepository.save(user);
    }

    public void cancelAttendance(String userID, String eventID) {
        if (userID.isEmpty() || eventID.isEmpty()) { return; }

        UserEntity user = userRepository.findById(Long.parseLong(userID)).orElseThrow();
        EventEntity event = eventRepository.findById(Long.parseLong(eventID)).orElseThrow();

        user.cancelEventAttendance(event);
        userRepository.save(user);
    }

    /**
     * Converts a UserEntity to a UserDTO.
     *
     * @param user the UserEntity to convert.
     * @return the corresponding UserDTO.
     */
    public UserDTO convertToDTO(UserEntity user) {
        String strId = user.getId().toString();
        UserDTO dto = new UserDTO();
        dto.setId(strId);
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFriends(user.getFriends());

        if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
            dto.setProfilePicture(user.getProfilePicture());
            dto.setProfilePictureType(user.getProfilePictureType());
        }

        return dto;
    }

    public boolean isFriend(Long userId, Long friendId) {
        // Fetch the user by ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the friend by ID
        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        // Check if the user is following the friend
        return user.getFriends().contains(friend);
    }

    public void addFriend(Long userId, Long friendId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Following user not found"));

        user.friend(friend);
        userRepository.save(user);
    }

    public void removeFriend(Long userId, Long friendId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Following user not found"));

        user.unfriend(friend);
        userRepository.save(user);
    }

    public List<UserDTO> getAllFriends(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getFriends();
        List<UserDTO> listi = new ArrayList<>();
        for(UserEntity entity : user.getFriends()){
            listi.add(convertToDTO(entity));
        }
        return listi;
    }

    /**
     * Converts a UserDTO to a UserEntity.
     *
     * @param userDTO the UserDTO to convert.
     * @return the corresponding UserEntity.
     */
    public UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity(userDTO.getUsername(), userDTO.getEmail());
        Long longStr = Long.parseLong(userDTO.getId());
        user.setId(longStr);
        user.setFriends(userDTO.getFriends());

        // Handle profile picture if present in DTO
        if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
            user.setProfilePicture(user.getProfilePicture());
        }

        return user;
    }

    /*
    private void attendEvent(String eventID) {
        attendingEvents.add(event);
        event.getAttendees().add(this);
    }

    private void cancelEventAttendance(EventEntity event) {
        attendingEvents.remove(event);
        event.getAttendees().remove(this);
    }
*/
}

