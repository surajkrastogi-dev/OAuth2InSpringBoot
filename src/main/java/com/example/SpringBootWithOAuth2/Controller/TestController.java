package com.example.SpringBootWithOAuth2.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/secure")
    public String secureApi() {
        return "OAuth2.0 Access Granted for secured user only!";
    }
    
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userApi() {
        return "OAuth2.0 User Access Granted!";
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "OAuth2.0 Admin Access Granted!";
    }
    
    @GetMapping("/userActivity")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String userActivityApi() {
        return "OAuth2.0 Admin and User Access Granted for these User Activity Page!";
    }
}
