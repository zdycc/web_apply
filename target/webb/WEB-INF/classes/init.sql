CREATE DATABASE IF NOT EXISTS webb_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE webb_db;

-- 角色表
CREATE TABLE IF NOT EXISTS `roles` (
                                       `id` INT PRIMARY KEY AUTO_INCREMENT,
                                       `role_name` VARCHAR(50) UNIQUE NOT NULL COMMENT '角色名称 (人事管理员, 财务管理员, 总经理, 系统管理员, 审计员)',
    `description` VARCHAR(255) COMMENT '角色描述'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 初始化角色数据
INSERT INTO `roles` (`role_name`, `description`) VALUES
                                                     ('系统管理员', '管理系统用户、角色权限等'),
                                                     ('人事管理员', '管理部门、人员信息、专项附加扣除'),
                                                     ('财务管理员', '管理工资条目、工资计算与发放'),
                                                     ('总经理', '查看各类报表和信息，审批权限'),
                                                     ('审计员', '查看系统操作日志');

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` INT PRIMARY KEY AUTO_INCREMENT,
                                       `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '登录用户名',
    `password_hash` VARCHAR(128) NOT NULL COMMENT 'SM3加密后的密码哈希',
    -- `salt` VARCHAR(64) COMMENT '密码盐值 (SM3通常不需要盐，但如果特定实现需要则添加)', -- SM3是哈希，一般不加盐，加盐是为了防彩虹表，对强哈希意义不大
    `role_id` INT NOT NULL COMMENT '角色ID',
    `employee_id` INT UNIQUE COMMENT '关联的员工ID (如果用户是员工, 后续添加employees表后建立外键)',
    `last_password_change_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后密码修改日期',
    `failed_login_attempts` INT DEFAULT 0 COMMENT '连续登录失败次数',
    `lockout_until` TIMESTAMP NULL COMMENT '锁定截止时间',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '账户是否激活',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 部门表
CREATE TABLE IF NOT EXISTS `departments` (
                                             `id` INT PRIMARY KEY AUTO_INCREMENT,
                                             `dept_name` VARCHAR(100) UNIQUE NOT NULL COMMENT '部门名称',
    `parent_dept_id` INT NULL COMMENT '上级部门ID (用于支持层级结构)',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`parent_dept_id`) REFERENCES `departments`(`id`) ON DELETE SET NULL -- 如果上级部门被删除，子部门的parent_dept_id设为NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 为系统管理员创建一个初始用户 (密码：Admin@12345)
-- SM3哈希值 of "Admin@12345" (请用 CryptoUtils.sm3Hash("Admin@12345") 生成实际值)
-- 例如，假设其值为 "abcdef1234567890..." (实际值会不同)
-- INSERT INTO `users` (`username`, `password_hash`, `role_id`, `is_active`) VALUES
-- ('admin', '【这里替换为Admin@12345的SM3哈希值】', (SELECT id FROM roles WHERE role_name='系统管理员'), TRUE);
-- 请运行CryptoUtils.main() 得到 "Admin@12345" 的SM3哈希值，然后手动插入。
-- 例如，如果CryptoUtils.sm3Hash("Admin@12345") 输出是 0109d10398a48a9e8377e6806760291884788293191080318435413827302640
-- 则SQL为:
-- INSERT INTO `users` (`username`, `password_hash`, `role_id`, `is_active`) VALUES
-- ('admin', 'c4039a5810e67a8b1eb72cc2b9d9f2e55ca0e2fd2b84f55e212ad3a23aec89bc', (SELECT id FROM roles WHERE role_name='系统管理员'), TRUE);