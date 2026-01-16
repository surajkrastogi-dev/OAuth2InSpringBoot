package com.example.OAuth2InSpringBoot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OAuth2InSpringBoot.Entity.UserDetailsEntity;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer> {
    Optional<UserDetailsEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}

