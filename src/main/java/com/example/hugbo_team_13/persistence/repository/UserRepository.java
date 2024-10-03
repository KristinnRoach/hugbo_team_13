package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    UserEntity findByUsername(String username); // should be DTO instead?
    boolean existsByUsername(String username);

}
