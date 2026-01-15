package com.example.SpringBootWithOAuth2.Repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringBootWithOAuth2.Entity.RefreshTokenAttempts;

@Repository
public interface RefreshTokenAttemptRepository extends JpaRepository<RefreshTokenAttempts, Long> {

	long countByRefreshTokenAndAttemptTimeAfter(String refreshToken,Instant after);
	long countByEmailAndDeviceIdAndAttemptTimeAfter(String email,String deviceId,Instant after);
}
