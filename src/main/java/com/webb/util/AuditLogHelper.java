package com.webb.util;

import com.webb.model.AuditLog;
import com.webb.model.User;
import com.webb.service.AuditLogService;
import com.webb.service.impl.AuditLogServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class AuditLogHelper {

    private static final AuditLogService auditLogService = new AuditLogServiceImpl();

    /**
     * 记录操作日志的通用方法
     * @param request HttpServletRequest 对象，用于获取用户信息和IP地址
     * @param actionType 操作类型 (建议使用标准化常量)
     * @param status 操作状态 ("SUCCESS" 或 "FAILURE")
     * @param targetResource 操作对象描述
     * @param details 详细信息
     */
    public static void log(HttpServletRequest request, String actionType, String status, String targetResource, String details) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        Integer userId = null;
        String username = "[未登录用户]";

        if (currentUser != null) {
            userId = currentUser.getId();
            username = currentUser.getUsername();
        } else {
            // 尝试从登录请求中获取用户名（用于记录登录失败日志）
            String loginUsername = request.getParameter("username");
            if (loginUsername != null) {
                username = loginUsername;
            }
        }

        String ipAddress = getClientIp(request);

        AuditLog log = new AuditLog(userId, username, ipAddress, actionType, status, targetResource, details);
        auditLogService.recordLog(log);
    }

    /**
     * 更简洁的日志记录方法 (成功状态)
     */
    public static void logSuccess(HttpServletRequest request, String actionType, String targetResource, String details) {
        log(request, actionType, "SUCCESS", targetResource, details);
    }

    /**
     * 更简洁的日志记录方法 (失败状态)
     */
    public static void logFailure(HttpServletRequest request, String actionType, String targetResource, String details) {
        log(request, actionType, "FAILURE", targetResource, details);
    }


    /**
     * 获取客户端IP地址
     * @param request HttpServletRequest 对象
     * @return 客户端IP地址
     */
    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static final class ActionType {
        public static final String LOGIN_SUCCESS = "用户登录成功";
        public static final String LOGIN_FAILURE = "用户登录失败";
        public static final String LOGOUT = "用户登出";

        public static final String VIEW_DEPARTMENTS = "查看部门列表";
        public static final String ADD_DEPARTMENT = "添加部门";
        public static final String UPDATE_DEPARTMENT = "更新部门";
        public static final String DELETE_DEPARTMENT = "删除部门";

        public static final String VIEW_EMPLOYEES = "查看员工列表";
        public static final String ADD_EMPLOYEE = "添加员工";
        public static final String UPDATE_EMPLOYEE = "更新员工";
        public static final String DELETE_EMPLOYEE = "删除员工";

        // ... 其他操作类型 ...
        public static final String VIEW_ANNUAL_DEDUCTIONS = "查看年度专项扣除";
        public static final String SAVE_ANNUAL_DEDUCTION = "保存年度专项扣除";
        public static final String DELETE_ANNUAL_DEDUCTION = "删除年度专项扣除";

        public static final String VIEW_DEPENDENTS = "查看被抚养人";
        public static final String SAVE_DEPENDENT = "保存被抚养人";
        public static final String DELETE_DEPENDENT = "删除被抚养人";

        public static final String VIEW_SALARY_LIST = "查看月度工资列表";
        public static final String SAVE_SALARY = "保存月度工资";
    }
}