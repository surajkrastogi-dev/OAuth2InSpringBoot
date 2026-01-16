package com.example.OAuth2InSpringBoot.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.OAuth2InSpringBoot.Entity.UserDetailsEntity;
import com.example.OAuth2InSpringBoot.Model.TokenResponse;
import com.example.OAuth2InSpringBoot.ServiceImpl.OAuthServiceImpl;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired private OAuthServiceImpl oAuthServiceImpl;
	@Autowired private JwtService jwtService;
	
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,Authentication authentication)throws IOException {
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		
		// 1️. Create or update local user
		UserDetailsEntity user = oAuthServiceImpl.processOAuthPostLogin(email, name);
		
		// 2️. Generate YOUR JWT Access Token // loginUser with OAuth Google
		TokenResponse tokenResponse = oAuthServiceImpl.oauthGoogleLogin(user, request);
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                """
                {
                  "accessToken": "%s",
                  "refreshToken": "%s",
                  "message": "OAuth Login Successful"
                }
                """.formatted(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken())
        );
		
	}

}
