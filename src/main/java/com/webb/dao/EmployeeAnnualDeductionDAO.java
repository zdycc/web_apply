package com.webb.dao;

import com.webb.model.EmployeeAnnualDeduction;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeAnnualDeductionDAO {

    /**
     * 为员工添加新的年度专项附加扣除记录
     * @param deduction 包含扣除信息的对象
     * @return 返回生成的记录ID，如果失败则返回-1或抛出异常
     * @throws SQLException SQL执行异常
     */
    int addAnnualDeduction(EmployeeAnnualDeduction deduction) throws SQLException;

    /**
     * 更新员工的年度专项附加扣除记录
     * @param deduction 包含更新后扣除信息的对象
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean updateAnnualDeduction(EmployeeAnnualDeduction deduction) throws SQLException;

    /**
     * 根据ID删除年度专项附加扣除记录
     * @param id 记录ID
     * @return 删除成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean deleteAnnualDeduction(int id) throws SQLException;

    /**
     * 根据记录ID查询年度专项附加扣除信息
     * @param id 记录ID
     * @return EmployeeAnnualDeduction 对象，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    EmployeeAnnualDeduction findAnnualDeductionById(int id) throws SQLException;

    /**
     * 根据员工ID和年份查询年度专项附加扣除信息
     * @param employeeId 员工ID
     * @param year 年份
     * @return EmployeeAnnualDeduction 对象，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    EmployeeAnnualDeduction findAnnualDeductionByEmployeeIdAndYear(int employeeId, int year) throws SQLException;

    /**
     * 根据员工ID查询其所有的年度专项附加扣除记录
     * @param employeeId 员工ID
     * @return 该员工的扣除记录列表
     * @throws SQLException SQL执行异常
     */
    List<EmployeeAnnualDeduction> findAnnualDeductionsByEmployeeId(int employeeId) throws SQLException;

    /**
     * 查询所有员工的所有年度专项附加扣除记录 (可能用于报表或特定查询，注意数据量)
     * @return 所有扣除记录列表
     * @throws SQLException SQL执行异常
     */
    List<EmployeeAnnualDeduction> findAllAnnualDeductions() throws SQLException;
}