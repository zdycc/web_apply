package com.webb.dao.impl;

import com.webb.dao.RoleDAO;
import com.webb.model.Role;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {
    private static final Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);

    @Override
    public Role findById(int id) throws SQLException {
        String sql = "SELECT id, role_name, description FROM roles WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Role role = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法连接到数据库");
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            logger.error("根据ID查询角色失败, ID: {}", id, e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return role;
    }

    @Override
    public List<Role> findAll() throws SQLException {
        String sql = "SELECT id, role_name, description FROM roles ORDER BY id ASC";
        List<Role> roles = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
                roles.add(role);
            }
        } catch (SQLException e) {
            logger.error("查询所有角色失败", e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return roles;
    }

    @Override
    public int addRole(Role role) throws SQLException {
        String sql = "INSERT INTO roles (role_name, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int roleId = -1;
        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, role.getRoleName());
            pstmt.setString(2, role.getDescription());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    roleId = generatedKeys.getInt(1);
                }
            }
            conn.commit();
            return roleId;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加角色失败: {}", role.getRoleName(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean updateRole(Role role) throws SQLException {
        String sql = "UPDATE roles SET role_name = ?, description = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role.getRoleName());
            pstmt.setString(2, role.getDescription());
            pstmt.setInt(3, role.getId());
            boolean success = pstmt.executeUpdate() > 0;
            conn.commit();
            return success;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新角色失败: ID={}", role.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean deleteRole(int roleId) throws SQLException {
        String sql = "DELETE FROM roles WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            boolean success = pstmt.executeUpdate() > 0;
            conn.commit();
            return success;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("删除角色失败: ID={}", roleId, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }
}