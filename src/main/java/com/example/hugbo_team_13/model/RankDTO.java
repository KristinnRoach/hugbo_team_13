package com.example.hugbo_team_13.model;

import com.example.hugbo_team_13.persistence.entity.GameEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
@NoArgsConstructor
public class RankDTO {

    private Long id;
    private GameDTO game;
    private Map<Integer, String> ranks = new HashMap<>();
    //private int numRanks;

    public RankDTO(GameDTO game) {
        this.game = game;
    }


}
