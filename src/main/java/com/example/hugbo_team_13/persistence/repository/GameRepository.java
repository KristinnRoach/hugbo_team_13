package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    
    GameEntity findByName(String name); // should be Optional<GameEntity> ?
    boolean existsByName(String name);
}
