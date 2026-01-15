package com.example.SpringBootWithOAuth2.Model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    public String email;
    public String password;
    private String deviceId;
}

