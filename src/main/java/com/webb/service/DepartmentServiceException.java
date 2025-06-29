package com.webb.service;

// 自定义部门服务异常类
public class DepartmentServiceException extends Exception {
    public DepartmentServiceException(String message) {
        super(message);
    }
    public DepartmentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
