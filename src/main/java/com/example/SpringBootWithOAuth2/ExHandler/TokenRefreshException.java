package com.example.SpringBootWithOAuth2.ExHandler;

public class TokenRefreshException  extends RuntimeException{
	
	public TokenRefreshException(String message) {
		super(message);
	}
}
