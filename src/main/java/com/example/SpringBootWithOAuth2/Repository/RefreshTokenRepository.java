package com.example.SpringBootWithOAuth2.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringBootWithOAuth2.Entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
	List<RefreshToken> findAllByEmailAndRevokedFalse(String email);
	Optional<RefreshToken> findByToken(String token);
	void deleteByEmail(String emial);
	
}
