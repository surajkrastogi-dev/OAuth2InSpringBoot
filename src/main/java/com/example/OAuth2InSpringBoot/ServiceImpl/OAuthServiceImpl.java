package com.example.OAuth2InSpringBoot.ServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.OAuth2InSpringBoot.Config.JwtService;
import com.example.OAuth2InSpringBoot.Entity.LoginHistory;
import com.example.OAuth2InSpringBoot.Entity.MasterRoleEntity;
import com.example.OAuth2InSpringBoot.Entity.RefreshToken;
import com.example.OAuth2InSpringBoot.Entity.UserDetailsEntity;
import com.example.OAuth2InSpringBoot.Entity.UserVsRolesEntity;
import com.example.OAuth2InSpringBoot.Entity.UserVsRolesId;
import com.example.OAuth2InSpringBoot.Helper.RequestUtil;
import com.example.OAuth2InSpringBoot.Model.TokenResponse;
import com.example.OAuth2InSpringBoot.Repository.LoginHistoryRepository;
import com.example.OAuth2InSpringBoot.Repository.MasterRoleRepository;
import com.example.OAuth2InSpringBoot.Repository.UserDetailsRepository;
import com.example.OAuth2InSpringBoot.Repository.UserVsRolesRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OAuthServiceImpl {

	@Value("${OAuth_Client_ID}")
	private String clientId;

	@Value("${OAuth_Client_SECRET}")
	private String clientSecret;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private UserDetailsRepository userRepo;
	@Autowired
	private MasterRoleRepository masterRole;
	@Autowired
	private UserVsRolesRepository userVsRoleRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private RequestUtil requestUtils;
	@Autowired
	private LoginHistoryRepository loginHistoryRepo;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private CustomUserDetailsService customUserDetails;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private RefreshTokenService refreshTokenService;

	public ResponseEntity<?> getOAuthUserAuthorizationFromGoogleAPI(String code, HttpServletRequest request) {
		try {
			String tokenRequestUri = "https://oauth2.googleapis.com/token";
			String redirectUri = "https://developers.google.com/oauthplayground";
			String tokenInfoRequestUri = "https://oauth2.googleapis.com/tokeninfo?id_token=";
			String otherUserInfoGoogleApi = "https://openidconnect.googleapis.com/v1/userinfo";
			String userEmail = null;
			String userName = null;

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("code", code);
			params.add("client_id", clientId);
			params.add("client_secret", clientSecret);
			params.add("redirect_uri", redirectUri);
			params.add("grant_type", "authorization_code");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

			/** 1.Exchange authorization code for tokens by calling google /token API **/
			ResponseEntity<Map> tokenApiResponse = restTemplate.postForEntity(tokenRequestUri, entity, Map.class);

			// get Token_id from the tokenApiResponse
//			System.out.println("Token API Response::" + tokenApiResponse.getBody().toString());
			String googleTokenId = tokenApiResponse.getBody().get("id_token").toString();
			String finalTokenInfoRequestUri = tokenInfoRequestUri + googleTokenId;
			String accessTokenGoogle = tokenApiResponse.getBody().get("access_token").toString();

			/**
			 * 2.userInfor from the Google Server with the TokenId by using the
			 * /tokeninfo?id_token=?1
			 **/
			ResponseEntity<Map> tokenInfoApiResponse = restTemplate.getForEntity(finalTokenInfoRequestUri, Map.class);
			if (tokenInfoApiResponse.getStatusCode() == HttpStatus.OK) {
				Map<String, Object> userInfo = tokenInfoApiResponse.getBody();
				userEmail = userInfo.get("email").toString();
				System.out.println("User Email :" + userEmail + " >> Access Token::" + accessTokenGoogle);

				// Retrieve others details of the User using the Google API
				HttpHeaders newHeaders = new HttpHeaders();
				newHeaders.add("Authorization", "Bearer " + accessTokenGoogle);

				Map<String, String> newRequestParam = new HashMap<>();
				newRequestParam.put("scope", "openid profile email");

				HttpEntity<Map> newEntity = new HttpEntity<Map>(newRequestParam, newHeaders);

//				ResponseEntity<Map> otherUserInfoGoogleApiResponse = restTemplate.getForEntity(otherUserInfoGoogleApi, Map.class);
				ResponseEntity<Map> otherUserInfoGoogleApiResponse = restTemplate.exchange(otherUserInfoGoogleApi,
						HttpMethod.GET, newEntity, Map.class);

				if (otherUserInfoGoogleApiResponse.getStatusCode() == HttpStatus.OK) {
					Map<String, Object> userAllInfo = otherUserInfoGoogleApiResponse.getBody();
					userName = userAllInfo.get("name").toString();
				}

				// create or register UserDetails into the DB
				UserDetailsEntity user = createorRegisterUserDetailsWithOAuth(userName, userEmail);

				// loginUser with OAuth Google
				TokenResponse tokenResponse = oauthGoogleLogin(user, request);
				
				Map<String,String> response = new HashMap<>();
				response.put("userName",user.getEmail());
				response.put("accessToken",tokenResponse.getAccessToken());
				response.put("refreshToken",tokenResponse.getRefreshToken());
				
				System.out.println("OAuth Authorization UserName is:" + user.getUserName() + " and UserEmail :" + user.getEmail());
				
				return ResponseEntity.status(HttpStatus.OK).body(response.toString());
			} else {
				return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}

	}

	public TokenResponse oauthGoogleLogin(UserDetailsEntity user, HttpServletRequest request) {
		try {
			LoginHistory loginHistory = new LoginHistory();
			loginHistory.setEmail(user.getEmail());
//			loginHistory.setDeviceId("WINDOW");
			loginHistory.setDeviceId(request.getHeader("User-Agent"));
			loginHistory.setLoginTime(Instant.now());
			loginHistory.setIpAddress(requestUtils.generateIpAddress(request));
			loginHistory.setUserAgent(requestUtils.generateUserAgent(request));

			try {
				Authentication auth = authManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), "password"));

				// save LoginHistory with success True
				loginHistory.setSuccess(true);
				loginHistoryRepo.save(loginHistory);

				UserDetails userDetails = customUserDetails.loadUserByUsername(user.getEmail());
				String accessToken = jwtService.generateAccessToken(userDetails);

				/** New Access Token with dynamic JWT JSON Payload **/
				RefreshToken refreshToken = refreshTokenService.createRToken(user.getEmail(),
											loginHistory.getDeviceId(), request);
				return new TokenResponse(accessToken, refreshToken.getToken());

			} catch (Exception ex) {
				// save LoginHistory with success False
				loginHistory.setSuccess(false);
				loginHistoryRepo.save(loginHistory);
				throw ex;
			}
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Invalid email or password");

		} catch (DisabledException e) {
			throw new RuntimeException("User is disabled");

		} catch (Exception e) {
			throw new RuntimeException("Authentication failed", e);
		}

	}

	// register UserDetails
	public UserDetailsEntity createorRegisterUserDetailsWithOAuth(String userName, String userEmail) {
		try {
			if (userEmail != null) {
				// check for UserEmail Already exist
				if (userRepo.existsByEmail(userEmail)) {
					throw new RuntimeException("UserName already Exists " + userEmail);
				}
				UserDetailsEntity user = new UserDetailsEntity();
				user.setEmail(userEmail);
				if (userName != null) {
					user.setUserName(userName);
				} else {
					user.setUserName("GUEST_USER_GOOGLE");
				}
				user.setMobileNo("9555577779");
				user.setPassword(encoder.encode("password"));
				user.setActiveFlag(true);
				user.setCreatedOn(LocalDateTime.now());
				user.setUpdatedOn(LocalDateTime.now());
				user.setGoogleOauthLogin(true);
				userRepo.save(user);

				MasterRoleEntity roles = masterRole.findByRoleName("USER")
						.orElseThrow(() -> new RuntimeException("Role USER not found"));

				UserVsRolesId userRoleId = new UserVsRolesId(user.getUserId(), roles.getRoleId());

				UserVsRolesEntity userVsRole = new UserVsRolesEntity();
				userVsRole.setId(userRoleId);
				userVsRole.setRole(roles);
				userVsRole.setActiveFlag(true);
				userVsRoleRepo.save(userVsRole);

				return user;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	
	
	/** OAuth2 Logic for Implementation **/
	public UserDetailsEntity processOAuthPostLogin(String email, String name) {

        return userRepo.findByEmail(email)
                .orElseGet(() -> {
                	
                	UserDetailsEntity user = new UserDetailsEntity();
    				user.setEmail(email);
    				if (name != null) {
    					user.setUserName(name);
    				} else {
    					user.setUserName("GUEST_USER_GOOGLE");
    				}
    				user.setMobileNo("9555577779");
    				user.setPassword(encoder.encode("password"));
    				user.setActiveFlag(true);
    				user.setCreatedOn(LocalDateTime.now());
    				user.setUpdatedOn(LocalDateTime.now());
    				user.setGoogleOauthLogin(true);
    				 user.setAuthProvider("GOOGLE");
    				userRepo.save(user);

    				MasterRoleEntity roles = masterRole.findByRoleName("USER")
    						.orElseThrow(() -> new RuntimeException("Role USER not found"));

    				UserVsRolesId userRoleId = new UserVsRolesId(user.getUserId(), roles.getRoleId());

    				UserVsRolesEntity userVsRole = new UserVsRolesEntity();
    				userVsRole.setId(userRoleId);
    				userVsRole.setRole(roles);
    				userVsRole.setActiveFlag(true);
    				userVsRoleRepo.save(userVsRole);
    				return user;
                });
    }
	
	
	
	
}
