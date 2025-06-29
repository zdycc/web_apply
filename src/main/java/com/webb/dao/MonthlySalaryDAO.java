package com.webb.dao;

import com.webb.model.MonthlySalary;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MonthlySalaryDAO {

    /**
     * 添加新的月度工资记录
     * @param salary 月度工资对象
     * @return 返回生成的记录ID，如果失败则返回-1或抛出异常
     * @throws SQLException SQL执行异常
     */
    long addMonthlySalary(MonthlySalary salary) throws SQLException;

    /**
     * 更新月度工资记录
     * @param salary 月度工资对象
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean updateMonthlySalary(MonthlySalary salary) throws SQLException;

    /**
     * 根据ID删除月度工资记录
     * @param id 记录ID
     * @return 删除成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean deleteMonthlySalary(long id) throws SQLException;

    /**
     * 根据记录ID查询月度工资信息
     * @param id 记录ID
     * @return MonthlySalary 对象，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    MonthlySalary findMonthlySalaryById(long id) throws SQLException;

    /**
     * 根据员工ID和工资月份查询月度工资信息
     * @param employeeId 员工ID
     * @param yearMonth 工资月份 (格式: "YYYY-MM")
     * @return MonthlySalary 对象，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    MonthlySalary findMonthlySalaryByEmployeeAndYearMonth(int employeeId, String yearMonth) throws SQLException;

    /**
     * 根据员工ID查询其所有的月度工资记录 (按月份降序)
     * @param employeeId 员工ID
     * @return 该员工的工资记录列表
     * @throws SQLException SQL执行异常
     */
    List<MonthlySalary> findMonthlySalariesByEmployeeId(int employeeId) throws SQLException;

    /**
     * 根据条件查询月度工资记录 (支持分页和排序)
     * @param filters 查询条件 (例如: departmentId, yearMonth范围, employeeName模糊查询等)
     * @param pageNum 页码 (从1开始)
     * @param pageSize 每页记录数
     * @param orderBy 排序字段和方式 (例如: "year_month DESC, employee_id ASC")
     * @return 符合条件的工资记录列表
     * @throws SQLException SQL执行异常
     */
    List<MonthlySalary> findMonthlySalariesByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws SQLException;

    /**
     * 根据条件统计月度工资记录总数
     * @param filters 查询条件
     * @return 记录总数
     * @throws SQLException SQL执行异常
     */
    int countMonthlySalariesByFilters(Map<String, Object> filters) throws SQLException;

    /**
     * (可选) 批量插入月度工资记录 (用于数据导入)
     * @param salaries 月度工资对象列表
     * @return 成功插入的记录数数组，或总成功数
     * @throws SQLException SQL执行异常
     */
    // int[] batchAddMonthlySalaries(List<MonthlySalary> salaries) throws SQLException;
}