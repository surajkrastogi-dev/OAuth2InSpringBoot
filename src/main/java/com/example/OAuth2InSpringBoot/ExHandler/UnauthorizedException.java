package com.example.OAuth2InSpringBoot.ExHandler;

public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}
}
