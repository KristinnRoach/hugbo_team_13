package com.example.hugbo_team_13.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/* 
    Data Transfer Object with user info that can be safely used and sent to the client. 
    No password (no id?)
*/
@Getter // to be removed if not needed
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    public String id;
    public String username;
    public String email;
    public byte[] profilePicture;  // for displaying

}