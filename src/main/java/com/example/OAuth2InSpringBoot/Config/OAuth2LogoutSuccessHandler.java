package com.example.OAuth2InSpringBoot.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.OAuth2InSpringBoot.Entity.UserDetailsEntity;
import com.example.OAuth2InSpringBoot.Repository.RefreshTokenRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler{

	@Autowired private RefreshTokenRepository refreshRepo;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		if(authentication!=null && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
//		if(authentication!=null) {
//			OAuth2User oAuth2User =  (OAuth2User) authentication.getPrincipal();
			String email = oAuth2User.getAttribute("email");
			
			refreshRepo.revokeAllByEmail(email);
		}
		
		response.setContentType("application/json");
		response.getWriter().write(
			"{ \"message\": \"OAuth Logout SuccessFul\" }"
		);
		
		
		
	}

}
