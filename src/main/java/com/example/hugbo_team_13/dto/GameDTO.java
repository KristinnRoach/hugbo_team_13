package com.example.hugbo_team_13.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GameDTO {

    private Long id;
    private String name;
    private String platform;
    private Map<Integer, String> ranks = new HashMap<>();


    // Parameterized constructor
    public GameDTO(Long id, String name, String platform, Map<Integer, String> ranks) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.ranks = ranks;
    }
}

// private UserEntity[] players;

