package com.webb.controller;

import com.webb.model.Department;
import com.webb.model.Employee;
import com.webb.service.DepartmentService;
import com.webb.service.DepartmentServiceException;
import com.webb.service.EmployeeService;
import com.webb.service.EmployeeServiceException;
import com.webb.service.impl.DepartmentServiceImpl;
import com.webb.service.impl.EmployeeServiceImpl;
import com.webb.util.AuditLogHelper; // 确保导入
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServlet.class);
    private EmployeeService employeeService = new EmployeeServiceImpl();
    private DepartmentService departmentService = new DepartmentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add_form":
                    showNewEmployeeForm(request, response);
                    break;
                case "edit_form":
                    showEditEmployeeForm(request, response);
                    break;
                case "delete":
                    deleteEmployee(request, response);
                    break;
                case "list":
                default:
                    listEmployees(request, response);
                    break;
            }
        } catch (EmployeeServiceException | DepartmentServiceException e) {
            logger.error("人员管理(GET)操作失败: action={}, error={}", action, e.getMessage(), e);
            // 失败日志在具体的action方法中记录，这里只负责页面跳转
            listEmployeesWithError(request, response, "操作失败：" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add_submit":
                    addEmployee(request, response);
                    break;
                case "edit_submit":
                    updateEmployee(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的操作");
                    break;
            }
        } catch (EmployeeServiceException | DepartmentServiceException e) {
            logger.error("人员管理(POST)操作失败: action={}, error={}", action, e.getMessage(), e);
            // 失败日志已在 add/update 方法中记录，这里只负责页面跳转
            request.setAttribute("errorMessage", "保存失败：" + e.getMessage());
            if ("add_submit".equals(action)) {
                try {
                    showNewEmployeeForm(request, response);
                } catch (DepartmentServiceException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("edit_submit".equals(action)) {
                try {
                    Employee employeeToEdit = employeeService.getEmployeeById(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("employee", employeeToEdit);
                } catch (Exception ex) {
                    logger.error("在处理更新员工错误时，重新加载员工信息失败: {}", ex.getMessage());
                }
                try {
                    showEditEmployeeForm(request, response);
                } catch (EmployeeServiceException ex) {
                    throw new RuntimeException(ex);
                } catch (DepartmentServiceException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                listEmployeesWithError(request, response, "操作失败：" + e.getMessage());
            }
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmployeeServiceException {
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
            logDescription = String.format("用户搜索了员工列表。关键词: %s, 部门ID: %s", 
                searchKeyword != null ? searchKeyword : "无", departmentId != null ? departmentId : "无");
        } else {
            // 显示所有员工
            employees = employeeService.getAllEmployees();
            logDescription = "用户查看了员工列表。";
        }

        //记录查看日志
        AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.VIEW_EMPLOYEES, "员工列表", logDescription);

        List<Employee> employeesToDisplay = new ArrayList<>();
        for (Employee emp : employees) {
            employeesToDisplay.add(employeeService.desensitizeEmployeeData(emp));
        }

        // 获取所有部门用于下拉框
        try {
            List<Department> departments = departmentService.getAllDepartments();
            request.setAttribute("departments", departments);
        } catch (DepartmentServiceException e) {
            logger.warn("获取部门列表失败: {}", e.getMessage());
        }

        request.setAttribute("employees", employeesToDisplay);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedDepartmentId", departmentId);
        request.setAttribute("pageTitle", "人员列表");
        request.setAttribute("activeNavItem", "personnel");
        request.setAttribute("contentPage", "/WEB-INF/jsp/employee/employee_list_content.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmployeeServiceException, DepartmentServiceException {
        Employee employee = populateEmployeeFromRequest(request);
        try {
            if (employeeService.addEmployee(employee)) {
                //添加成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.ADD_EMPLOYEE, "员工编号: " + employee.getEmployeeNumber(), "成功添加新员工: " + employee.getName());
                request.getSession().setAttribute("successMessage", "员工 \"" + employee.getName() + "\" 添加成功！");
                response.sendRedirect(request.getContextPath() + "/employees?action=list");
            } else {
                AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.ADD_EMPLOYEE, "员工编号: " + employee.getEmployeeNumber(), "添加员工失败，但未抛出异常。");
                request.setAttribute("errorMessage", "员工 \"" + employee.getName() + "\" 添加失败！");
                showNewEmployeeForm(request, response);
            }
        } catch (EmployeeServiceException e) {
            //添加失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.ADD_EMPLOYEE, "员工编号: " + employee.getEmployeeNumber(), "添加员工失败: " + e.getMessage());
            throw e; // 重新抛出，让外层catch处理页面跳转
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmployeeServiceException, DepartmentServiceException {
        Employee employee = populateEmployeeFromRequest(request);
        try {
            if (employeeService.updateEmployee(employee)) {
                //更新成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.UPDATE_EMPLOYEE, "员工ID: " + employee.getId(), "成功更新员工: " + employee.getName());
                request.getSession().setAttribute("successMessage", "员工 \"" + employee.getName() + "\" 更新成功！");
                response.sendRedirect(request.getContextPath() + "/employees?action=list");
            } else {
                AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.UPDATE_EMPLOYEE, "员工ID: " + employee.getId(), "更新员工失败，但未抛出异常。");
                request.setAttribute("errorMessage", "员工 \"" + employee.getName() + "\" 更新失败！");
                showEditEmployeeForm(request, response);
            }
        } catch (EmployeeServiceException e) {
            //更新失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.UPDATE_EMPLOYEE, "员工ID: " + employee.getId(), "更新员工失败: " + e.getMessage());
            throw e;
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmployeeServiceException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee empToDelete = employeeService.getEmployeeById(id);
        String empName = (empToDelete != null && empToDelete.getName() != null) ? empToDelete.getName() : "ID: " + id;

        try {
            if (employeeService.deleteEmployee(id)) {
                //删除成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.DELETE_EMPLOYEE, "员工ID: " + id, "成功删除员工: " + empName);
                request.getSession().setAttribute("successMessage", "员工 \"" + empName + "\" 删除成功！");
            } else {
                //删除失败日志
                AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.DELETE_EMPLOYEE, "员工ID: " + id, "删除员工失败，但未抛出异常。");
                request.getSession().setAttribute("errorMessage", "员工 \"" + empName + "\" 删除失败！");
            }
        } catch (EmployeeServiceException e) {
            //删除失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.DELETE_EMPLOYEE, "员工ID: " + id, "删除员工失败: " + e.getMessage());
            throw e; // 重新抛出
        }
        response.sendRedirect(request.getContextPath() + "/employees?action=list");
    }

    private void showNewEmployeeForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        if (request.getAttribute("employee") == null) {
            request.setAttribute("employee", new Employee());
        }
        request.setAttribute("formAction", "add_submit");
        request.setAttribute("pageTitle", "添加新员工");
        request.setAttribute("activeNavItem", "personnel");
        request.setAttribute("contentPage", "/WEB-INF/jsp/employee/employee_form_content.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
    private void showEditEmployeeForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmployeeServiceException, DepartmentServiceException {
        if (request.getAttribute("employee") == null) {
            int id = Integer.parseInt(request.getParameter("id"));
            Employee employee = employeeService.getEmployeeById(id);
            if (employee == null) {
                listEmployeesWithError(request, response, "未找到要编辑的员工 (ID: " + id + ")。");
                return;
            }
            request.setAttribute("employee", employee);
        }
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        request.setAttribute("formAction", "edit_submit");
        request.setAttribute("pageTitle", "编辑员工信息");
        request.setAttribute("activeNavItem", "personnel");
        request.setAttribute("contentPage", "/WEB-INF/jsp/employee/employee_form_content.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }
    private Employee populateEmployeeFromRequest(HttpServletRequest request) {
        Employee employee = new Employee();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                employee.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException e) { logger.warn("填充员工对象时ID格式无效: {}", idStr); }
        }
        employee.setEmployeeNumber(request.getParameter("employeeNumber"));
        employee.setName(request.getParameter("name"));
        employee.setIdCardNumber(request.getParameter("idCardNumber"));
        employee.setPhoneNumber(request.getParameter("phoneNumber"));
        employee.setAddress(request.getParameter("address"));
        String deptIdStr = request.getParameter("departmentId");
        if (deptIdStr != null && !deptIdStr.isEmpty()) {
            try {
                employee.setDepartmentId(Integer.parseInt(deptIdStr));
            } catch (NumberFormatException e) { logger.warn("填充员工对象时部门ID格式无效: {}", deptIdStr); }
        }
        employee.setPosition(request.getParameter("position"));
        employee.setJobTitle(request.getParameter("jobTitle"));
        employee.setStatus(request.getParameter("status"));
        String hireDateStr = request.getParameter("hireDate");
        if (hireDateStr != null && !hireDateStr.isEmpty()) {
            try {
                employee.setHireDate(Date.valueOf(hireDateStr));
            } catch (IllegalArgumentException e) { logger.warn("填充员工对象时入职日期格式无效: {}", hireDateStr); }
        }
        request.setAttribute("employee", employee); // 确保回显时能获取
        return employee;
    }
    private void listEmployeesWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        try {
            listEmployees(request, response);
        } catch (EmployeeServiceException e) {
            logger.warn("获取员工列表(带错误信息时)也失败了: {}", e.getMessage());
            request.setAttribute("employees", List.of());
            request.setAttribute("pageTitle", "人员列表");
            request.setAttribute("activeNavItem", "personnel");
            request.setAttribute("contentPage", "/WEB-INF/jsp/employee/employee_list_content.jsp");
            request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
        }
    }
}