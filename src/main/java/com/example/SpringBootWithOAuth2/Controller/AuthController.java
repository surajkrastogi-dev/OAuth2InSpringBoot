package com.example.SpringBootWithOAuth2.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBootWithOAuth2.Config.JwtService;
import com.example.SpringBootWithOAuth2.Entity.RefreshToken;
import com.example.SpringBootWithOAuth2.Model.ApiResponse;
import com.example.SpringBootWithOAuth2.Model.ApiTokenResponse;
import com.example.SpringBootWithOAuth2.Model.AuthResponse;
import com.example.SpringBootWithOAuth2.Model.LoginRequest;
import com.example.SpringBootWithOAuth2.Model.LogoutRequest;
import com.example.SpringBootWithOAuth2.Model.RefreshTokenRequest;
import com.example.SpringBootWithOAuth2.Model.RegisterRequest;
import com.example.SpringBootWithOAuth2.Model.TokenResponse;
import com.example.SpringBootWithOAuth2.Repository.RefreshTokenRepository;
import com.example.SpringBootWithOAuth2.ServiceImpl.CustomUserDetailsService;
import com.example.SpringBootWithOAuth2.ServiceImpl.RefreshTokenService;
import com.example.SpringBootWithOAuth2.ServiceImpl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private RefreshTokenService refreshService;
	@Autowired
	private RefreshTokenRepository refreshRepo;
	@Autowired
	private CustomUserDetailsService customUserService;
	@Autowired
	private JwtService jwtService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest request) {

		if (userServiceImpl.registerUserDetails(request)) {
			return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
		} else {
			if (request.getMobileNo() == null || request.getMobileNo().isBlank()) {
				return ResponseEntity.badRequest().body(new ApiResponse(false, "Mobile number is mandatory"));
			}

			if (request.getEmail() == null || request.getEmail().isBlank()) {
				return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is mandatory"));
			}

			if (request.getPassword() == null || request.getPassword().isBlank()) {
				return ResponseEntity.badRequest().body(new ApiResponse(false, "Password is mandatory"));
			}
		}
		return ResponseEntity.badRequest().body(new ApiResponse(false, "UserName Already Exist Bad Request!"));

	}


	// refresh token login
	@PostMapping("/refreshlogin")
	public ResponseEntity<ApiTokenResponse> refreshLogin(@RequestBody LoginRequest req,HttpServletRequest request) {
		TokenResponse response = userServiceImpl.loginRefresh(req,request);
		return ResponseEntity.ok
				(new ApiTokenResponse(response.getAccessToken(),response.getRefreshToken(), "Refresh Login SuccessFull!"));
	}

	// refresh API for create new RefreshToken
	@PostMapping("/refresh")
	public TokenResponse refresh(@RequestBody RefreshTokenRequest req,HttpServletRequest request) {
		return userServiceImpl.refreshTokenGeneration(req,request);
	}

	@PostMapping("/logout")
	public ApiResponse logout(@RequestBody LogoutRequest req) {
		if (userServiceImpl.logoutUser(req)) {
			return new ApiResponse(true, "Logged out Success");
		} else {
			return new ApiResponse(false, "Logged out Failed!");
		}
	}
	
	@PostMapping("/logoutAllDevice")
	public ApiResponse logoutAllDevice(@RequestBody LogoutRequest req) {
		if (userServiceImpl.logoutAllUser(req)) {
			return new ApiResponse(true, "Logged out All Devices Success");
		} else {
			return new ApiResponse(false, "Logged out Failed!");
		}
	}

	
	
}
