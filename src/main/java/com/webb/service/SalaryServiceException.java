package com.webb.service;

// 自定义工资服务异常类
public class SalaryServiceException extends Exception {
    public SalaryServiceException(String message) {
        super(message);
    }

    public SalaryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
