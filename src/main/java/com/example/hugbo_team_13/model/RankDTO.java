package com.example.hugbo_team_13.model;

import com.example.hugbo_team_13.persistence.entity.GameEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RankDTO {
    private Long id;
    private Long gameId;
    private Map<Integer, String> ranks = new HashMap<>();
}

// private int numRanks;
// subRanks ?