package com.webb.controller;

import com.webb.model.User;
import com.webb.service.LoginException;
import com.webb.service.UserService;
import com.webb.service.impl.UserServiceImpl;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService = new UserServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userService.login(username, password);
            // 登录成功
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("roleName", user.getRole().getRoleName());

            AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.LOGIN_SUCCESS, "用户: " + username, "用户登录成功。");

            //检查密码过期标志并强制跳转
            if (user.isPasswordExpired()) {
                session.setAttribute("forcePasswordChange", true); // 设置一个强制修改密码的会话标志
                request.getSession().setAttribute("infoMessage", "您的密码已超过90天未登录，为保证安全，请修改密码。");
                response.sendRedirect(request.getContextPath() + "/changePassword");
                return; // 终止后续操作，直接跳转
            }

            response.sendRedirect(request.getContextPath() + "/home");

        } catch (LoginException e) {
            logger.warn("用户 {} 登录失败: {}", username, e.getMessage());
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.LOGIN_FAILURE, "用户: " + username, "登录失败原因: " + e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("登录过程中发生未知错误 for user: {}", username, e);
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.LOGIN_FAILURE, "用户: " + username, "系统内部错误: " + e.getMessage());
            request.setAttribute("errorMessage", "系统繁忙，请稍后再试。");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}