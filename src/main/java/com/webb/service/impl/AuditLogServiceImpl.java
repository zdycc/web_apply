package com.webb.service.impl;

import com.webb.dao.AuditLogDAO;
import com.webb.dao.impl.AuditLogDAOImpl;
import com.webb.model.AuditLog;
import com.webb.service.AuditLogService;
import com.webb.service.AuditLogServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AuditLogServiceImpl implements AuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);
    private AuditLogDAO auditLogDAO = new AuditLogDAOImpl();

    @Override
    public void recordLog(AuditLog log) {
        if (log == null || log.getUsername() == null || log.getActionType() == null) {
            logger.warn("尝试记录无效的审计日志（用户名或操作类型为空）。");
            return;
        }
        try {
            auditLogDAO.addLog(log);
        } catch (SQLException e) {
            logger.error("记录审计日志时发生数据库错误。", e);
        }
    }

    @Override
    public List<AuditLog> getLogsByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws AuditLogServiceException {
        try {
            return auditLogDAO.findLogsByFilters(filters, pageNum, pageSize, orderBy);
        } catch (SQLException e) {
            throw new AuditLogServiceException("查询审计日志失败。", e);
        }
    }

    @Override
    public int countLogsByFilters(Map<String, Object> filters) throws AuditLogServiceException {
        try {
            return auditLogDAO.countLogsByFilters(filters);
        } catch (SQLException e) {
            throw new AuditLogServiceException("统计审计日志总数失败。", e);
        }
    }
}