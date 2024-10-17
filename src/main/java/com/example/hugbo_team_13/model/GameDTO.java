package com.example.hugbo_team_13.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameDTO {

    private Long id;
    private String name;
    private String platform;
    private String rank;

    public GameDTO(Long id, String name, String platform, String rank) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.rank = rank;
    }
}
