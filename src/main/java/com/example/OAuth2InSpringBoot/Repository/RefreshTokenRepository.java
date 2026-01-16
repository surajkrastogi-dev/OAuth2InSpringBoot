package com.example.OAuth2InSpringBoot.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.OAuth2InSpringBoot.Entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
	List<RefreshToken> findAllByEmailAndRevokedFalse(String email);
	Optional<RefreshToken> findByToken(String token);
	void deleteByEmail(String emial);
	
	@Modifying
	@Query("UPDATE RefreshToken r set r.revoked=true WHERE r.email= :email")
	void revokeAllByEmail(@Param("email") String email);
	
}
