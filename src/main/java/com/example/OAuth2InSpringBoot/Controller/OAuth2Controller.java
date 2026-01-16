package com.example.OAuth2InSpringBoot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.OAuth2InSpringBoot.ServiceImpl.OAuthServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/auth/google")
@RestController
public class OAuth2Controller {
	
	@Autowired private OAuthServiceImpl oAuthService;
	
	@GetMapping("/getOauthUserAuthorization")
	public ResponseEntity<?> getOAuthAuthorization(@RequestParam String code,HttpServletRequest request){
			return oAuthService.getOAuthUserAuthorizationFromGoogleAPI(code,request);
	}
	

}
