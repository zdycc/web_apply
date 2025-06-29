USE webb_db;

CREATE TABLE IF NOT EXISTS `monthly_salaries` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '工资记录唯一ID',
    `employee_id` INT NOT NULL COMMENT '关联的员工ID',
    `year_month` VARCHAR(7) NOT NULL COMMENT '工资所属月份 (格式: YYYY-MM, 例如: 2023-01)',

    -- 应发项目 (Earnings)
    `basic_salary` DECIMAL(12,2) DEFAULT 0.00 COMMENT '基本工资',
    `post_allowance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '岗位津贴',
    `lunch_subsidy` DECIMAL(10,2) DEFAULT 0.00 COMMENT '午餐补贴',
    `overtime_pay` DECIMAL(10,2) DEFAULT 0.00 COMMENT '加班工资',
    `attendance_bonus` DECIMAL(10,2) DEFAULT 0.00 COMMENT '全勤工资',
    `other_earnings` DECIMAL(10,2) DEFAULT 0.00 COMMENT '其他应发款项/奖金',
    -- `total_earnings` DECIMAL(12,2) AS (`basic_salary` + `post_allowance` + `lunch_subsidy` + `overtime_pay` + `attendance_bonus` + `other_earnings`) STORED COMMENT '应发工资合计 (计算列)',

    -- 税前扣除项目 (Pre-tax Deductions)
    `social_security_personal` DECIMAL(10,2) DEFAULT 0.00 COMMENT '个人承担社保总额 (养老,医疗,失业)',
    `provident_fund_personal` DECIMAL(10,2) DEFAULT 0.00 COMMENT '个人承担住房公积金',
    `special_additional_deduction_monthly` DECIMAL(10,2) DEFAULT 0.00 COMMENT '本月专项附加扣除总额 (从年度申报计算得出)',
    `enterprise_annuity_personal` DECIMAL(10,2) DEFAULT 0.00 COMMENT '个人承担企业年金 (如果适用)',
    `other_pre_tax_deductions` DECIMAL(10,2) DEFAULT 0.00 COMMENT '其他税前扣除项 (如特定商业健康险)',
    -- `total_pre_tax_deductions` DECIMAL(12,2) AS (`social_security_personal` + `provident_fund_personal` + `special_additional_deduction_monthly` + `enterprise_annuity_personal` + `other_pre_tax_deductions`) STORED COMMENT '税前扣除总额 (计算列)',

    -- 应纳税所得额相关
    `taxable_income_before_threshold` DECIMAL(12,2) COMMENT '未减除起征点的应纳税所得额 (总应发 - 总税前扣除)', -- 这个是计算个税的基础（如果使用累计预扣法，这个概念可能需要调整为累计值）
    `tax_threshold_amount` DECIMAL(10,2) DEFAULT 5000.00 COMMENT '个税起征点/基本减除费用 (当前标准为5000元/月)', -- 可能会变化，记录当时的起征点
    `taxable_income` DECIMAL(12,2) COMMENT '应纳税所得额 (taxable_income_before_threshold - tax_threshold_amount, 若小于0则为0)', -- (计算列或应用层计算)

    -- 税款及税后扣除
    `personal_income_tax` DECIMAL(10,2) DEFAULT 0.00 COMMENT '个人所得税',
    `late_leave_deduction` DECIMAL(10,2) DEFAULT 0.00 COMMENT '迟到/早退/请假扣款 (需明确是税前调整还是税后扣)', -- 假设这里是税后调整，如果影响应纳税所得额，则应归入税前扣除或调整应发
    `other_post_tax_deductions` DECIMAL(10,2) DEFAULT 0.00 COMMENT '其他税后扣除项',

    -- 实发工资
    -- `net_salary` DECIMAL(12,2) AS (`total_earnings` - `total_pre_tax_deductions` - `personal_income_tax` - `late_leave_deduction` - `other_post_tax_deductions`) STORED COMMENT '实发工资 (计算列)',

    -- 如果不使用计算列，则需要显式定义以下字段：
    `total_earnings_manual` DECIMAL(12,2) DEFAULT 0.00 COMMENT '应发工资合计 (手动计算或应用层计算)',
    `total_pre_tax_deductions_manual` DECIMAL(12,2) DEFAULT 0.00 COMMENT '税前扣除总额 (手动计算或应用层计算)',
    `net_salary_manual` DECIMAL(12,2) DEFAULT 0.00 COMMENT '实发工资 (手动计算或应用层计算)',

    -- 状态与记录信息
    `salary_status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT '工资状态 (DRAFT草稿, PENDING_APPROVAL待审核, APPROVED已审核, PAID已发放, REJECTED已驳回)',
    `calculation_method_notes` VARCHAR(255) COMMENT '税款计算方法备注 (例如：月度，累计预扣)',
    `remarks` VARCHAR(500) COMMENT '工资备注',
    `created_by_user_id` INT COMMENT '创建人用户ID',
    `approved_by_user_id` INT COMMENT '审核人用户ID',
    `paid_by_user_id` INT COMMENT '发放操作人用户ID',
    `paid_date` DATETIME COMMENT '发放日期',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY `uk_employee_year_month` (`employee_id`, `year_month`), -- 每个员工每个月只能有一条工资记录
    FOREIGN KEY (`employee_id`) REFERENCES `employees`(`id`)
        ON DELETE RESTRICT -- 如果员工被删除，其工资记录通常不应级联删除，而是保留作为历史。如果一定要删除，需谨慎。
        ON UPDATE CASCADE,
    FOREIGN KEY (`created_by_user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`approved_by_user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`paid_by_user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='月度工资明细表';