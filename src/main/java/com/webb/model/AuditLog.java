package com.webb.model;

import java.sql.Timestamp;

public class AuditLog {
    private Long id;
    private Integer userId;
    private String username;
    private String ipAddress;
    private String actionType;
    private String targetResource;
    private String details;
    private String status;
    private Timestamp logTime;
    private User user;
    public AuditLog() {}

    public AuditLog(Integer userId, String username, String ipAddress, String actionType, String status, String targetResource, String details) {
        this.userId = userId;
        this.username = username;
        this.ipAddress = ipAddress;
        this.actionType = actionType;
        this.status = status;
        this.targetResource = targetResource;
        this.details = details;
    }

    // --- Getter 和 Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getTargetResource() { return targetResource; }
    public void setTargetResource(String targetResource) { this.targetResource = targetResource; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getLogTime() { return logTime; }
    public void setLogTime(Timestamp logTime) { this.logTime = logTime; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", actionType='" + actionType + '\'' +
                ", status='" + status + '\'' +
                ", logTime=" + logTime +
                '}';
    }
}