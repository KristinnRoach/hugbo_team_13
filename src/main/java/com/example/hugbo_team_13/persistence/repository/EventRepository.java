package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    EventEntity findByName(String name);

    boolean existsByName(String name);
}
