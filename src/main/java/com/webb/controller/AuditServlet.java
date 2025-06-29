package com.webb.controller;

import com.webb.model.AuditLog;
import com.webb.service.AuditLogService;
import com.webb.service.AuditLogServiceException;
import com.webb.service.impl.AuditLogServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/audit")
public class AuditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AuditServlet.class);

    private AuditLogService auditLogService = new AuditLogServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "view_logs"; // 默认动作
        }

        try {
            switch (action) {
                case "view_logs":
                    viewLogs(request, response);
                    break;
                default:
                    logger.warn("未知的GET action in AuditServlet: {}", action);
                    viewLogs(request, response); // 默认显示日志列表
                    break;
            }
        } catch (AuditLogServiceException e) {
            logger.error("审计日志管理操作失败: action={}, error={}", action, e.getMessage(), e);
            request.setAttribute("errorMessage", "操作失败：" + e.getMessage());
            forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "审计日志错误", "audit");
        }
    }

    private void viewLogs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, AuditLogServiceException {
        // 获取分页参数
        int pageNum = 1;
        String pageNumStr = request.getParameter("pageNum");
        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            try {
                pageNum = Integer.parseInt(pageNumStr);
                if (pageNum < 1) pageNum = 1;
            } catch (NumberFormatException e) {
                logger.warn("无效的页码参数: {}", pageNumStr);
            }
        }
        int pageSize = 20; // 每页显示20条日志

        // 获取筛选条件
        String usernameFilter = request.getParameter("username");
        String actionTypeFilter = request.getParameter("actionType");
        String startDateFilter = request.getParameter("startDate");
        String endDateFilter = request.getParameter("endDate");

        Map<String, Object> filters = new HashMap<>();
        if (usernameFilter != null && !usernameFilter.isEmpty()) filters.put("username", usernameFilter);
        if (actionTypeFilter != null && !actionTypeFilter.isEmpty()) filters.put("actionType", actionTypeFilter);
        if (startDateFilter != null && !startDateFilter.isEmpty()) filters.put("startDate", startDateFilter);
        if (endDateFilter != null && !endDateFilter.isEmpty()) filters.put("endDate", endDateFilter);

        // 查询数据
        List<AuditLog> logs = auditLogService.getLogsByFilters(filters, pageNum, pageSize, null); // 使用默认排序 (log_time DESC)
        int totalRecords = auditLogService.countLogsByFilters(filters);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("logs", logs);
        request.setAttribute("currentPage", pageNum);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        // 构建用于分页链接的查询参数字符串
        StringBuilder searchParams = new StringBuilder();
        if (usernameFilter != null) searchParams.append("username=").append(URLEncoder.encode(usernameFilter, StandardCharsets.UTF_8)).append("&");
        if (actionTypeFilter != null) searchParams.append("actionType=").append(URLEncoder.encode(actionTypeFilter, StandardCharsets.UTF_8)).append("&");
        if (startDateFilter != null) searchParams.append("startDate=").append(URLEncoder.encode(startDateFilter, StandardCharsets.UTF_8)).append("&");
        if (endDateFilter != null) searchParams.append("endDate=").append(URLEncoder.encode(endDateFilter, StandardCharsets.UTF_8)).append("&");
        request.setAttribute("searchParams", searchParams.toString());

        forwardToLayout(request, response, "/WEB-INF/jsp/audit/audit_log_view_content.jsp", "安全审计日志", "audit");
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, String contentPagePath, String pageTitle, String activeNavItem) throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activeNavItem", activeNavItem);
        request.setAttribute("contentPage", contentPagePath);
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
}