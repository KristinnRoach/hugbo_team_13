package com.example.hugbo_team_13.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String platform;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private RankEntity rankEntity;

    public void setRankEntity(RankEntity rankEntity) {
        this.rankEntity = rankEntity;
        if (rankEntity != null) {
            rankEntity.setGame(this);
        }
    }
}

/* IF we want to get rid of the RankEntity:

....@Column(nullable = false)
    private String platform;

    @ElementCollection
    @CollectionTable(name = "game_ranks", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "main_rank")
    @Column(name = "sub_ranks")
    private Map<String, String> ranks = new HashMap<>();

    public void addRank(String mainRank, List<String> subRanks) {
        ranks.put(mainRank, String.join(",", subRanks));
    }

    public List<String> getSubRanks(String mainRank) {
        String subRanksString = ranks.get(mainRank);
        return subRanksString != null ? Arrays.asList(subRanksString.split(",")) : new ArrayList<>();
    }
}

// Subranks (if no RankEntity) :

GameEntity game = new GameEntity();
game.setName("CSGO");
game.setPlatform("PC");

game.addRank("Bronze", Arrays.asList("Bronze 1", "Bronze 2", "Bronze 3"));
game.addRank("Silver", Arrays.asList("Silver 1", "Silver 2", "Silver 3"));

// Later, to retrieve sub-ranks
List<String> bronzeSubRanks = game.getSubRanks("Bronze");
 */
