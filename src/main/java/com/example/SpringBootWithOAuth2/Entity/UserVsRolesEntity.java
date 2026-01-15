package com.example.SpringBootWithOAuth2.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_vs_roles")
public class UserVsRolesEntity {

	    @EmbeddedId
	    private UserVsRolesId id;

	    /* =======================
	       Relationship Mapping
	       ======================= */

	    @ManyToOne(fetch = FetchType.LAZY)
	    @MapsId("roleId")
	    @JoinColumn(name = "role_id", nullable = false)
	    private MasterRoleEntity role;

	    // If you later want to map User entity:
	    // @ManyToOne(fetch = FetchType.LAZY)
	    // @MapsId("userId")
	    // @JoinColumn(name = "user_id", nullable = false)
	    // private UserDetailsEntity user;

	    @Column(name = "created_on", updatable = false)
	    private LocalDateTime createdOn;

	    @Column(name = "updated_on")
	    private LocalDateTime updatedOn;

	    @Column(name = "active_flag")
	    private Boolean activeFlag;

	    /* =======================
	       Lifecycle Callbacks
	       ======================= */

	    @PrePersist
	    protected void onCreate() {
	        this.createdOn = LocalDateTime.now();
	        this.updatedOn = LocalDateTime.now();
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        this.updatedOn = LocalDateTime.now();
	    }

	    /* =======================
	       Getters & Setters
	       ======================= */

	    public UserVsRolesId getId() {
	        return id;
	    }

	    public void setId(UserVsRolesId id) {
	        this.id = id;
	    }

	    public MasterRoleEntity getRole() {
	        return role;
	    }

	    public void setRole(MasterRoleEntity role) {
	        this.role = role;
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
	}
