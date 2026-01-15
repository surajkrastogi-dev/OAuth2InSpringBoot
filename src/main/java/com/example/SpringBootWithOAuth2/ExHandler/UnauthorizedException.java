package com.example.SpringBootWithOAuth2.ExHandler;

public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}
}
