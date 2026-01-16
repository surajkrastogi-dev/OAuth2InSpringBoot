package com.example.OAuth2InSpringBoot.Model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String userName;
    private String mobileNo;   
    private String email;
    private String password;
    private String pincode;
    private String stateCode;
    private String country;
    private String panNo;
    private String aadharNo;
    private LocalDate dateOfBirth;
    private String address;
    private String roleType;

}


