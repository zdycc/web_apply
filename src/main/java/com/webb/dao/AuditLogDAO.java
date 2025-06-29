package com.webb.dao;

import com.webb.model.AuditLog;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AuditLogDAO {

    /**
     * 添加一条新的审计日志记录
     * @param log AuditLog 对象
     * @throws SQLException SQL执行异常
     */
    void addLog(AuditLog log) throws SQLException;

    /**
     * 根据条件查询审计日志记录 (支持分页和排序)
     * @param filters 查询条件 (例如: username, actionType, startDate, endDate)
     * @param pageNum 页码 (从1开始)
     * @param pageSize 每页记录数
     * @param orderBy 排序字段和方式 (例如: "log_time DESC")
     * @return 符合条件的日志记录列表
     * @throws SQLException SQL执行异常
     */
    List<AuditLog> findLogsByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws SQLException;

    /**
     * 根据条件统计审计日志记录总数
     * @param filters 查询条件
     * @return 记录总数
     * @throws SQLException SQL执行异常
     */
    int countLogsByFilters(Map<String, Object> filters) throws SQLException;
}