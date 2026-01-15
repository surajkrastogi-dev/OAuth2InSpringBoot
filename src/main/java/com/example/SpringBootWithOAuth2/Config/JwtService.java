package com.example.SpringBootWithOAuth2.Config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${JWT_SECRET}")
    private String SECRET_KEY ;

    @Value("${JWT_EXPIRY_MILLISEC}")
    private long EXPIRATION_TIME ;
    
    @Value("${JWT_A_EXPIRY_MILLISEC}")
    private long accessExpiry;


    @Value("${JWT_R_EXPIRY_MILLISEC}")
    private long refreshExpiry;
    
    public String generateToken(UserDetails userDetails) {
    	
    	// store roles as a list of strings
    	List<String> roles = userDetails.getAuthorities().stream()
    			.map(GrantedAuthority::getAuthority)
    			.collect(Collectors.toList());
    	
        return Jwts.builder()
        		.setSubject(userDetails.getUsername())
        		.claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String generateAccessToken(UserDetails user) {
    	return Jwts.builder()
    			.setSubject(user.getUsername())
    			.claim("roles", user.getAuthorities())
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + accessExpiry ))
    			.signWith(getKey(),SignatureAlgorithm.HS256)
    			.compact();
    }
    
    //test access token with multiples claims values in JWT JSON Payload
    public String generateAccessTokenWithDynamicJWTJSONPayload(UserDetails user) {
    	Map<String, Object> claims = new HashMap<>();
    	claims.put("roles", user.getAuthorities());
    	claims.put("userId", "TEST_USER_ID");
    	claims.put("device", "WEB|MOBILE");
    	claims.put("ip", "192.168.1.10");
    	
    	return Jwts.builder()
    			.setSubject(user.getUsername())
    			.addClaims(claims)
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + accessExpiry ))
    			.signWith(getKey(),SignatureAlgorithm.HS256)
    			.compact();
    }
    
    public String generateRefreshToken(String username) {
    	return Jwts.builder()
    			.setSubject(username)
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis()+refreshExpiry))
    			.signWith(getKey(),SignatureAlgorithm.HS256)
    			.compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !getClaims(token).getExpiration().before(new Date());
    }

    public boolean validate(String token) {
    	try {
    		getClaims(token);
    		return true;
    	} catch (JwtException | IllegalArgumentException e) {
    		return false;
    	}
    }
    
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
    
    public List<String> getRolesFromToken(String token) {
    	Claims claims = getClaims(token);
    	Object rolesObj = claims.get("roles");
    	if (rolesObj instanceof List<?>) {
    	return ((List<?>) rolesObj).stream()
    	.map(String::valueOf)
    	.collect(Collectors.toList());
    	}
    	return Collections.emptyList();
    }
    
}
