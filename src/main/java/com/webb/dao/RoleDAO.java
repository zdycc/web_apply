package com.webb.dao;

import com.webb.model.Role;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {
    /**
     * 根据角色ID查询角色信息
     * @param id 角色ID
     * @return Role 对象，如果未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    Role findById(int id) throws SQLException;

    /**
     * 查询所有角色信息
     * @return 角色列表
     * @throws SQLException SQL执行异常
     */
    List<Role> findAll() throws SQLException;

    /**
     * 添加新角色
     * @param role 角色对象
     * @return 返回生成的角色ID
     * @throws SQLException SQL执行异常
     */
    int addRole(Role role) throws SQLException;

    /**
     * 更新角色信息
     * @param role 角色对象
     * @return 更新成功返回 true
     * @throws SQLException SQL执行异常
     */
    boolean updateRole(Role role) throws SQLException;

    /**
     * 根据ID删除角色
     * @param roleId 角色ID
     * @return 删除成功返回 true
     * @throws SQLException SQL执行异常
     */
    boolean deleteRole(int roleId) throws SQLException;
}