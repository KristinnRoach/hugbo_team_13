package com.example.hugbo_team_13.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Platform is required")
    private String platform;

    @NotBlank(message = "At least 1 skill level is required")
    private String skill;
}