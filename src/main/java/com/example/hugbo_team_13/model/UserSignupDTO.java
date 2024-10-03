package com.example.hugbo_team_13.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 
    Data Transfer Object for retrieving signup info safely. 
*/

@Getter @Setter @NoArgsConstructor
public class UserSignupDTO {

    private String username;
    private String email;
    private String password;
}

