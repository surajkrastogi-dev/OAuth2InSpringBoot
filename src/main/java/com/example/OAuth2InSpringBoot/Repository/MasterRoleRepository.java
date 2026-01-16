package com.example.OAuth2InSpringBoot.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OAuth2InSpringBoot.Entity.MasterRoleEntity;

@Repository
public interface MasterRoleRepository extends JpaRepository<MasterRoleEntity, Integer> {
    Optional<MasterRoleEntity> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}

