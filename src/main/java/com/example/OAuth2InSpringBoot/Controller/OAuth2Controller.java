package com.example.OAuth2InSpringBoot.Controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.OAuth2InSpringBoot.Config.JwtService;
import com.example.OAuth2InSpringBoot.Entity.RefreshToken;
import com.example.OAuth2InSpringBoot.Model.ApiResponse;
import com.example.OAuth2InSpringBoot.ServiceImpl.CustomUserDetailsService;
import com.example.OAuth2InSpringBoot.ServiceImpl.OAuthServiceImpl;
import com.example.OAuth2InSpringBoot.ServiceImpl.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RequestMapping("/oauth/google")
@RestController
public class OAuth2Controller {
	
	@Autowired private OAuthServiceImpl oAuthService;
	@Autowired private RefreshTokenService refreshTokenService;
	@Autowired private CustomUserDetailsService customUserDetailsService; 
	@Autowired private JwtService jwtService;
	
	@GetMapping("/getOauthUserAuthorization")
	public ResponseEntity<?> getOAuthAuthorization(@RequestParam String code,HttpServletRequest request){
			return oAuthService.getOAuthUserAuthorizationFromGoogleAPI(code,request);
	}
	

	/**
     * REST endpoint to initiate OAuth2 login
     * Frontend simply redirects user to this URL
     */
    @GetMapping("/login")
    public void oauth2Login(HttpServletResponse response) throws IOException {

        // Spring Security default OAuth2 authorization endpoint
        response.sendRedirect("/oauth2/authorization/google");
    }
    
    /**
     * REST logout endpoint
     * This triggers Spring Security logout
     * 
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws Exception {

        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);

        return ResponseEntity.ok(
                new ApiResponse(true, "OAuth2 Logout Successful")
        );
    }
    
    
    //refresh API 
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
    	
    	// ✅ cookie se refresh token read karo
    	String refreshToken = null;
    	if(request.getCookies()!=null) {
    		for(Cookie cookie : request.getCookies()) {
    			if("refreshToken".equals(cookie.getName())) {
    				refreshToken = cookie.getValue();
    				break;
    			}
    		}
    	}//if condition end
    		
    		if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Refresh token missing"));
            }
    		 // ✅ DB me refreshToken validate karo
            RefreshToken tokenObj = refreshTokenService.verifyRefreshToken(refreshToken);

            // ✅ user load karo
            UserDetails user = customUserDetailsService.loadUserByUsername(tokenObj.getEmail());
            
            // ✅ new access token generate
            String newAccessToken = jwtService.generateAccessToken(user);

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "message", "New access token generated"
            ));
    	
    }
    
    
    
    
    
    
	
}
