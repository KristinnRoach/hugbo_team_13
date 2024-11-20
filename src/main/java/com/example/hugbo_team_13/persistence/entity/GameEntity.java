package com.example.hugbo_team_13.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity class representing a game in the application.
 * Mapped to the "game" table in the database.
 */
@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
public class GameEntity {

    /**
     * Unique identifier for the game.
     * Automatically generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="game_id", nullable = false)
    private Long id;

    /**
     * The name of the game.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The platform on which the game is available.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String platform;

    /**
     * An image associated with the game.
     * Stored as a byte array.
     */
   // @Lob
    @Column(name = "img")
    @Basic(fetch = FetchType.LAZY)
    private byte[] img;

    /**
     *  The image type (e.g., "image/jpeg")
     */
    private String imgType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity admin;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events = new ArrayList<>();


    /**
     * The associated ranking system for the game.
     * This is a one-to-one relationship with {@link RankEntity}.
     * The relationship is mapped by the "game" field in the {@link RankEntity} class.
     */
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private RankEntity rankEntity;

    @ElementCollection
    @CollectionTable(name = "game_ranks", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "rank_level") // Column for the map's key
    @Column(name = "rank_name") // Column for the map's value
    private Map<Integer, String> ranks = new HashMap<>();

}
/*
    public Map<Integer, String> getRanks() {
        return ranks;
    }

    public void setRanks(Map<Integer, String> ranks) {
        this.ranks = ranks;
    }
}
*/
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
