package com.webb.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private int roleId;
    private Role role;
    private Integer employeeId;
    private Timestamp lastPasswordChangeDate;
    private int failedLoginAttempts;
    private Timestamp lockoutUntil;
    private boolean isActive; // 字段名保持不变
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLoginTime;
    private transient boolean passwordExpired; // transient关键字表示此字段不参与序列化

    // 构造函数
    public User() {}

    // Getter 和 Setter 方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Timestamp getLastPasswordChangeDate() { return lastPasswordChangeDate; }
    public void setLastPasswordChangeDate(Timestamp lastPasswordChangeDate) { this.lastPasswordChangeDate = lastPasswordChangeDate; }

    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }

    public Timestamp getLockoutUntil() { return lockoutUntil; }
    public void setLockoutUntil(Timestamp lockoutUntil) { this.lockoutUntil = lockoutUntil; }
    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public boolean getIsActive() { // 将 isActive() 重命名为 getIsActive()
        return isActive;
    }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roleId=" + roleId +
                ", employeeId=" + employeeId +
                ", isActive=" + isActive +
                '}';
    }
}