package com.webb.service;

// 自定义角色服务异常类
public class RoleServiceException extends Exception {
    public RoleServiceException(String message) {
        super(message);
    }

    public RoleServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
