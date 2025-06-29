package com.webb.service.impl;

import com.webb.dao.RoleDAO;
import com.webb.dao.UserDAO;
import com.webb.dao.impl.RoleDAOImpl;
import com.webb.dao.impl.UserDAOImpl;
import com.webb.model.Role;
import com.webb.service.RoleService;
import com.webb.service.RoleServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private RoleDAO roleDAO = new RoleDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public List<Role> getAllRoles() throws RoleServiceException {
        try {
            return roleDAO.findAll();
        } catch (SQLException e) {
            throw new RoleServiceException("获取角色列表失败。", e);
        }
    }

    @Override
    public Role getRoleById(int roleId) throws RoleServiceException {
        try {
            return roleDAO.findById(roleId);
        } catch (SQLException e) {
            throw new RoleServiceException("获取角色信息失败。", e);
        }
    }

    @Override
    public Role addRole(Role role) throws RoleServiceException {
        if (role == null || role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            throw new RoleServiceException("角色名称不能为空。");
        }
        try {
            int newId = roleDAO.addRole(role);
            if (newId > 0) {
                role.setId(newId);
                return role;
            } else {
                throw new RoleServiceException("添加角色失败。");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw new RoleServiceException("角色名称 '" + role.getRoleName() + "' 已存在。", e);
            }
            throw new RoleServiceException("添加角色失败。", e);
        }
    }

    @Override
    public Role updateRole(Role role) throws RoleServiceException {
        if (role == null || role.getId() <= 0 || role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            throw new RoleServiceException("角色ID或名称无效。");
        }
        try {
            boolean success = roleDAO.updateRole(role);
            if (!success) {
                throw new RoleServiceException("更新角色失败，可能角色不存在。");
            }
            return role;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                throw new RoleServiceException("角色名称 '" + role.getRoleName() + "' 已被其他角色使用。", e);
            }
            throw new RoleServiceException("更新角色失败。", e);
        }
    }

    @Override
    public void deleteRole(int roleId) throws RoleServiceException {
        try {
            int userCount = userDAO.countByRoleId(roleId);
            if (userCount > 0) {
                throw new RoleServiceException("无法删除该角色，因为已有 " + userCount + " 个用户属于此角色。请先将这些用户的角色更改。");
            }
            boolean success = roleDAO.deleteRole(roleId);
            if (!success) {
                throw new RoleServiceException("删除角色失败，可能角色不存在。");
            }
        } catch (SQLException e) {
            throw new RoleServiceException("删除角色失败。", e);
        }
    }
}