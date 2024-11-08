package com.example.hugbo_team_13.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for user login.
 * This DTO is used to encapsulate the login credentials for the user, including the username and password.
 * The validation annotations ensure that both fields are not blank.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTO {

    /**
     * The username of the user.
     * It is a required field for login authentication.
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * The password of the user.
     * It is a required field for login authentication.
     */
    @NotBlank(message = "Password is required")
    private String password;
}
