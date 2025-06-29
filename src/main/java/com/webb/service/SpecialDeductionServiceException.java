package com.webb.service;

// 自定义专项扣除服务异常类
public class SpecialDeductionServiceException extends Exception {
    public SpecialDeductionServiceException(String message) {
        super(message);
    }

    public SpecialDeductionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
