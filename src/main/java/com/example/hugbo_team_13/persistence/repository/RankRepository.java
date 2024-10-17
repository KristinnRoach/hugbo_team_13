package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<RankEntity, Long> {

}
