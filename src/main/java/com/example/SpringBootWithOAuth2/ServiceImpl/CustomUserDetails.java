package com.example.SpringBootWithOAuth2.ServiceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.SpringBootWithOAuth2.Entity.UserDetailsEntity;

public class CustomUserDetails implements UserDetails{

	   private final UserDetailsEntity user;
	    private final List<String> roles;

	    public CustomUserDetails(UserDetailsEntity user, List<String> roles) {
	        this.user = user;
	        this.roles = roles;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return roles.stream()
	                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
	                .toList();
	    }

	    @Override public String getPassword() { return user.getPassword(); }
	    @Override public String getUsername() { return user.getEmail(); }
	    @Override public boolean isEnabled() { return Boolean.TRUE.equals(user.getActiveFlag()); }
	    @Override public boolean isAccountNonExpired() { return true; }
	    @Override public boolean isAccountNonLocked() { return true; }
	    @Override public boolean isCredentialsNonExpired() { return true; }
}
