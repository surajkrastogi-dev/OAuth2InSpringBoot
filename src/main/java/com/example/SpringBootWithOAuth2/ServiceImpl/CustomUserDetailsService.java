package com.example.SpringBootWithOAuth2.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SpringBootWithOAuth2.Entity.UserDetailsEntity;
import com.example.SpringBootWithOAuth2.Repository.UserDetailsRepository;
import com.example.SpringBootWithOAuth2.Repository.UserVsRolesRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	  @Autowired 
	  private UserDetailsRepository userRepo;
	  
	  @Autowired 
	  private UserVsRolesRepository userRoleRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 
		UserDetailsEntity user = userRepo.findByEmail(email)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    List<String> roles = userRoleRepo.findRolesByUserId(user.getUserId());
	    return new CustomUserDetails(user, roles);
	}

	
}
