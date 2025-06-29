package com.webb.service;

// 自定义登录异常类
public class LoginException extends Exception {
    public LoginException(String message) {
        super(message);
    }
}
