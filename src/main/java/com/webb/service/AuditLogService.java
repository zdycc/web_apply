package com.webb.service;

import com.webb.model.AuditLog;
import java.util.List;
import java.util.Map;

public interface AuditLogService {

    /**
     * 记录一条审计日志
     * @param log 要记录的日志对象
     */
    void recordLog(AuditLog log);

    /**
     * 根据条件查询审计日志
     * @param filters 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderBy 排序方式
     * @return 日志列表
     * @throws AuditLogServiceException 业务逻辑异常
     */
    List<AuditLog> getLogsByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws AuditLogServiceException;

    /**
     * 根据条件统计审计日志总数
     * @param filters 查询条件
     * @return 记录总数
     * @throws AuditLogServiceException 业务逻辑异常
     */
    int countLogsByFilters(Map<String, Object> filters) throws AuditLogServiceException;
}

