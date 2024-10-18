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

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "game_id")
    private GameEntity game;

    @ElementCollection
    @CollectionTable(name = "rank_system_ranks", joinColumns = @JoinColumn(name = "rank_system_id"))
    @MapKeyColumn(name = "rank_level")
    @Column(name = "rank_name")
    private Map<Integer, String> ranks = new HashMap<>();
}
/*
simplest way to handle sub ranks, remove the integer level

    @ElementCollection
    @CollectionTable(name = "rank_system_ranks", joinColumns = @JoinColumn(name = "rank_system_id"))
    @MapKeyColumn(name = "rank_name")   // just change these
    @Column(name = "sub_ranks")         // just change these
    private Map<String, String> ranks = new HashMap<>();



Another way, creating a class without creating an entity:
This could also work in the game class instead of the RankEntity.

    public class RankEntity ....
    ...private List<Rank> ranks = new ArrayList<>();

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rank {
        @Column(name = "rank_name")
        private String name;

        @ElementCollection
        @CollectionTable(name = "rank_sub_ranks", joinColumns = @JoinColumn(name = "rank_id"))
        @Column(name = "sub_rank_name")
        @OrderColumn(name = "sub_rank_order")
        private List<String> subRanks = new ArrayList<>();
    }
*/
/*
RankSystem rs = new RankSystem();
rs.getRanks().put(1, "Bronze");
rs.getRanks().put(2, "Silver");
rs.getRanks().put(3, "Gold");
*/

