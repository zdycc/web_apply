package com.webb.dao.impl;

import com.webb.dao.AuditLogDAO;
import com.webb.model.AuditLog;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditLogDAOImpl implements AuditLogDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogDAOImpl.class);

    @Override
    public void addLog(AuditLog log) throws SQLException {
        String sql = "INSERT INTO audit_logs (user_id, username, ip_address, action_type, target_resource, details, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            // 日志记录通常不参与业务事务，可以单独提交
            pstmt = conn.prepareStatement(sql);
            if (log.getUserId() != null) {
                pstmt.setInt(1, log.getUserId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, log.getUsername());
            pstmt.setString(3, log.getIpAddress());
            pstmt.setString(4, log.getActionType());
            pstmt.setString(5, log.getTargetResource());
            pstmt.setString(6, log.getDetails());
            pstmt.setString(7, log.getStatus());

            pstmt.executeUpdate();
            logger.debug("审计日志已记录: User='{}', Action='{}'", log.getUsername(), log.getActionType());
        } catch (SQLException e) {
            logger.error("记录审计日志失败", e);
            // 通常不向上抛出异常，因为日志记录的失败不应中断主业务流程
            // 但对于关键审计，也可以选择抛出
        } finally {
            DBUtils.close(pstmt, conn);
        }
    }

    private String buildFilterSql(StringBuilder sqlBuilder, Map<String, Object> filters, List<Object> params) {
        if (filters != null) {
            String username = (String) filters.get("username");
            if (username != null && !username.isEmpty()) {
                sqlBuilder.append(" AND username LIKE ?");
                params.add("%" + username + "%");
            }
            String actionType = (String) filters.get("actionType");
            if (actionType != null && !actionType.isEmpty()) {
                sqlBuilder.append(" AND action_type = ?");
                params.add(actionType);
            }
            String startDate = (String) filters.get("startDate");
            if (startDate != null && !startDate.isEmpty()) {
                sqlBuilder.append(" AND log_time >= ?");
                params.add(startDate + " 00:00:00");
            }
            String endDate = (String) filters.get("endDate");
            if (endDate != null && !endDate.isEmpty()) {
                sqlBuilder.append(" AND log_time <= ?");
                params.add(endDate + " 23:59:59");
            }
        }
        return sqlBuilder.toString();
    }

    @Override
    public List<AuditLog> findLogsByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM audit_logs WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        buildFilterSql(sqlBuilder, filters, params);

        if (orderBy != null && !orderBy.trim().isEmpty()) {
            // TODO: 对orderBy的字段进行白名单校验以防SQL注入
            sqlBuilder.append(" ORDER BY ").append(orderBy);
        } else {
            sqlBuilder.append(" ORDER BY log_time DESC"); // 默认按时间降序
        }

        if (pageNum > 0 && pageSize > 0) {
            sqlBuilder.append(" LIMIT ? OFFSET ?");
            params.add(pageSize);
            params.add((pageNum - 1) * pageSize);
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AuditLog> logs = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(mapRowToAuditLog(rs));
            }
        } catch (SQLException e) {
            logger.error("按条件查询审计日志失败", e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return logs;
    }

    @Override
    public int countLogsByFilters(Map<String, Object> filters) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(id) FROM audit_logs WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        buildFilterSql(sqlBuilder, filters, params);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("统计审计日志总数失败", e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return count;
    }

    private AuditLog mapRowToAuditLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getLong("id"));
        log.setUserId(rs.getObject("user_id", Integer.class));
        log.setUsername(rs.getString("username"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setActionType(rs.getString("action_type"));
        log.setTargetResource(rs.getString("target_resource"));
        log.setDetails(rs.getString("details"));
        log.setStatus(rs.getString("status"));
        log.setLogTime(rs.getTimestamp("log_time"));
        return log;
    }
}