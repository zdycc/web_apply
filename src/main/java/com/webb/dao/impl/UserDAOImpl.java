package com.webb.dao.impl;

import com.webb.dao.RoleDAO;
import com.webb.dao.UserDAO;
import com.webb.model.Role;
import com.webb.model.User;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    private RoleDAO roleDAO = new RoleDAOImpl();

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = findUsersBySql(sql, username);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = findUsersBySql(sql, id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY id ASC";
        return findUsersBySql(sql);
    }

    @Override
    public int countByRoleId(int roleId) throws SQLException {
        String sql = "SELECT COUNT(id) FROM users WHERE role_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("根据角色ID统计用户数失败, RoleID: {}", roleId, e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return count;
    }

    private List<User> findUsersBySql(String sql, Object... params) throws SQLException {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            logger.error("查询用户列表失败, SQL (部分): {}", sql.split(" WHERE")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return users;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRoleId(rs.getInt("role_id"));
        user.setEmployeeId(rs.getObject("employee_id", Integer.class));
        user.setLastPasswordChangeDate(rs.getTimestamp("last_password_change_date"));
        user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
        user.setLockoutUntil(rs.getTimestamp("lockout_until"));
        user.setLastLoginTime(rs.getTimestamp("last_login_time"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        if (user.getRoleId() > 0) {
            try {
                Role role = roleDAO.findById(user.getRoleId());
                user.setRole(role);
            } catch (SQLException e) {
                logger.warn("加载用户ID {} 的角色信息失败, RoleID: {}", user.getId(), user.getRoleId(), e);
            }
        }
        return user;
    }

    @Override
    public int addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, role_id, employee_id, is_active, last_password_change_date) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int userId = -1;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setInt(3, user.getRoleId());
            if (user.getEmployeeId() != null) {
                pstmt.setInt(4, user.getEmployeeId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setBoolean(5, user.getIsActive());
            pstmt.setTimestamp(6, user.getLastPasswordChangeDate() != null ? user.getLastPasswordChangeDate() : new Timestamp(System.currentTimeMillis()));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }
            }
            conn.commit();
            return userId;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加用户失败: {}", user.getUsername(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET role_id = ?, employee_id = ?, is_active = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getRoleId());
            if (user.getEmployeeId() != null) {
                pstmt.setInt(2, user.getEmployeeId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setBoolean(3, user.getIsActive());
            pstmt.setInt(4, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新用户信息失败, UserID: {}", user.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean resetUserPassword(int userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ?, last_password_change_date = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("重置用户密码失败, UserID: {}", userId, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean updateLoginAttempts(int userId, int attempts, Timestamp lockoutUntil) throws SQLException {
        String sql = "UPDATE users SET failed_login_attempts = ?, lockout_until = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attempts);
            pstmt.setTimestamp(2, lockoutUntil);
            pstmt.setInt(3, userId);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新用户登录尝试次数失败, UserID: {}", userId, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean resetLoginAttempts(int userId) throws SQLException {
        return updateLoginAttempts(userId, 0, null);
    }

    @Override
    public boolean updateLastPasswordChangeDate(int userId, Timestamp changeDate) throws SQLException {
        String sql = "UPDATE users SET last_password_change_date = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, changeDate);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新用户最后密码修改日期失败, UserID: {}", userId, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
    }

    @Override
    public boolean updateLastLoginTime(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login_time = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("更新用户最后登录时间失败, UserID: {}", userId, e);
            throw e;
        } finally {
            DBUtils.close(pstmt, conn);
        }
    }
}