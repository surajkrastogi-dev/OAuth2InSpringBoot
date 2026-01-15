package com.example.SpringBootWithOAuth2.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringBootWithOAuth2.Entity.MasterRoleEntity;

@Repository
public interface MasterRoleRepository extends JpaRepository<MasterRoleEntity, Integer> {
    Optional<MasterRoleEntity> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}

