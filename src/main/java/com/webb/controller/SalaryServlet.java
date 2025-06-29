package com.webb.controller;

import com.webb.model.Department;
import com.webb.model.Employee;
import com.webb.model.MonthlySalary;
import com.webb.service.*;
import com.webb.service.impl.DepartmentServiceImpl;
import com.webb.service.impl.EmployeeServiceImpl;
import com.webb.service.impl.SalaryServiceImpl;
import com.webb.util.AuditLogHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/salaries")
public class SalaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SalaryServlet.class);

    private SalaryService salaryService = new SalaryServiceImpl();
    private EmployeeService employeeService = new EmployeeServiceImpl();
    private DepartmentService departmentService = new DepartmentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list_current_month";
        }

        try {
            switch (action) {
                case "list_current_month":
                    listSalariesForMonth(request, response, YearMonth.now().toString());
                    break;
                case "list_by_month":
                    String yearMonthToList = request.getParameter("yearMonth");
                    if (!isValidYearMonth(yearMonthToList)) {
                        yearMonthToList = YearMonth.now().toString();
                        request.setAttribute("infoMessage", "未提供有效月份，显示当月工资。");
                    }
                    listSalariesForMonth(request, response, yearMonthToList);
                    break;
                case "edit_form":
                    showSalaryForm(request, response);
                    break;
                case "history_query":
                    showHistoryQueryPage(request, response);
                    break;
                default:
                    logger.warn("未知的GET action: {}", action);
                    listSalariesForMonth(request, response, YearMonth.now().toString());
                    break;
            }
        } catch (SalaryServiceException | EmployeeServiceException | DepartmentServiceException e) {
            logger.error("工资管理(GET)操作失败: action={}, error={}", action, e.getMessage(), e);
            request.setAttribute("errorMessage", "操作失败：" + e.getMessage());
            try {
                listSalariesForMonth(request, response, YearMonth.now().toString());
            } catch (Exception exFallback) {
                logger.error("工资管理(GET)错误处理中的回退操作也失败了: {}", exFallback.getMessage(), exFallback);
                forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "严重错误", "salaries");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (!"save_salary".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的操作请求。");
            return;
        }

        try {
            saveSalary(request, response);
        } catch (SalaryServiceException | EmployeeServiceException e) {
            logger.error("工资管理(POST)操作失败: action={}, error={}", action, e.getMessage(), e);
            request.setAttribute("errorMessage", "保存失败：" + e.getMessage());
            try {
                MonthlySalary salaryForForm = populateSalaryFromRequest(request);
                if (salaryForForm.getEmployeeId() > 0) {
                    Employee employee = employeeService.getEmployeeById(salaryForForm.getEmployeeId());
                    request.setAttribute("currentEmployee", employee);
                }
                forwardToLayout(request, response, "/WEB-INF/jsp/salary/monthly_salary_form_content.jsp",
                        (salaryForForm.getId() != null && salaryForForm.getId() > 0) ? "编辑工资(重试)" : "录入工资(重试)",
                        "salaries");
            } catch (Exception exFallback) {
                logger.error("工资管理(POST)错误处理中的回退操作也失败: {}", exFallback.getMessage(), exFallback);
                response.sendRedirect(request.getContextPath() + "/salaries?action=list_current_month&error=save_failed");
            }
        }
    }

    private boolean isValidYearMonth(String yearMonthStr) {
        if (yearMonthStr == null) return false;
        try {
            YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern("yyyy-MM"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void listSalariesForMonth(HttpServletRequest request, HttpServletResponse response, String yearMonth) throws ServletException, IOException, SalaryServiceException {
        AuditLogHelper.logSuccess(request, "查看月度工资列表", "工资月份: " + yearMonth, "查看了 " + yearMonth + " 的月度工资列表。");

        Map<String, Object> filters = new HashMap<>();
        filters.put("yearMonthExact", yearMonth);
        List<MonthlySalary> salaries = salaryService.getSalariesByFilters(filters, 1, 1000, "e.employee_number ASC");

        request.setAttribute("salaries", salaries);
        request.setAttribute("currentYearMonth", yearMonth);

        forwardToLayout(request, response, "/WEB-INF/jsp/salary/monthly_salary_list_content.jsp", yearMonth + " 工资列表", "salaries");
    }

    private void showSalaryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SalaryServiceException, EmployeeServiceException {
        String employeeIdStr = request.getParameter("employeeId");
        String yearMonthStr = request.getParameter("yearMonth");
        String salaryIdStr = request.getParameter("salaryId");

        if (employeeIdStr == null || employeeIdStr.isEmpty() || !isValidYearMonth(yearMonthStr)) {
            throw new SalaryServiceException("必须提供员工ID和有效的工资月份(格式yyyy-MM)。");
        }

        int employeeId = Integer.parseInt(employeeIdStr);
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) throw new SalaryServiceException("未找到员工ID: " + employeeId);
        request.setAttribute("currentEmployee", employee);

        MonthlySalary salary;
        if (salaryIdStr != null && !salaryIdStr.isEmpty()) {
            long salaryId = Long.parseLong(salaryIdStr);
            salary = salaryService.getMonthlySalaryById(salaryId);
            if (salary == null || salary.getEmployeeId() != employeeId || !salary.getYearMonth().equals(yearMonthStr)) {
                throw new SalaryServiceException("未找到指定的工资记录，或记录与员工/月份不匹配。");
            }
        } else {
            salary = salaryService.getMonthlySalaryByEmployeeAndYearMonth(employeeId, yearMonthStr);
            if (salary == null) {
                salary = new MonthlySalary();
                salary.setEmployeeId(employeeId);
                salary.setYearMonth(yearMonthStr);
            }
        }

        salary = salaryService.calculateSalaryComponents(salary);
        request.setAttribute("salary", salary);
        String pageTitleSuffix = (salary.getId() != null && salary.getId() > 0) ? "编辑工资" : "录入工资";

        forwardToLayout(request, response, "/WEB-INF/jsp/salary/monthly_salary_form_content.jsp", pageTitleSuffix, "salaries");
    }

    private void showHistoryQueryPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SalaryServiceException, DepartmentServiceException {
        //加载部门列表用于筛选表单
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);

        //获取分页和筛选参数
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
        int pageSize = 15;

        String yearMonthStart = request.getParameter("yearMonthStart");
        String yearMonthEnd = request.getParameter("yearMonthEnd");
        String departmentIdStr = request.getParameter("departmentId");
        String employeeNumber = request.getParameter("employeeNumber");

        Map<String, Object> filters = new HashMap<>();
        if (yearMonthStart != null && !yearMonthStart.isEmpty()) filters.put("yearMonthStart", yearMonthStart);
        if (yearMonthEnd != null && !yearMonthEnd.isEmpty()) filters.put("yearMonthEnd", yearMonthEnd);
        if (departmentIdStr != null && !departmentIdStr.isEmpty() && !"0".equals(departmentIdStr)) {
            filters.put("departmentId", Integer.parseInt(departmentIdStr));
        }
        if (employeeNumber != null && !employeeNumber.isEmpty()) filters.put("employeeNumber", employeeNumber);

        //无论是否有筛选条件，都执行查询
        if (!filters.isEmpty()) {
            AuditLogHelper.logSuccess(request, "历史工资查询", "查询条件: " + request.getQueryString(), "执行了一次历史工资查询。");
        }

        List<MonthlySalary> salaries = salaryService.getSalariesByFilters(filters, pageNum, pageSize, null);
        int totalRecords = salaryService.countSalariesByFilters(filters);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("salaries", salaries);
        request.setAttribute("currentPage", pageNum);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        // 构建用于分页链接的查询参数字符串
        StringBuilder searchParams = new StringBuilder();
        if (yearMonthStart != null) searchParams.append("yearMonthStart=").append(URLEncoder.encode(yearMonthStart, StandardCharsets.UTF_8)).append("&");
        if (yearMonthEnd != null) searchParams.append("yearMonthEnd=").append(URLEncoder.encode(yearMonthEnd, StandardCharsets.UTF_8)).append("&");
        if (departmentIdStr != null) searchParams.append("departmentId=").append(URLEncoder.encode(departmentIdStr, StandardCharsets.UTF_8)).append("&");
        if (employeeNumber != null) searchParams.append("employeeNumber=").append(URLEncoder.encode(employeeNumber, StandardCharsets.UTF_8)).append("&");
        request.setAttribute("searchParams", searchParams.toString());

        //转发到视图
        forwardToLayout(request, response, "/WEB-INF/jsp/salary/salary_history_query_content.jsp", "历史工资查询", "salary_history");
    }

    private MonthlySalary populateSalaryFromRequest(HttpServletRequest request) {
        MonthlySalary salary = new MonthlySalary();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            salary.setId(Long.parseLong(idStr));
        }

        try {
            salary.setEmployeeId(Integer.parseInt(request.getParameter("employeeId")));
            salary.setYearMonth(request.getParameter("yearMonth"));
            salary.setBasicSalary(parseBigDecimal(request, "basicSalary"));
            salary.setPostAllowance(parseBigDecimal(request, "postAllowance"));
            salary.setLunchSubsidy(parseBigDecimal(request, "lunchSubsidy"));
            salary.setOvertimePay(parseBigDecimal(request, "overtimePay"));
            salary.setAttendanceBonus(parseBigDecimal(request, "attendanceBonus"));
            salary.setOtherEarnings(parseBigDecimal(request, "otherEarnings"));
            salary.setSocialSecurityPersonal(parseBigDecimal(request, "socialSecurityPersonal"));
            salary.setProvidentFundPersonal(parseBigDecimal(request, "providentFundPersonal"));
            String specialDeductionStr = request.getParameter("specialAdditionalDeductionMonthly_manual");
            if (specialDeductionStr != null && !specialDeductionStr.isEmpty()) {
                salary.setSpecialAdditionalDeductionMonthly(parseBigDecimal(request, "specialAdditionalDeductionMonthly_manual"));
            }
            salary.setEnterpriseAnnuityPersonal(parseBigDecimal(request, "enterpriseAnnuityPersonal"));
            salary.setOtherPreTaxDeductions(parseBigDecimal(request, "otherPreTaxDeductions"));
            salary.setLateLeaveDeduction(parseBigDecimal(request, "lateLeaveDeduction"));
            salary.setOtherPostTaxDeductions(parseBigDecimal(request, "otherPostTaxDeductions"));
            salary.setSalaryStatus(request.getParameter("salaryStatus"));
            salary.setRemarks(request.getParameter("remarks"));
        } catch (NumberFormatException e) {
            logger.warn("解析工资表单数据时发生数字格式错误: {}", e.getMessage());
            request.setAttribute("parsingError", "部分数字字段格式不正确。");
        }
        request.setAttribute("salary", salary);
        return salary;
    }

    private BigDecimal parseBigDecimal(HttpServletRequest request, String paramName) {
        String valueStr = request.getParameter(paramName);
        if (valueStr != null && !valueStr.trim().isEmpty()) {
            try {
                return new BigDecimal(valueStr.trim());
            } catch (NumberFormatException e) {
                logger.warn("解析BigDecimal参数 '{}' 失败: value='{}'", paramName, valueStr);
                String currentErrors = (String) request.getAttribute("parsingError");
                String errorMsg = "字段 " + paramName + " 的金额格式不正确。";
                request.setAttribute("parsingError", (currentErrors == null) ? errorMsg : currentErrors + " " + errorMsg);
            }
        }
        return BigDecimal.ZERO;
    }

    private void saveSalary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SalaryServiceException, EmployeeServiceException {
        MonthlySalary salary = populateSalaryFromRequest(request);
        if (request.getAttribute("parsingError") != null) {
            request.setAttribute("errorMessage", (String)request.getAttribute("parsingError"));
            if (salary.getEmployeeId() > 0 && request.getAttribute("currentEmployee") == null) {
                request.setAttribute("currentEmployee", employeeService.getEmployeeById(salary.getEmployeeId()));
            }
            forwardToLayout(request, response, "/WEB-INF/jsp/salary/monthly_salary_form_content.jsp",
                    (salary.getId() != null && salary.getId() > 0) ? "编辑工资(重试)" : "录入工资(重试)", "salaries");
            return;
        }

        salaryService.saveOrUpdateMonthlySalary(salary);
        request.getSession().setAttribute("successMessage", "员工 " + salary.getEmployeeId() + " 的 " + salary.getYearMonth() + " 工资已成功保存和计算！");
        response.sendRedirect(request.getContextPath() + "/salaries?action=list_by_month&yearMonth=" + salary.getYearMonth());
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, String contentPagePath, String pageTitle, String activeNavItem) throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activeNavItem", activeNavItem);
        request.setAttribute("contentPage", contentPagePath);
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
}