package com.example.SpringBootWithOAuth2.ServiceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SpringBootWithOAuth2.Entity.RefreshToken;
import com.example.SpringBootWithOAuth2.ExHandler.TokenRefreshException;
import com.example.SpringBootWithOAuth2.Helper.FingerPrintUtils;
import com.example.SpringBootWithOAuth2.Helper.RequestUtil;
import com.example.SpringBootWithOAuth2.Repository.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RefreshTokenService {

	@Value("${JWT_R_EXPIRY_MILLISEC}")
	private long refreshExpiry;

	@Autowired
	RefreshTokenRepository repo;
	@Autowired
	private FingerPrintUtils fingerPrintUtils;
	@Autowired
	private RequestUtil requestUtils;

	public RefreshToken getActiveRefreshToken(String refreshToken) {

		return repo.findByTokenAndRevokedFalse(refreshToken)
				.orElseThrow(() -> new TokenRefreshException("Invalid or revoked Refresh Token"));
	}

	public boolean expiredRToken(RefreshToken rt) {
		return rt.getExpiryDate().isBefore(Instant.now());
	}

	public RefreshToken createRToken(String username, String deviceId, HttpServletRequest request) {
		RefreshToken rt = new RefreshToken();
		rt.setEmail(username);
		rt.setToken(UUID.randomUUID().toString());
		rt.setExpiryDate(Instant.now().plusMillis(refreshExpiry));
		rt.setDeviceId(deviceId);
		rt.setRevoked(false);

		// get fingerPrint
		String fingerPrint = fingerPrintUtils.generateFingerPrint(deviceId, requestUtils.generateIpAddress(request),
				requestUtils.generateUserAgent(request));

		rt.setFingerPrint(fingerPrint);
		return repo.save(rt);
	}

	public RefreshToken verifyAndRotateToken(RefreshToken existingToken, HttpServletRequest request) {

		/** Validate FingerPrint Logic generate new FingerPrint **/
		String fingerPrint = fingerPrintUtils.generateFingerPrint(existingToken.getDeviceId(),
				requestUtils.generateIpAddress(request), requestUtils.generateUserAgent(request));

		// get Existing FingerPrint and compare both
		if (!existingToken.getFingerPrint().equals(fingerPrint)) {
			existingToken.setRevoked(true);
			repo.save(existingToken);
			throw new TokenRefreshException("Device mismatch detected and FingerPrint not matched!");
		}

		// expiry check
		if (existingToken.getExpiryDate().isBefore(Instant.now())) {
			existingToken.setRevoked(true);
			repo.save(existingToken);
			throw new TokenRefreshException("Refresh Token Expired,please login again!");
		}

		// ROTATION → blacklist old token
		existingToken.setRevoked(true);
		repo.save(existingToken);

		// 🔁 Create new refresh token (same device)
		return createRToken(existingToken.getEmail(), existingToken.getDeviceId(), request);

	}

}
