package com.webb.service;

// 自定义用户服务异常类
public class UserServiceException extends Exception {
    public UserServiceException(String message) {
        super(message);
    }
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
