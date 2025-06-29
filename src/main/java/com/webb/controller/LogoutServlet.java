package com.webb.controller;

import com.webb.util.AuditLogHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // 获取现有会话，不创建新的
        if (session != null) {
            String username = (String) session.getAttribute("username");

            //在 session 失效前记录日志
            AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.LOGOUT, "用户: " + (username != null ? username : "[未知]"), "用户主动登出。");

            session.invalidate(); // 使会话无效
            logger.info("用户 {} 已成功退出登录。", username != null ? username : "未知用户");
        }
        // 重定向到登录页面
        response.sendRedirect(request.getContextPath() + "/login");
    }
}