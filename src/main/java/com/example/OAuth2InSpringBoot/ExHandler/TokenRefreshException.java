package com.example.OAuth2InSpringBoot.ExHandler;

public class TokenRefreshException  extends RuntimeException{
	
	public TokenRefreshException(String message) {
		super(message);
	}
}
