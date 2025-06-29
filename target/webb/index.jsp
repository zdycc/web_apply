<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 检查用户是否已登录
    if (session.getAttribute("currentUser") != null) {
        response.sendRedirect(request.getContextPath() + "/home");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>