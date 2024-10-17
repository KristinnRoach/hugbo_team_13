package com.example.hugbo_team_13.persistence.entity;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "rank_system")
@Getter
@Setter
@NoArgsConstructor
public class RankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @ElementCollection
    @CollectionTable(name = "rank_system_ranks", joinColumns = @JoinColumn(name = "rank_system_id"))
    @MapKeyColumn(name = "rank_level")
    @Column(name = "rank_name")
    private Map<Integer, String> ranks = new HashMap<>();
}

/*
RankSystem rs = new RankSystem();
rs.getRanks().put(1, "Bronze");
rs.getRanks().put(2, "Silver");
rs.getRanks().put(3, "Gold");

 */