package com.webb.controller;

import com.webb.model.Department;
import com.webb.model.Employee;
import com.webb.model.EmployeeAnnualDeduction;
import com.webb.model.EmployeeDependent;
import com.webb.service.DepartmentService;
import com.webb.service.EmployeeService;
import com.webb.service.EmployeeServiceException;
import com.webb.service.SpecialDeductionService;
import com.webb.service.SpecialDeductionServiceException;
import com.webb.service.impl.EmployeeServiceImpl;
import com.webb.service.impl.SpecialDeductionServiceImpl;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/specialDeductions")
public class SpecialDeductionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SpecialDeductionServlet.class);

    private SpecialDeductionService specialDeductionService = new SpecialDeductionServiceImpl();
    private EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String employeeIdStr = request.getParameter("employeeId");

        if (action == null) {
            action = "list_employees_for_deduction";
        }

        int employeeId = 0;
        if (employeeIdStr != null && !employeeIdStr.isEmpty()) {
            try {
                employeeId = Integer.parseInt(employeeIdStr);
            } catch (NumberFormatException e) {
                logger.error("无效的员工ID格式: {}", employeeIdStr, e);
                request.setAttribute("errorMessage", "无效的员工ID格式。");
                forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "错误", "error");
                return;
            }
        }

        try {
            if (employeeId > 0 && !"list_employees_for_deduction".equals(action)) {
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee == null) {
                    throw new SpecialDeductionServiceException("未找到ID为 " + employeeId + " 的员工。");
                }
                request.setAttribute("currentEmployee", employee);
            }

            switch (action) {
                case "list_employees_for_deduction":
                    listEmployeesForDeductionChoice(request, response);
                    break;
                case "list_annual_deductions":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("查看年度扣除列表必须提供员工ID。");
                    listAnnualDeductions(request, response, employeeId);
                    break;
                case "add_annual_form":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("添加年度扣除记录必须提供员工ID。");
                    showAnnualDeductionForm(request, response, employeeId, null);
                    break;
                case "edit_annual_form":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("编辑年度扣除记录必须提供员工ID。");
                    int annualDeductionIdToEdit = Integer.parseInt(request.getParameter("id"));
                    showAnnualDeductionForm(request, response, employeeId, annualDeductionIdToEdit);
                    break;
                case "delete_annual":
                    if (employeeId <= 0)
                        throw new SpecialDeductionServiceException("删除年度扣除记录必须关联员工ID以便重定向。");
                    deleteAnnualDeduction(request, response, employeeId);
                    break;
                case "list_dependents":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("查看被抚养人列表必须提供员工ID。");
                    listDependents(request, response, employeeId);
                    break;
                case "add_dependent_form":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("添加被抚养人必须关联员工ID。");
                    showDependentForm(request, response, employeeId, null);
                    break;
                case "edit_dependent_form":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("编辑被抚养人必须关联员工ID。");
                    int dependentIdToEdit = Integer.parseInt(request.getParameter("id"));
                    showDependentForm(request, response, employeeId, dependentIdToEdit);
                    break;
                case "delete_dependent":
                    if (employeeId <= 0)
                        throw new SpecialDeductionServiceException("删除被抚养人必须关联员工ID以便重定向。");
                    deleteDependent(request, response, employeeId);
                    break;
                default:
                    logger.warn("未知的GET action: {}", action);
                    listEmployeesForDeductionChoice(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            logger.error("参数格式错误 (GET): {}", e.getMessage());
            request.setAttribute("errorMessage", "请求参数格式错误。");
            forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "错误", "error");
        } catch (SpecialDeductionServiceException | EmployeeServiceException e) {
            logger.error("专项扣除管理(GET)操作失败: action={}, employeeId={}, error={}", action, employeeIdStr, e.getMessage(), e);
            request.setAttribute("errorMessage", "操作失败：" + e.getMessage());
            try {
                if (action.contains("annual") || action.contains("dependent")) {
                    if (employeeId > 0) {
                        if (action.contains("annual")) {
                            listAnnualDeductions(request, response, employeeId);
                        } else {
                            listDependents(request, response, employeeId);
                        }
                    } else {
                        listEmployeesForDeductionChoice(request, response);
                    }
                } else if ("list_employees_for_deduction".equals(action)) {
                    request.setAttribute("employeesForSelection", List.of());
                    forwardToLayout(request, response, "/WEB-INF/jsp/deduction/employee_selection_for_deduction_content.jsp", "选择员工 - 错误", "special_deductions_main");
                } else {
                    forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "操作失败", "error");
                }
            } catch (Exception exFallback) {
                logger.error("在处理GET操作的异常时，调用回退方法也失败了: {}", exFallback.getMessage(), exFallback);
                forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "系统严重错误", "error");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String employeeIdStr = request.getParameter("employeeId");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "未指定操作。");
            return;
        }

        int employeeId = 0;
        if (employeeIdStr != null && !employeeIdStr.isEmpty()) {
            try {
                employeeId = Integer.parseInt(employeeIdStr);
            } catch (NumberFormatException e) {
                logger.error("无效的员工ID格式 (POST): {}", employeeIdStr, e);
                request.setAttribute("errorMessage", "无效的员工ID格式。");
                forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "错误", "error");
                return;
            }
        }

        if (employeeId > 0 && request.getAttribute("currentEmployee") == null) {
            try {
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee == null) {
                    try {
                        throw new SpecialDeductionServiceException("未找到ID为 " + employeeId + " 的员工以进行POST操作。");
                    } catch (SpecialDeductionServiceException e) {
                        throw new RuntimeException(e);
                    }
                }
                request.setAttribute("currentEmployee", employee);
            } catch (EmployeeServiceException e) {
                logger.error("加载员工信息失败 (POST): {}", e.getMessage());
                request.setAttribute("errorMessage", "加载员工信息失败: " + e.getMessage());
                forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "错误", "error");
                return;
            }
        }

        try {
            switch (action) {
                case "save_annual_deduction":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("保存年度扣除记录必须提供员工ID。");
                    saveAnnualDeduction(request, response, employeeId);
                    break;
                case "save_dependent":
                    if (employeeId <= 0) throw new SpecialDeductionServiceException("保存被抚养人信息必须关联员工ID。");
                    saveDependent(request, response, employeeId);
                    break;
                default:
                    logger.warn("未知的POST action: {}", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的操作请求。");
                    break;
            }
        } catch (NumberFormatException e) {
            logger.error("参数格式错误 (POST): {}", e.getMessage());
            request.setAttribute("errorMessage", "提交的数据格式不正确。");
            try {
                if ("save_annual_deduction".equals(action)) {
                    populateAnnualDeductionFromRequest(request, employeeId);
                    showAnnualDeductionForm(request, response, employeeId, getIdFromRequest(request, "annualDeductionId"));
                } else if ("save_dependent".equals(action)) {
                    populateDependentFromRequest(request, employeeId);
                    showDependentForm(request, response, employeeId, getIdFromRequest(request, "dependentId"));
                } else {
                    forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "错误", "error");
                }
            } catch (Exception exFallback) {
                logger.error("在处理POST NumberFormatException 的后续操作中失败: {}", exFallback.getMessage(), exFallback);
                response.sendRedirect(request.getContextPath() + "/specialDeductions?action=list_employees_for_deduction&error=true");
            }
        } catch (SpecialDeductionServiceException | EmployeeServiceException e) {
            logger.error("专项扣除管理(POST)操作失败: action={}, employeeId={}, error={}", action, employeeIdStr, e.getMessage(), e);
            request.setAttribute("errorMessage", "保存失败：" + e.getMessage());
            try {
                if ("save_annual_deduction".equals(action)) {
                    populateAnnualDeductionFromRequest(request, employeeId);
                    showAnnualDeductionForm(request, response, employeeId, getIdFromRequest(request, "annualDeductionId"));
                } else if ("save_dependent".equals(action)) {
                    populateDependentFromRequest(request, employeeId);
                    showDependentForm(request, response, employeeId, getIdFromRequest(request, "dependentId"));
                } else {
                    listSafeFallback(request, response, employeeId, "处理POST请求时发生未知类型的专项扣除错误。");
                }
            } catch (Exception ex) {
                logger.error("在处理POST操作的异常时，调用表单/列表显示方法也失败了: {}", ex.getMessage(), ex);
                response.sendRedirect(request.getContextPath() + "/specialDeductions?action=list_employees_for_deduction&error=true");
            }
        }
    }

    private void listSafeFallback(HttpServletRequest request, HttpServletResponse response, int employeeId, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        try {
            // 统一回退到员工选择页，因为不确定当前上下文是年度扣除还是被抚养人
            listEmployeesForDeductionChoice(request, response);
        } catch (Exception e) {
            logger.error("安全回退到列表失败: {}", e.getMessage());
            forwardToLayout(request, response, "/WEB-INF/jsp/error_content.jsp", "操作失败", "error");
        }
    }

    private void listEmployeesForDeductionChoice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmployeeServiceException {
        // 获取搜索参数
        String searchKeyword = request.getParameter("searchKeyword");
        String departmentIdStr = request.getParameter("departmentId");
        Integer departmentId = null;
        
        if (departmentIdStr != null && !departmentIdStr.trim().isEmpty() && !"0".equals(departmentIdStr)) {
            try {
                departmentId = Integer.parseInt(departmentIdStr);
            } catch (NumberFormatException e) {
                logger.warn("无效的部门ID参数: {}", departmentIdStr);
            }
        }

        List<Employee> employees;
        String logDescription;
        
        // 判断是否执行搜索
        if ((searchKeyword != null && !searchKeyword.trim().isEmpty()) || departmentId != null) {
            // 执行搜索
            employees = employeeService.searchEmployees(searchKeyword, departmentId);
            logDescription = String.format("用户搜索了专项扣除员工列表。关键词: %s, 部门ID: %s", 
                searchKeyword != null ? searchKeyword : "无", departmentId != null ? departmentId : "无");
        } else {
            // 显示所有员工
            employees = employeeService.getAllEmployees();
            logDescription = "用户进入专项附加扣除模块，查看员工选择列表。";
        }

        // *** 记录查看日志 ***
        AuditLogHelper.logSuccess(request, "查看专项扣除员工选择列表", "员工列表", logDescription);
        
        List<Employee> employeesToDisplay = new ArrayList<>();
        for (Employee emp : employees) {
            employeesToDisplay.add(employeeService.desensitizeEmployeeData(emp));
        }

        // 获取所有部门用于下拉框
        try {
            DepartmentService departmentService = new com.webb.service.impl.DepartmentServiceImpl();
            List<Department> departments = departmentService.getAllDepartments();
            request.setAttribute("departments", departments);
        } catch (Exception e) {
            logger.warn("获取部门列表失败: {}", e.getMessage());
        }

        request.setAttribute("employeesForSelection", employeesToDisplay);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedDepartmentId", departmentId);
        forwardToLayout(request, response, "/WEB-INF/jsp/deduction/employee_selection_for_deduction_content.jsp", "选择员工 - 专项扣除", "special_deductions_main");
    }

    private void listAnnualDeductions(HttpServletRequest request, HttpServletResponse response, int employeeId) throws ServletException, IOException, SpecialDeductionServiceException, EmployeeServiceException {
        // *** 记录查看日志 ***
        AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.VIEW_ANNUAL_DEDUCTIONS, "员工ID: " + employeeId, "查看该员工的年度专项附加扣除列表。");
        if (request.getAttribute("currentEmployee") == null && employeeId > 0) {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null)
                throw new SpecialDeductionServiceException("未找到员工ID: " + employeeId + " 无法列出年度扣除。");
            request.setAttribute("currentEmployee", employee);
        }
        List<EmployeeAnnualDeduction> deductions = specialDeductionService.getAnnualDeductionsByEmployeeId(employeeId);
        request.setAttribute("annualDeductions", deductions);
        forwardToLayout(request, response, "/WEB-INF/jsp/deduction/annual_deduction_list_content.jsp", "年度专项附加扣除", "special_deductions_annual");
    }

    private void showAnnualDeductionForm(HttpServletRequest request, HttpServletResponse response, int employeeId, Integer annualDeductionIdToEdit) throws ServletException, IOException, SpecialDeductionServiceException, EmployeeServiceException {
        if (request.getAttribute("currentEmployee") == null && employeeId > 0) {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null)
                throw new SpecialDeductionServiceException("未找到员工ID: " + employeeId + " 无法显示年度扣除表单。");
            request.setAttribute("currentEmployee", employee);
        }

        EmployeeAnnualDeduction deduction;
        String pageTitle;
        if (request.getAttribute("annualDeduction") != null) {
            deduction = (EmployeeAnnualDeduction) request.getAttribute("annualDeduction");
            if (deduction.getEmployeeId() == 0 && employeeId > 0)
                deduction.setEmployeeId(employeeId); // 确保employeeId被设置
            pageTitle = (deduction.getId() > 0) ? "编辑年度专项附加扣除 (重试)" : "添加年度专项附加扣除 (重试)";
        } else if (annualDeductionIdToEdit != null && annualDeductionIdToEdit > 0) {
            deduction = specialDeductionService.getAnnualDeductionById(annualDeductionIdToEdit);
            if (deduction == null || deduction.getEmployeeId() != employeeId) {
                throw new SpecialDeductionServiceException("未找到指定的年度扣除记录或记录不属于该员工。");
            }
            pageTitle = "编辑年度专项附加扣除";
        } else {
            deduction = new EmployeeAnnualDeduction();
            deduction.setEmployeeId(employeeId);
            pageTitle = "添加年度专项附加扣除";
        }
        request.setAttribute("annualDeduction", deduction);
        forwardToLayout(request, response, "/WEB-INF/jsp/deduction/annual_deduction_form_content.jsp", pageTitle, "special_deductions_annual");
    }

    private EmployeeAnnualDeduction populateAnnualDeductionFromRequest(HttpServletRequest request, int employeeId) {
        EmployeeAnnualDeduction deduction = new EmployeeAnnualDeduction();
        deduction.setEmployeeId(employeeId);

        String idStr = request.getParameter("annualDeductionId");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                deduction.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException e) { /* 新增时ID为空，忽略 */ }
        }

        try {
            String yearStr = request.getParameter("year");
            if (yearStr != null && !yearStr.isEmpty()) deduction.setYear(Integer.parseInt(yearStr));

            deduction.setChildrenEducationAmount(parseBigDecimal(request.getParameter("childrenEducationAmount"), request, "parsingError_childrenEducationAmount", "子女教育金额"));
            deduction.setContinuingEducationAmount(parseBigDecimal(request.getParameter("continuingEducationAmount"), request, "parsingError_continuingEducationAmount", "继续教育金额"));
            deduction.setSeriousIllnessMedicalAmount(parseBigDecimal(request.getParameter("seriousIllnessMedicalAmount"), request, "parsingError_seriousIllnessMedicalAmount", "大病医疗金额"));
            deduction.setHousingLoanInterestAmount(parseBigDecimal(request.getParameter("housingLoanInterestAmount"), request, "parsingError_housingLoanInterestAmount", "住房贷款利息金额"));
            deduction.setHousingRentAmount(parseBigDecimal(request.getParameter("housingRentAmount"), request, "parsingError_housingRentAmount", "住房租金金额"));
            deduction.setElderlyCareAmount(parseBigDecimal(request.getParameter("elderlyCareAmount"), request, "parsingError_elderlyCareAmount", "赡养老人金额"));
            deduction.setInfantCareAmount(parseBigDecimal(request.getParameter("infantCareAmount"), request, "parsingError_infantCareAmount", "婴幼儿照护金额"));
            deduction.setRemarks(request.getParameter("remarks"));

        } catch (NumberFormatException e) {
            logger.warn("解析年度扣除表单数据时，年份格式错误: {}", e.getMessage());
            request.setAttribute("parsingError", "填写的年份格式不正确，请检查。");
        }
        request.setAttribute("annualDeduction", deduction);
        return deduction;
    }

    private BigDecimal parseBigDecimal(String valueStr, HttpServletRequest request, String errorAttributeName, String fieldDisplayName) {
        if (valueStr != null && !valueStr.trim().isEmpty()) {
            try {
                return new BigDecimal(valueStr.trim());
            } catch (NumberFormatException e) {
                logger.warn("解析BigDecimal失败 for {}: '{}'", fieldDisplayName, valueStr, e);
                request.setAttribute(errorAttributeName, fieldDisplayName + "格式不正确。");
                request.setAttribute("parsingError", // 设置一个通用的解析错误标记
                        (request.getAttribute("parsingError") == null ? "" : request.getAttribute("parsingError") + " ") +
                                fieldDisplayName + "格式不正确。");
            }
        }
        return BigDecimal.ZERO;
    }

    private void saveAnnualDeduction(HttpServletRequest request, HttpServletResponse response, int employeeId) throws ServletException, IOException, SpecialDeductionServiceException, EmployeeServiceException {
        EmployeeAnnualDeduction deduction = populateAnnualDeductionFromRequest(request, employeeId);
        if (request.getAttribute("parsingError") != null) {
            request.setAttribute("errorMessage", (String) request.getAttribute("parsingError"));
            showAnnualDeductionForm(request, response, employeeId, deduction.getId());
            return;
        }
        String actionLogDetail = (deduction.getId() > 0 ? "更新" : "添加") + "员工ID " + employeeId + " 的 " + deduction.getYear() + " 年度专项附加扣除记录。";
        try {
            if (specialDeductionService.saveOrUpdateAnnualDeduction(deduction)) {
                //保存成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.SAVE_ANNUAL_DEDUCTION, "员工ID: " + employeeId + ", 年份: " + deduction.getYear(), actionLogDetail);
                request.getSession().setAttribute("successMessage", "年度专项附加扣除信息保存成功！");
                response.sendRedirect(request.getContextPath() + "/specialDeductions?action=list_annual_deductions&employeeId=" + employeeId);
            }
        } catch (SpecialDeductionServiceException e) {
            //保存失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.SAVE_ANNUAL_DEDUCTION, "员工ID: " + employeeId + ", 年份: " + deduction.getYear(), "保存失败: " + e.getMessage());
            throw e;
        }
    }

    private void deleteAnnualDeduction(HttpServletRequest request, HttpServletResponse response, int employeeId) throws ServletException, IOException, SpecialDeductionServiceException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            if (specialDeductionService.deleteAnnualDeduction(id)) {
                //删除成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.DELETE_ANNUAL_DEDUCTION, "记录ID: " + id, "成功删除员工 " + employeeId + " 的年度专项附加扣除记录。");
                request.getSession().setAttribute("successMessage", "年度专项附加扣除记录删除成功！");
            }
        } catch (SpecialDeductionServiceException e) {
            //删除失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.DELETE_ANNUAL_DEDUCTION, "记录ID: " + id, "删除失败: " + e.getMessage());
            throw e;
        }
        response.sendRedirect(request.getContextPath() + "/specialDeductions?action=list_annual_deductions&employeeId=" + employeeId);
    }

    //被抚养人 Helper 方法
    private void listDependents(HttpServletRequest request, HttpServletResponse response, int employeeId) throws ServletException, IOException, SpecialDeductionServiceException, EmployeeServiceException {
        // *** 记录查看日志 ***
        AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.VIEW_DEPENDENTS, "员工ID: " + employeeId, "查看该员工的被抚养人列表。");
        if (request.getAttribute("currentEmployee") == null && employeeId > 0) {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null)
                throw new SpecialDeductionServiceException("未找到员工ID: " + employeeId + " 无法列出被抚养人。");
            request.setAttribute("currentEmployee", employee);
        }
        List<EmployeeDependent> dependents = specialDeductionService.getDependentsByEmployeeId(employeeId);
        request.setAttribute("dependents", dependents);
        forwardToLayout(request, response, "/WEB-INF/jsp/deduction/dependent_list_content.jsp", "被抚养人列表", "special_deductions_dependent");
    }

    private void showDependentForm(HttpServletRequest request, HttpServletResponse response, int employeeId, Integer dependentIdToEdit) throws ServletException, IOException, SpecialDeductionServiceException, EmployeeServiceException {
        if (request.getAttribute("currentEmployee") == null && employeeId > 0) {
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null)
                throw new SpecialDeductionServiceException("未找到员工ID: " + employeeId + " 无法显示被抚养人表单。");
            request.setAttribute("currentEmployee", employee);
        }

        EmployeeDependent dependent;
        String pageTitle;
        if (request.getAttribute("dependent") != null) {
            dependent = (EmployeeDependent) request.getAttribute("dependent");
            if (dependent.getEmployeeId() == 0 && employeeId > 0) dependent.setEmployeeId(employeeId);
            pageTitle = (dependent.getId() > 0) ? "编辑被抚养人信息 (重试)" : "添加被抚养人信息 (重试)";
        } else if (dependentIdToEdit != null && dependentIdToEdit > 0) {
            dependent = specialDeductionService.getDependentById(dependentIdToEdit);
            if (dependent == null || dependent.getEmployeeId() != employeeId) {
                throw new SpecialDeductionServiceException("未找到指定的被抚养人记录或记录不属于该员工。");
            }
            pageTitle = "编辑被抚养人信息";
        } else {
            dependent = new EmployeeDependent();
            dependent.setEmployeeId(employeeId);
            pageTitle = "添加被抚养人信息";
        }
        request.setAttribute("dependent", dependent);

        List<EmployeeAnnualDeduction> annualDeductions = specialDeductionService.getAnnualDeductionsByEmployeeId(employeeId);
        request.setAttribute("availableAnnualDeductions", annualDeductions);

        forwardToLayout(request, response, "/WEB-INF/jsp/deduction/dependent_form_content.jsp", pageTitle, "special_deductions_dependent");
    }

    private EmployeeDependent populateDependentFromRequest(HttpServletRequest request, int employeeId) {
        EmployeeDependent dependent = new EmployeeDependent();
        dependent.setEmployeeId(employeeId);

        String idStr = request.getParameter("dependentId");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                dependent.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException e) { /* 新增时忽略 */ }
        }

        dependent.setDependentName(request.getParameter("dependentName"));
        dependent.setDependentIdCardNumber(request.getParameter("dependentIdCardNumber"));
        dependent.setRelationship(request.getParameter("relationship"));

        String[] deductionTypesArray = request.getParameterValues("deductionTypeInvolved");
        if (deductionTypesArray != null && deductionTypesArray.length > 0) {
            dependent.setDeductionTypeInvolved(String.join(",", deductionTypesArray));
        } else {
            dependent.setDeductionTypeInvolved(null);
        }

        String birthDateStr = request.getParameter("birthDate");
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            try {
                dependent.setBirthDate(Date.valueOf(birthDateStr));
            } catch (IllegalArgumentException e) {
                logger.warn("解析被抚养人出生日期失败: {}", birthDateStr);
                request.setAttribute("parsingError_dependent_birthDate", "出生日期格式不正确。");
            }
        }

        String annualDeductionIdStr = request.getParameter("annualDeductionId");
        if (annualDeductionIdStr != null && !annualDeductionIdStr.isEmpty() && !"0".equals(annualDeductionIdStr)) {
            try {
                dependent.setAnnualDeductionId(Integer.parseInt(annualDeductionIdStr));
            } catch (NumberFormatException e) {
                logger.warn("解析关联年度扣除ID失败: {}", annualDeductionIdStr);
            }
        } else {
            dependent.setAnnualDeductionId(null);
        }

        dependent.setNotes(request.getParameter("notes"));

        request.setAttribute("dependent", dependent);
        return dependent;
    }

    private void saveDependent(HttpServletRequest request, HttpServletResponse response, int employeeId) throws ServletException, IOException, SpecialDeductionServiceException, EmployeeServiceException {
        EmployeeDependent dependent = populateDependentFromRequest(request, employeeId);
        String actionLogDetail = (dependent.getId() > 0 ? "更新" : "添加") + "员工ID " + employeeId + " 的被抚养人: " + dependent.getDependentName();
        try {
            boolean success;
            if (dependent.getId() > 0) {
                success = specialDeductionService.updateDependent(dependent);
            } else {
                success = specialDeductionService.addDependent(dependent);
            }

            if (success) {
                //保存成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.SAVE_DEPENDENT, "员工ID: " + employeeId, actionLogDetail);
                request.getSession().setAttribute("successMessage", "被抚养人信息" + (dependent.getId() > 0 ? "更新" : "添加") + "成功！");
                response.sendRedirect(request.getContextPath() + "/specialDeductions?action=list_dependents&employeeId=" + employeeId);
            }
        } catch (SpecialDeductionServiceException e) {
            //保存失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.SAVE_DEPENDENT, "员工ID: " + employeeId, actionLogDetail + ", 失败原因: " + e.getMessage());
            throw e;
        }
    }

    private void deleteDependent(HttpServletRequest request, HttpServletResponse response, int employeeId) throws ServletException, IOException, SpecialDeductionServiceException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            if (specialDeductionService.deleteDependent(id)) {
                //删除成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.DELETE_DEPENDENT, "被抚养人记录ID: " + id, "成功删除员工 " + employeeId + " 的被抚养人记录。");
                request.getSession().setAttribute("successMessage", "被抚养人信息删除成功！");
            }
        } catch (SpecialDeductionServiceException e) {
            //删除失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.DELETE_DEPENDENT, "被抚养人记录ID: " + id, "删除失败: " + e.getMessage());
            throw e;
        }
        response.sendRedirect(request.getContextPath() + "/specialDeductions?action=list_dependents&employeeId=" + employeeId);
    }


    private Integer getIdFromRequest(HttpServletRequest request, String paramName) {
        String idStr = request.getParameter(paramName);
        if (idStr != null && !idStr.isEmpty()) {
            try {
                return Integer.parseInt(idStr);
            } catch (NumberFormatException e) { /* 忽略 */ }
        }
        return null;
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, String contentPagePath, String pageTitle, String activeNavItem) throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activeNavItem", activeNavItem);
        request.setAttribute("contentPage", contentPagePath);
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
}