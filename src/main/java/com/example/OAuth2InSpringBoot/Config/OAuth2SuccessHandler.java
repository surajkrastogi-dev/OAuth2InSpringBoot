package com.example.OAuth2InSpringBoot.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.OAuth2InSpringBoot.Entity.UserDetailsEntity;
import com.example.OAuth2InSpringBoot.Model.TokenResponse;
import com.example.OAuth2InSpringBoot.ServiceImpl.OAuthServiceImpl;

import java.io.IOException;
import java.time.Duration;

import jakarta.servlet.http.Cookie;
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
		
		
		//send refreshToken in Http Secure Cookies
		Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
		cookie.setHttpOnly(true); //JS access nahi kar sakta
		cookie.setSecure(true);//HTTPS required
		cookie.setPath("/");//har endpoint pe accessible
		cookie.setMaxAge(7*24*60*60); //7days
		
		response.addCookie(cookie);
		/**Java Cookie me SameSite directly set nahi hota properly. use ResponseCookie **/
		
		//Another way of adding cookie in API Response
		/**
		ResponseCookie responseCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(Duration.ofDays(7))
				.sameSite("Lax") //"Strict"-> max secure and "Lax"-> OAuth redirects me thoda easier
				.build();
		
		response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
		**/
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                """
                {
                  "accessToken": "%s",
                  "message": "OAuth Login Successful"
                }
                """.formatted(tokenResponse.getAccessToken())
        );
		
	}

}
