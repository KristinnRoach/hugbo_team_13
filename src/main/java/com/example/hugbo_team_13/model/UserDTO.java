package com.example.hugbo_team_13.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 
    Data Transfer Object with user info that can be safely used and sent to the client. 
    No password (no id?)
*/
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private byte[] profilePicture;

    // Parameterized constructor
    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}