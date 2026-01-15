package com.example.SpringBootWithOAuth2.Helper;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.SpringBootWithOAuth2.Entity.RefreshTokenAttempts;
import com.example.SpringBootWithOAuth2.ExHandler.TokenRefreshException;
import com.example.SpringBootWithOAuth2.Repository.RefreshTokenAttemptRepository;

@Component
public class RefreshTokenAttemptUtil {
	
	@Autowired
	private RefreshTokenAttemptRepository refreshAttemptRepo;
	@Value("${Refresh_Token_Rate_limit_Time_in_Seconds}")
	private long rateLimitTime;
	
	public void validateRefreshTokenAttemptRateLimit(String existingToken,String email,String deviceId) {
		
		
		Instant oneMinuteAgo = Instant.now().minusSeconds(rateLimitTime);
		
		long attempts = refreshAttemptRepo.countByEmailAndDeviceIdAndAttemptTimeAfter(email, deviceId,oneMinuteAgo);
		
		if(attempts>=5) {
			throw new TokenRefreshException("Too many refresh attempts from this device within 1 Minutes.Try again later!");
		}
		
		//save Refresh_token_attempts
		RefreshTokenAttempts refreshAttempt = new RefreshTokenAttempts();
		refreshAttempt.setRefreshToken(existingToken);
		refreshAttempt.setAttemptTime(Instant.now());
		refreshAttempt.setDeviceId(deviceId);
		refreshAttempt.setEmail(email);
		
		refreshAttemptRepo.save(refreshAttempt);
		
	}

}
