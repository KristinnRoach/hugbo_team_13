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
