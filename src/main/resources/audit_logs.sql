USE webb_db;

CREATE TABLE IF NOT EXISTS `audit_logs` (
                                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志唯一ID',
                                            `user_id` INT COMMENT '操作用户ID (可为空，如系统自动任务)',
                                            `username` VARCHAR(50) NOT NULL COMMENT '操作用户名',
    `ip_address` VARCHAR(45) COMMENT '操作者IP地址',
    `action_type` VARCHAR(100) NOT NULL COMMENT '操作类型 (例如: LOGIN_SUCCESS, LOGIN_FAILURE, VIEW_EMPLOYEES, ADD_DEPARTMENT)',
    `target_resource` VARCHAR(255) COMMENT '操作对象描述 (例如: Employee ID: 123, Department Name: 销售部)',
    `details` TEXT COMMENT '操作详情 (例如: 更新员工“张三”的手机号)',
    `status` VARCHAR(20) NOT NULL COMMENT '操作状态 (SUCCESS, FAILURE)',
    `log_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '日志记录时间',
    -- `hmac_sm3_hash` VARCHAR(128) COMMENT '日志记录的HMAC-SM3值 (选做，用于完整性保护)'
    KEY `idx_log_time` (`log_time`),
    KEY `idx_username` (`username`),
    KEY `idx_action_type` (`action_type`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安全审计日志表';