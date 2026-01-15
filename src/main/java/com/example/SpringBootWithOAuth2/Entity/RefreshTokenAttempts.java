package com.example.SpringBootWithOAuth2.Entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="refresh_token_attempts",schema="defaultdb")
public class RefreshTokenAttempts {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	private Long id;
	
	@Column(name="refresh_token")
	private String refreshToken;
	
	@Column(name="attempt_time")
	private Instant attemptTime;
	
	@Column(name="email")
	private String email;
	
	@Column(name="device_id")
	private String deviceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Instant getAttemptTime() {
		return attemptTime;
	}

	public void setAttemptTime(Instant attemptTime) {
		this.attemptTime = attemptTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
