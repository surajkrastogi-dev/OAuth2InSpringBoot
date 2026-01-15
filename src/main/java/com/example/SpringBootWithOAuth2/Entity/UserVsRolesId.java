package com.example.SpringBootWithOAuth2.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserVsRolesId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    public UserVsRolesId() {}

    public UserVsRolesId(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    /* =======================
       equals & hashCode
       (MANDATORY for composite keys)
       ======================= */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserVsRolesId)) return false;
        UserVsRolesId that = (UserVsRolesId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

    /* =======================
       Getters & Setters
       ======================= */

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}

