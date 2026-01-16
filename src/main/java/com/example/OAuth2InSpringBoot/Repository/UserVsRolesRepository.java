package com.example.OAuth2InSpringBoot.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.OAuth2InSpringBoot.Entity.UserVsRolesEntity;
import com.example.OAuth2InSpringBoot.Entity.UserVsRolesId;

@Repository
public interface UserVsRolesRepository extends JpaRepository<UserVsRolesEntity, UserVsRolesId> {
    @Query("""
            SELECT r.roleName
            FROM UserVsRolesEntity ur
            JOIN ur.role r
            WHERE ur.id.userId = :userId
              AND ur.activeFlag = true
        """)
        List<String> findRolesByUserId(@Param("userId") Integer userId);
	
	
    /* ===============================
       Check role already assigned
       =============================== */
    boolean existsByIdUserIdAndIdRoleId(Integer userId, Integer roleId);

    /* ===============================
       Delete all roles of a user
       =============================== */
    void deleteByIdUserId(Integer userId);
}

