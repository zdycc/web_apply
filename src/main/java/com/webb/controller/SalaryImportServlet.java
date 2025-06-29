package com.webb.controller;

import com.webb.service.SalaryService;
import com.webb.service.SalaryServiceException;
import com.webb.service.impl.SalaryServiceImpl;
import com.webb.util.AuditLogHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

@WebServlet("/importSalary")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB：超过这个大小的文件会直接写入磁盘，而不是内存
        maxFileSize = 1024 * 1024 * 10,      // 10MB：允许上传的单个文件最大大小
        maxRequestSize = 1024 * 1024 * 15   // 15MB：整个HTTP请求的最大大小
)
public class SalaryImportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SalaryImportServlet.class);
    private SalaryService salaryService = new SalaryServiceImpl();


     // 处理GET请求，直接显示文件上传的表单页面。
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 记录查看日志
        AuditLogHelper.logSuccess(request, "访问工资导入页面", "N/A", "用户访问了工资批量导入页面。");
        forwardToLayout(request, response, "工资批量导入", "/WEB-INF/jsp/salary/salary_import_form_content.jsp", "salary_import");
    }


     //处理POST请求，即处理上传的Excel文件。
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String yearMonth = request.getParameter("yearMonth");
        Part filePart = request.getPart("salaryFile");

        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("errorMessage", "请选择一个文件进行上传。");
            doGet(request, response);
            return;
        }

        if (yearMonth == null || yearMonth.isEmpty()) {
            request.setAttribute("errorMessage", "请选择工资所属月份。");
            doGet(request, response);
            return;
        }

        String fileName = filePart.getSubmittedFileName();
        logger.info("开始处理上传的工资文件: {}, 所属月份: {}", fileName, yearMonth);

        try (InputStream fileContent = filePart.getInputStream()) {
            // 调用Service层处理导入逻辑
            String resultMessage = salaryService.batchImportSalaries(fileContent, yearMonth);

            // 记录成功日志
            AuditLogHelper.logSuccess(request, "工资批量导入", "文件名: " + fileName, "导入处理完成。摘要: " + resultMessage.replace("\n", " "));
            request.setAttribute("successMessage", "文件导入处理完成。<br/><pre>" + resultMessage + "</pre>");

        } catch (SalaryServiceException e) {
            logger.error("工资批量导入失败: {}", e.getMessage(), e);
            // 记录失败日志
            AuditLogHelper.logFailure(request, "工资批量导入", "文件名: " + fileName, "导入失败: " + e.getMessage());
            request.setAttribute("errorMessage", "导入失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("处理上传文件时发生未知错误: {}", e.getMessage(), e);
            AuditLogHelper.logFailure(request, "工资批量导入", "文件名: " + fileName, "处理文件时发生严重错误: " + e.getMessage());
            request.setAttribute("errorMessage", "处理文件时发生严重错误，请检查文件格式或联系管理员。");
        }

        // 处理完成后，再次显示上传页面并附带结果消息
        doGet(request, response);
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, String pageTitle, String contentPage, String activeNavItem) throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activeNavItem", activeNavItem);
        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
}