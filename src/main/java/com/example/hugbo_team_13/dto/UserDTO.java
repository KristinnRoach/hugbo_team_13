package com.example.hugbo_team_13.dto;

import com.example.hugbo_team_13.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a user.
 * This DTO contains user details that can be safely sent to the client, excluding sensitive information like the password.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * The unique identifier of the user.
     * Typically, a username or user ID.
     */
    public String id;

    /**
     * The username of the user.
     * Used for login and identification.
     */
    public String username;

    /**
     * The email address of the user.
     * Used for communication or notifications.
     */
    public String email;

    /**
     * The profile picture of the user, stored as a byte array.
     * Used for displaying the user's profile image in the UI.
    */
    public byte[] profilePicture;

    public String password;

    public String profilePictureType;

    public Set<UserEntity> friends = new HashSet<>();
}
