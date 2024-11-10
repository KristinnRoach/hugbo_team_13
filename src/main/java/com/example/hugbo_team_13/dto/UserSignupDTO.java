package com.example.hugbo_team_13.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for retrieving user signup information safely.
 * This DTO is used to encapsulate the user details required for account creation, including username, email, and password.
 * The validation annotations ensure that the fields meet the necessary criteria, such as non-blank values and proper email format.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserSignupDTO {

    /**
     * The username chosen by the user during signup.
     * It is a required field and cannot be blank.
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * The email address of the user.
     * It is a required field and must have a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The password chosen by the user during signup.
     * It is a required field and cannot be blank.
     */
    @NotBlank(message = "Password is required")
    private String password;
}
