package com.webb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("pageTitle", "仪表盘"); // 设置页面标题
        request.setAttribute("activeNavItem", "home"); // 设置当前活动的导航项
        request.setAttribute("contentPage", "/WEB-INF/jsp/dashboard_content.jsp"); // 设置要包含的内容页面
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response); // 转发到布局页面
    }
}