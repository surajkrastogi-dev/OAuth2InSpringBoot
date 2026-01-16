package com.example.OAuth2InSpringBoot.ExHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.OAuth2InSpringBoot.Model.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TokenRefreshException.class)
	public ResponseEntity<ErrorResponse> handleResfreshTokenException(TokenRefreshException ex){
		return new ResponseEntity<>(
				new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage()),
				HttpStatus.FORBIDDEN
		);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnauthorizedException ex){
		return new ResponseEntity<>(
				new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()),
				HttpStatus.UNAUTHORIZED
		);
	}
	
}
