package com.webb.controller;

import com.webb.model.User;
import com.webb.service.UserService;
import com.webb.service.UserServiceException;
import com.webb.service.impl.UserServiceImpl;
import com.webb.util.AuditLogHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // 确保导入 HttpSession

import java.io.IOException;

@WebServlet("/changePassword")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求只负责显示修改密码的表单页面
        // 为了能在layout中显示，我们同样使用forwardToLayout
        forwardToLayout(request, response, "修改密码", "/WEB-INF/jsp/change_password_content.jsp", "change_password");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // 获取现有会话，不创建新的
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User currentUser = (User) session.getAttribute("currentUser");

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "新密码和确认密码不一致！");
            doGet(request, response);
            return;
        }

        try {
            // 在调用service前，先检查是否存在强制修改密码的标志
            Object forceChangeFlag = session.getAttribute("forcePasswordChange");

            // 调用Service层修改密码
            userService.changePassword(currentUser.getId(), oldPassword, newPassword);

            AuditLogHelper.logSuccess(request, "修改个人密码", "用户: " + currentUser.getUsername(), "用户成功修改了自己的密码。");

            // 根据是否存在强制修改标志，决定后续操作
            if (forceChangeFlag != null && (Boolean) forceChangeFlag) {
                // 这是强制修改密码的流程
                // 1. 使当前会话失效，强制用户登出
                session.invalidate();
                // 2. 重定向到登录页面，并附带一个成功提示参数
                String successMessage = java.net.URLEncoder.encode("密码已成功更新，请使用新密码重新登录。", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/login?successMessage=" + successMessage);
            } else {
                // 这是用户主动修改密码的流程
                // 1. 在request中设置成功消息
                request.setAttribute("successMessage", "密码修改成功！");
                // 2. 再次显示修改密码页面，并显示成功消息
                doGet(request, response);
            }

        } catch (UserServiceException e) {
            AuditLogHelper.logFailure(request, "修改个人密码", "用户: " + currentUser.getUsername(), "修改密码失败: " + e.getMessage());
            request.setAttribute("errorMessage", "密码修改失败: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, String pageTitle, String contentPage, String activeNavItem) throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activeNavItem", activeNavItem);
        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
}