package com.example.OAuth2InSpringBoot.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenResponse {

	private String accessToken;
	private String refreshToken;
}
