package com.example.OAuth2InSpringBoot.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user_detail",schema = "defaultdb")
public class UserDetailsEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "mobile_no", nullable = false, length = 10)
    private String mobileNo;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "passwords", nullable = false, length = 100)
    private String password;

    @Column(name = "pincode", length = 6)
    private String pincode;

    @Column(name = "state_code", length = 20)
    private String stateCode;

    @Column(name = "country", length = 20)
    private String country;

    @Column(name = "pan_no", length = 10)
    private String panNo;

    @Column(name = "aadhar_no", length = 12)
    private String aadharNo;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "active_flag")
    private Boolean activeFlag;
    
    @Column(name="is_google_oauth_login")
    private boolean isGoogleOauthLogin;
    
    @Column(name="auth_provider")
    private String authProvider;
    
//    @Column(name = "role", length = 20)
//    private String role; // ADMIN or USER


    /* ==================== Lifecycle Callbacks ==================== */

//    @PrePersist
//    protected void onCreate() {
//        this.createdOn = LocalDateTime.now();
//        this.updatedOn = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedOn = LocalDateTime.now();
//    }

    /* ==================== Getters & Setters ==================== */

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPincode() {
        return pincode;
    }
    
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStateCode() {
        return stateCode;
    }
    
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }

    public String getPanNo() {
        return panNo;
    }
    
    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }
    
    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public Boolean getActiveFlag() {
        return activeFlag;
    }
    
    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

//	public String getRole() {
//		return role;
//	}
//
//	public void setRole(String role) {
//		this.role = role;
//	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public boolean isGoogleOauthLogin() {
		return isGoogleOauthLogin;
	}

	public void setGoogleOauthLogin(boolean isGoogleOauthLogin) {
		this.isGoogleOauthLogin = isGoogleOauthLogin;
	}
	
	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	@Override
	public String toString() {
		return "UserDetailsEntity [userId=" + userId + ", userName=" + userName + ", mobileNo=" + mobileNo + ", email="
				+ email + ", password=" + password + ", pincode=" + pincode + ", stateCode=" + stateCode + ", country="
				+ country + ", panNo=" + panNo + ", aadharNo=" + aadharNo + ", dateOfBirth=" + dateOfBirth
				+ ", address=" + address + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", activeFlag="
				+ activeFlag + ", isGoogleOauthLogin=" + isGoogleOauthLogin + "]";
	}

    
}
