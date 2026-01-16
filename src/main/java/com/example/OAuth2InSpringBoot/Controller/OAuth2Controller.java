package com.example.OAuth2InSpringBoot.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.OAuth2InSpringBoot.Model.ApiResponse;
import com.example.OAuth2InSpringBoot.ServiceImpl.OAuthServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/oauth/google")
@RestController
public class OAuth2Controller {
	
	@Autowired private OAuthServiceImpl oAuthService;
	
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
	
}
