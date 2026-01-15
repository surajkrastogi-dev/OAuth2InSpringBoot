package com.example.SpringBootWithOAuth2.ServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SpringBootWithOAuth2.Config.JwtService;
import com.example.SpringBootWithOAuth2.Entity.LoginHistory;
import com.example.SpringBootWithOAuth2.Entity.MasterRoleEntity;
import com.example.SpringBootWithOAuth2.Entity.RefreshToken;
import com.example.SpringBootWithOAuth2.Entity.UserDetailsEntity;
import com.example.SpringBootWithOAuth2.Entity.UserVsRolesEntity;
import com.example.SpringBootWithOAuth2.Entity.UserVsRolesId;
import com.example.SpringBootWithOAuth2.ExHandler.TokenRefreshException;
import com.example.SpringBootWithOAuth2.Helper.RefreshTokenAttemptUtil;
import com.example.SpringBootWithOAuth2.Helper.RequestUtil;
import com.example.SpringBootWithOAuth2.Model.LoginRequest;
import com.example.SpringBootWithOAuth2.Model.LogoutRequest;
import com.example.SpringBootWithOAuth2.Model.RefreshTokenRequest;
import com.example.SpringBootWithOAuth2.Model.RegisterRequest;
import com.example.SpringBootWithOAuth2.Model.TokenResponse;
import com.example.SpringBootWithOAuth2.Repository.LoginHistoryRepository;
import com.example.SpringBootWithOAuth2.Repository.MasterRoleRepository;
import com.example.SpringBootWithOAuth2.Repository.RefreshTokenRepository;
import com.example.SpringBootWithOAuth2.Repository.UserDetailsRepository;
import com.example.SpringBootWithOAuth2.Repository.UserVsRolesRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.hibernate.type.descriptor.jdbc.InstantAsTimestampJdbcType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl {

	@Autowired 
	private UserDetailsRepository userRepo;
	@Autowired
	private MasterRoleRepository roleRepo;
	@Autowired
	private UserVsRolesRepository userRoleRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomUserDetailsService customUserDetails;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private RefreshTokenRepository refreshRepo;
	@Autowired
	private LoginHistoryRepository loginHistoryRepo;
	@Autowired 
	private RequestUtil requestUtils;
	@Autowired 
	private RefreshTokenAttemptUtil refreshTokenAttemptUtil;
	

	public boolean registerUserDetails(RegisterRequest request) {
		try {
			System.out.println("MobileNo = " + request.getMobileNo());
			if (request.getMobileNo() == null || request.getMobileNo().isBlank()) {
				return false;
			}
			if (request.getEmail() == null || request.getEmail().isBlank()) {
				return false;
			}
			if (request.getPassword() == null || request.getPassword().isBlank()) {
				return false;
			}
			if (userRepo.existsByEmail(request.getEmail())) {
				return false;
			}

			UserDetailsEntity user = new UserDetailsEntity();
			user.setUserName(request.getUserName());
			user.setMobileNo(request.getMobileNo()); // 🔴 THIS WAS MISSING
			user.setEmail(request.getEmail());
			user.setPassword(encoder.encode(request.getPassword()));
			user.setPincode(request.getPincode());
			user.setStateCode(request.getStateCode());
			user.setCountry(request.getCountry());
			user.setPanNo(request.getPanNo());
			user.setAadharNo(request.getAadharNo());
			user.setDateOfBirth(request.getDateOfBirth());
			user.setAddress(request.getAddress());
			user.setActiveFlag(true);
			user.setCreatedOn(LocalDateTime.now());
			user.setUpdatedOn(LocalDateTime.now());
			user.setGoogleOauthLogin(false);

			userRepo.save(user);
			MasterRoleEntity role = null;
			if(request.getRoleType()!=null && !request.getRoleType().isBlank()) {
				if(request.getRoleType().equals("ADMIN")) {
					role = roleRepo.findByRoleName("ADMIN")
							.orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
				}else {
					role = roleRepo.findByRoleName("USER")
							.orElseThrow(() -> new RuntimeException("Role USER not found"));
				}
			}else {
				throw new RuntimeException("User Role Type is not be Null,must have some Role Type : USER|ADMIN");
			}

			UserVsRolesId userRoleId = new UserVsRolesId(user.getUserId(), role.getRoleId());

			UserVsRolesEntity userRole = new UserVsRolesEntity();
			userRole.setId(userRoleId);
			userRole.setRole(role);
			userRole.setActiveFlag(true);

			userRoleRepo.save(userRole);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// login and get AccessToken and RefreshToken
	public TokenResponse loginRefresh(LoginRequest req,HttpServletRequest request) {
		try {

			LoginHistory loginHistory = new LoginHistory();
			loginHistory.setEmail(req.getEmail());
			loginHistory.setDeviceId(req.getDeviceId());
			loginHistory.setLoginTime(Instant.now());
			loginHistory.setIpAddress(requestUtils.generateIpAddress(request));
			loginHistory.setUserAgent(requestUtils.generateUserAgent(request));

			try {
				Authentication auth = authManager
						.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
			
			// save LoginHistory with success True
			loginHistory.setSuccess(true);
			loginHistoryRepo.save(loginHistory);			
			
			UserDetails user = customUserDetails.loadUserByUsername(req.getEmail());
			String accessToken = jwtService.generateAccessToken(user);

			/** New Access Token with dynamic JWT JSON Payload **/
//			String accessToken = jwtService.generateAccessTokenWithDynamicJWTJSONPayload(user);
			RefreshToken refreshToken = refreshTokenService.createRToken(req.getEmail(),req.getDeviceId(),request);
			return new TokenResponse(accessToken,refreshToken.getToken());
			
			}catch(Exception ex) {
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

	
	public TokenResponse refreshTokenGeneration(RefreshTokenRequest req,HttpServletRequest request) {
		
		//  Resolve ACTIVE refresh token (revoked = false)
	    RefreshToken existingToken = refreshTokenService.getActiveRefreshToken(req.getRefreshToken());
		
		//logic for validate RefreshToken Attempts Limits
		refreshTokenAttemptUtil.validateRefreshTokenAttemptRateLimit(existingToken.getToken(),existingToken.getEmail(),existingToken.getDeviceId());
		
		//Verify fingerprint + rotate refresh token
		RefreshToken rt = refreshTokenService.verifyAndRotateToken(existingToken,request);
		
		UserDetails user = customUserDetails.loadUserByUsername(existingToken.getEmail());

		//Generate new access token
		String newAccessToken = jwtService.generateAccessToken(user);
		return new TokenResponse(newAccessToken, rt.getToken());
	}
	
	//logout user
	@Transactional
	public boolean logoutUser(LogoutRequest req) {
		refreshRepo.findByTokenAndRevokedFalse(req.getToken())
			.ifPresent(token-> {
				token.setRevoked(true);
				refreshRepo.save(token);
			});
//		refreshRepo.deleteByEmail(req.getUsername());
		return true;
	}
	
	//Logout from all devices
	@Transactional
	public boolean logoutAllUser(LogoutRequest req) {
		List<RefreshToken> tokenList = refreshRepo.findAllByEmailAndRevokedFalse(req.getUsername());
		tokenList.forEach(token-> token.setRevoked(true));
		refreshRepo.saveAll(tokenList);
//		refreshRepo.deleteByEmail(req.getUsername());
		return true;
	}
	
	
/** Others Logic **/	
	public static void copyNonNullProperties(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

}
