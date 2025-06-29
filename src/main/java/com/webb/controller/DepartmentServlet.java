package com.webb.controller;

import com.webb.model.Department;
import com.webb.service.DepartmentService;
import com.webb.service.DepartmentServiceException;
import com.webb.service.impl.DepartmentServiceImpl;
import com.webb.util.AuditLogHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/departments")
public class DepartmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServlet.class);
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
                    showNewDepartmentForm(request, response);
                    break;
                case "edit_form":
                    showEditDepartmentForm(request, response);
                    break;
                case "delete":
                    deleteDepartment(request, response);
                    break;
                case "list":
                default:
                    listDepartments(request, response);
                    break;
            }
        } catch (DepartmentServiceException e) {
            logger.error("部门管理(GET)操作失败: action={}, error={}", action, e.getMessage(), e);
            listDepartmentsWithError(request, response, "操作失败：" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add_submit":
                    addDepartment(request, response);
                    break;
                case "edit_submit":
                    updateDepartment(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的操作");
                    break;
            }
        } catch (DepartmentServiceException e) {
            logger.error("部门管理(POST)操作失败: action={}, error={}", action, e.getMessage(), e);
            request.setAttribute("errorMessage", "保存失败：" + e.getMessage());
            if ("add_submit".equals(action)) {
                try {
                    showNewDepartmentForm(request, response);
                } catch (DepartmentServiceException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("edit_submit".equals(action)) {
                try {
                    showEditDepartmentForm(request, response);
                } catch (DepartmentServiceException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                listDepartmentsWithError(request, response, "操作失败：" + e.getMessage());
            }
        }
    }

    private void listDepartments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        //记录查看日志
        AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.VIEW_DEPARTMENTS, "所有部门", "用户查看了部门列表。");

        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        request.setAttribute("pageTitle", "部门列表");
        request.setAttribute("activeNavItem", "departments");
        request.setAttribute("contentPage", "/WEB-INF/jsp/department/department_list_content.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }

    private void listDepartmentsWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        try {
            listDepartments(request, response);
        } catch (DepartmentServiceException e) {
            logger.warn("获取部门列表(带错误信息时)也失败了: {}", e.getMessage());
            request.setAttribute("departments", List.of());
            request.setAttribute("pageTitle", "部门列表");
            request.setAttribute("activeNavItem", "departments");
            request.setAttribute("contentPage", "/WEB-INF/jsp/department/department_list_content.jsp");
            request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
        }
    }

    private void showNewDepartmentForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        List<Department> allDepartments = departmentService.getAllDepartments();
        request.setAttribute("allDepartments", allDepartments);
        request.setAttribute("department", new Department());
        request.setAttribute("formAction", "add_submit");
        request.setAttribute("pageTitle", "添加新部门");
        request.setAttribute("activeNavItem", "departments");
        request.setAttribute("contentPage", "/WEB-INF/jsp/department/department_form_content.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }

    private void showEditDepartmentForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        int id = Integer.parseInt(request.getParameter("id"));
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            request.setAttribute("errorMessage", "未找到要编辑的部门。");
            listDepartments(request, response);
            return;
        }
        List<Department> allDepartments = departmentService.getAllDepartments();
        request.setAttribute("department", department);
        request.setAttribute("allDepartments", allDepartments);
        request.setAttribute("formAction", "edit_submit");
        request.setAttribute("pageTitle", "编辑部门");
        request.setAttribute("activeNavItem", "departments");
        request.setAttribute("contentPage", "/WEB-INF/jsp/department/department_form_content.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }

    private void addDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        String deptName = request.getParameter("deptName");
        String parentDeptIdStr = request.getParameter("parentDeptId");

        Department department = new Department();
        department.setDeptName(deptName);
        if (parentDeptIdStr != null && !parentDeptIdStr.isEmpty() && !"0".equals(parentDeptIdStr)) {
            department.setParentDeptId(Integer.parseInt(parentDeptIdStr));
        } else {
            department.setParentDeptId(null);
        }

        try {
            if (departmentService.addDepartment(department)) {
                //添加成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.ADD_DEPARTMENT, "部门名称: " + deptName, "成功添加新部门。");
                request.getSession().setAttribute("successMessage", "部门 \"" + deptName + "\" 添加成功！");
                response.sendRedirect(request.getContextPath() + "/departments?action=list");
            }
        } catch (DepartmentServiceException e) {
            //添加失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.ADD_DEPARTMENT, "部门名称: " + deptName, "添加部门失败: " + e.getMessage());
            throw e; // 重新抛出，让外层catch处理页面跳转
        }
    }

    private void updateDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        int id = Integer.parseInt(request.getParameter("id"));
        String deptName = request.getParameter("deptName");
        String parentDeptIdStr = request.getParameter("parentDeptId");

        Department department = new Department();
        department.setId(id);
        department.setDeptName(deptName);
        if (parentDeptIdStr != null && !parentDeptIdStr.isEmpty() && !"0".equals(parentDeptIdStr)) {
            department.setParentDeptId(Integer.parseInt(parentDeptIdStr));
        } else {
            department.setParentDeptId(null);
        }

        try {
            if (departmentService.updateDepartment(department)) {
                //更新成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.UPDATE_DEPARTMENT, "部门ID: " + id, "成功更新部门: " + deptName);
                request.getSession().setAttribute("successMessage", "部门 \"" + deptName + "\" 更新成功！");
                response.sendRedirect(request.getContextPath() + "/departments?action=list");
            }
        } catch (DepartmentServiceException e) {
            //更新失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.UPDATE_DEPARTMENT, "部门ID: " + id, "更新部门失败: " + e.getMessage());
            throw e;
        }
    }

    private void deleteDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DepartmentServiceException {
        int id = Integer.parseInt(request.getParameter("id"));
        // 先获取部门名称用于日志记录
        Department deptToDelete = null;
        try {
            deptToDelete = departmentService.getDepartmentById(id);
        } catch (DepartmentServiceException e) {
            logger.warn("删除部门前获取部门信息失败, ID: {}", id);
        }
        String deptName = (deptToDelete != null) ? deptToDelete.getDeptName() : "[未知名称]";

        try {
            if (departmentService.deleteDepartment(id)) {
                //删除成功日志
                AuditLogHelper.logSuccess(request, AuditLogHelper.ActionType.DELETE_DEPARTMENT, "部门ID: " + id, "成功删除部门: " + deptName);
                request.getSession().setAttribute("successMessage", "部门 \"" + deptName + "\" 删除成功！");
            }
        } catch (DepartmentServiceException e) {
            //删除失败日志
            AuditLogHelper.logFailure(request, AuditLogHelper.ActionType.DELETE_DEPARTMENT, "部门ID: " + id, "删除部门失败: " + e.getMessage());
            throw e;
        }
        response.sendRedirect(request.getContextPath() + "/departments?action=list");
    }
}