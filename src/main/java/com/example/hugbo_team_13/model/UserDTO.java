package com.example.hugbo_team_13.model;


/*
    Data Transfer Object with user info that can be safely used and sent to the client.
    No password (no id?)
*/

public class UserDTO {

    private Long id; // remove?
    private String username;
    private String email;
    private byte[] profilePicture;


    // Default NoArgs Constructor
    public UserDTO() {
    }

    // Constructor for creating DTO from entity (used when sending data to client)
    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Setters - no setter for id !!
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
