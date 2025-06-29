package com.webb.service;

// 自定义员工服务异常类
public class EmployeeServiceException extends Exception {
    public EmployeeServiceException(String message) {
        super(message);
    }

    public EmployeeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
