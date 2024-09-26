package com.example.hugbo_team_13.persistence.repository;

import com.example.hugbo_team_13.persistence.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
