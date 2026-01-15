package com.example.SpringBootWithOAuth2.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiTokenResponse {

	private String accessToken;
	private String refreshToken;
	private String message;
	
}
