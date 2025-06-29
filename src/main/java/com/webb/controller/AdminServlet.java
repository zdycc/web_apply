package com.webb.controller;

import com.webb.model.Employee;
import com.webb.model.Role;
import com.webb.model.User;
import com.webb.service.*;
import com.webb.service.impl.EmployeeServiceImpl;
import com.webb.service.impl.RoleServiceImpl;
import com.webb.service.impl.UserServiceImpl;
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
import java.util.stream.Collectors;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AdminServlet.class);

    private UserService userService = new UserServiceImpl();
    private RoleService roleService = new RoleServiceImpl();
    private EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list_users"; // 默认动作
        }

        try {
            // TODO: 在此添加权限过滤器，确保只有系统管理员能访问

            switch (action) {
                // 用户管理
                case "list_users":
                    listUsers(request, response);
                    break;
                case "add_user_form":
                    showUserForm(request, response, null);
                    break;
                case "edit_user_form":
                    int userId = Integer.parseInt(request.getParameter("id"));
                    showUserForm(request, response, userId);
                    break;
                case "unlock_user":
                    unlockUser(request, response);
                    break;

                // 角色管理
                case "list_roles":
                    listRoles(request, response);
                    break;
                case "add_role_form":
                    showRoleForm(request, response, null);
                    break;
                case "edit_role_form":
                    int roleId = Integer.parseInt(request.getParameter("id"));
                    showRoleForm(request, response, roleId);
                    break;
                case "delete_role":
                    deleteRole(request, response);
                    break;

                default:
                    listUsers(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            handleError(request, response, "无效的ID参数。");
        } catch (UserServiceException | RoleServiceException | EmployeeServiceException e) {
            logger.error("管理员操作失败: action={}, error={}", action, e.getMessage(), e);
            handleError(request, response, "操作失败：" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "未指定操作。");
            return;
        }

        try {
            // TODO: 添加权限过滤器

            switch(action) {
                // 用户管理
                case "add_user_submit":
                    addUser(request, response);
                    break;
                case "edit_user_submit":
                    updateUser(request, response);
                    break;
                case "reset_password":
                    resetPassword(request, response);
                    break;

                // 角色管理
                case "save_role":
                    saveRole(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的操作请求。");
                    break;
            }
        } catch (UserServiceException | RoleServiceException e) {
            logger.error("管理员操作失败: action={}, error={}", action, e.getMessage(), e);
            request.setAttribute("errorMessage", "保存失败: " + e.getMessage());
            // 根据action回显对应的表单
            try {
                if ("save_role".equals(action)) {
                    Integer roleId = (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) ? Integer.parseInt(request.getParameter("id")) : null;
                    showRoleForm(request, response, roleId);
                } else { // 默认为用户管理错误回显
                    String userIdStr = request.getParameter("id");
                    Integer userId = (userIdStr != null && !userIdStr.isEmpty()) ? Integer.parseInt(userIdStr) : null;
                    showUserForm(request, response, userId);
                }
            } catch (Exception exFallback) {
                logger.error("在处理POST操作的异常时，回显表单也失败了: {}", exFallback.getMessage(), exFallback);
                handleError(request, response, "系统错误，无法回显表单。");
            }
        }
    }

    // --- 用户管理 Helper 方法 ---
    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UserServiceException {
        AuditLogHelper.logSuccess(request, "查看用户列表", "所有系统用户", "系统管理员查看了用户列表。");
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        forwardToLayout(request, response, "用户管理", "/WEB-INF/jsp/admin/user_list_content.jsp", "users");
    }

    private void showUserForm(HttpServletRequest request, HttpServletResponse response, Integer userId) throws ServletException, IOException, UserServiceException, RoleServiceException, EmployeeServiceException {
        String pageTitle;
        User user;
        if (userId != null && userId > 0) {
            pageTitle = "编辑用户";
            user = userService.getUserById(userId);
            if (user == null) throw new UserServiceException("未找到ID为 " + userId + " 的用户。");
        } else {
            pageTitle = "添加新用户";
            user = new User();
        }

        List<Role> roles = roleService.getAllRoles();
        List<Employee> allEmployees = employeeService.getAllEmployees();
        List<User> allUsers = userService.getAllUsers();

        List<Integer> assignedEmployeeIds = allUsers.stream()
                .filter(u -> u.getEmployeeId() != null)
                .map(User::getEmployeeId)
                .collect(Collectors.toList());

        List<Employee> unassignedEmployees = allEmployees.stream()
                .filter(e -> !assignedEmployeeIds.contains(e.getId()))
                .collect(Collectors.toList());

        if (user.getId() > 0 && user.getEmployeeId() != null) {
            Employee currentEmployee = employeeService.getEmployeeById(user.getEmployeeId());
            if (currentEmployee != null) {
                unassignedEmployees.add(0, currentEmployee);
            }
        }

        request.setAttribute("user", user);
        request.setAttribute("roles", roles);
        request.setAttribute("employees", unassignedEmployees);

        forwardToLayout(request, response, pageTitle, "/WEB-INF/jsp/admin/user_form_content.jsp", "users");
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException, UserServiceException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setRoleId(Integer.parseInt(request.getParameter("roleId")));
        user.setActive("on".equals(request.getParameter("isActive")));

        String employeeIdStr = request.getParameter("employeeId");
        if (employeeIdStr != null && !employeeIdStr.isEmpty() && !"0".equals(employeeIdStr)) {
            user.setEmployeeId(Integer.parseInt(employeeIdStr));
        }

        String rawPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!rawPassword.equals(confirmPassword)) {
            throw new UserServiceException("两次输入的密码不一致。");
        }

        userService.addNewUser(user, rawPassword);

        AuditLogHelper.logSuccess(request, "添加用户", "新用户: " + user.getUsername(), "成功添加系统用户。");
        request.getSession().setAttribute("successMessage", "用户 " + user.getUsername() + " 添加成功！");
        response.sendRedirect(request.getContextPath() + "/admin?action=list_users");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException, UserServiceException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserServiceException("尝试更新一个不存在的用户。");
        }

        user.setRoleId(Integer.parseInt(request.getParameter("roleId")));
        user.setActive("on".equals(request.getParameter("isActive")));

        String employeeIdStr = request.getParameter("employeeId");
        if (employeeIdStr != null && !employeeIdStr.isEmpty() && !"0".equals(employeeIdStr)) {
            user.setEmployeeId(Integer.parseInt(employeeIdStr));
        } else {
            user.setEmployeeId(null);
        }

        userService.updateUser(user);

        AuditLogHelper.logSuccess(request, "更新用户", "用户ID: " + userId, "成功更新用户信息。");
        request.getSession().setAttribute("successMessage", "用户 " + user.getUsername() + " 更新成功！");
        response.sendRedirect(request.getContextPath() + "/admin?action=list_users");
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException, UserServiceException {
        int userId = Integer.parseInt(request.getParameter("id"));
        String rawPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!rawPassword.equals(confirmPassword)) {
            throw new UserServiceException("两次输入的密码不一致。");
        }

        userService.resetPassword(userId, rawPassword);

        AuditLogHelper.logSuccess(request, "重置密码", "用户ID: " + userId, "成功重置用户密码。");
        request.getSession().setAttribute("successMessage", "用户ID " + userId + " 的密码已成功重置！");
        response.sendRedirect(request.getContextPath() + "/admin?action=edit_user_form&id=" + userId);
    }

    // --- 角色管理 Helper 方法 ---

    private void listRoles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, RoleServiceException {
        AuditLogHelper.logSuccess(request, "查看角色列表", "所有角色", "系统管理员查看了角色列表。");
        List<Role> roles = roleService.getAllRoles();
        request.setAttribute("roles", roles);
        forwardToLayout(request, response, "角色管理", "/WEB-INF/jsp/admin/role_list_content.jsp", "roles");
    }

    private void showRoleForm(HttpServletRequest request, HttpServletResponse response, Integer roleId) throws ServletException, IOException, RoleServiceException {
        String pageTitle;
        Role role = new Role();
        if (request.getAttribute("role") != null) { // 用于错误回显
            role = (Role)request.getAttribute("role");
        } else if (roleId != null && roleId > 0) { // 编辑
            role = roleService.getRoleById(roleId);
            if (role == null) throw new RoleServiceException("未找到ID为 " + roleId + " 的角色。");
        }

        pageTitle = (role.getId() > 0) ? "编辑角色" : "添加新角色";

        request.setAttribute("role", role);
        forwardToLayout(request, response, pageTitle, "/WEB-INF/jsp/admin/role_form_content.jsp", "roles");
    }

    private void saveRole(HttpServletRequest request, HttpServletResponse response) throws IOException, RoleServiceException {
        String idStr = request.getParameter("id");
        String roleName = request.getParameter("roleName");
        String description = request.getParameter("description");

        Role role = new Role();
        role.setRoleName(roleName);
        role.setDescription(description);

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            role.setId(id);
            roleService.updateRole(role);
            AuditLogHelper.logSuccess(request, "更新角色", "角色ID: " + id, "成功更新角色: " + roleName);
            request.getSession().setAttribute("successMessage", "角色 '" + roleName + "' 更新成功！");
        } else { // 新增
            roleService.addRole(role);
            AuditLogHelper.logSuccess(request, "添加角色", "新角色: " + roleName, "成功添加新角色。");
            request.getSession().setAttribute("successMessage", "角色 '" + roleName + "' 添加成功！");
        }
        response.sendRedirect(request.getContextPath() + "/admin?action=list_roles");
    }

    private void deleteRole(HttpServletRequest request, HttpServletResponse response) throws IOException, RoleServiceException {
        int roleId = Integer.parseInt(request.getParameter("id"));

        Role roleToDelete = roleService.getRoleById(roleId);
        String roleName = (roleToDelete != null) ? roleToDelete.getRoleName() : "[未知角色]";

        try {
            roleService.deleteRole(roleId);
            AuditLogHelper.logSuccess(request, "删除角色", "角色ID: " + roleId, "成功删除角色: " + roleName);
            request.getSession().setAttribute("successMessage", "角色 '" + roleName + "' 删除成功！");
        } catch (RoleServiceException e) {
            AuditLogHelper.logFailure(request, "删除角色", "角色ID: " + roleId, "删除角色失败: " + e.getMessage());
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin?action=list_roles");
    }

    // --- 通用 Helper 方法 ---
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        forwardToLayout(request, response, "错误", "/WEB-INF/jsp/error_content.jsp", "error");
    }

    private void forwardToLayout(HttpServletRequest request, HttpServletResponse response, String pageTitle, String contentPage, String activeNavItem) throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activeNavItem", activeNavItem);
        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/WEB-INF/jsp/layout.jsp").forward(request, response);
    }

    private void unlockUser(HttpServletRequest request, HttpServletResponse response) throws IOException, UserServiceException {
        int userId = Integer.parseInt(request.getParameter("id"));
        try {
            if (userService.unlockUser(userId)) {
                User user = userService.getUserById(userId);
                AuditLogHelper.logSuccess(request, "解锁用户", "用户: " + (user != null ? user.getUsername() : "ID " + userId), "成功解锁用户账户。");
                request.getSession().setAttribute("successMessage", "用户 (ID: " + userId + ") 已成功解锁！");
            } else {
                throw new UserServiceException("解锁操作在数据库层面未成功执行。");
            }
        } catch (UserServiceException e) {
            AuditLogHelper.logFailure(request, "解锁用户", "用户ID: " + userId, "解锁用户失败: " + e.getMessage());
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin?action=list_users");
    }
}