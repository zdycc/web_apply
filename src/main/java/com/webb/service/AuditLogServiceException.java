package com.webb.service;

// 自定义审计日志服务异常类
public class AuditLogServiceException extends Exception {
    public AuditLogServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
