package com.webb.dao;

import com.webb.model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {

    /**
     * 添加新员工
     * @param employee 员工对象 (包含需要加密的明文字段)
     * @return 返回生成的员工ID，如果失败则返回-1或抛出异常
     * @throws SQLException SQL执行异常
     */
    int addEmployee(Employee employee) throws SQLException;

    /**
     * 更新员工信息
     * @param employee 员工对象 (包含需要加密的明文字段)
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean updateEmployee(Employee employee) throws SQLException;

    /**
     * 根据ID删除员工
     * @param employeeId 员工ID
     * @return 删除成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean deleteEmployee(int employeeId) throws SQLException;

    /**
     * 根据ID查询员工信息
     * @param employeeId 员工ID
     * @return Employee 对象 (包含已解密的明文字段)，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    Employee findEmployeeById(int employeeId) throws SQLException;

    /**
     * 根据员工编号查询员工信息
     * @param employeeNumber 员工编号
     * @return Employee 对象 (包含已解密的明文字段)，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    Employee findEmployeeByEmployeeNumber(String employeeNumber) throws SQLException;

    /**
     * 根据身份证号查询员工信息 (主要用于检查唯一性，返回的员工信息敏感字段也应解密)
     * @param idCardNumber 身份证号 (明文，方法内部会加密后查询)
     * @return Employee 对象，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    Employee findEmployeeByIdCardNumber(String idCardNumber) throws SQLException;


    /**
     * 查询所有员工信息 (可带分页和过滤参数)
     * @return 员工列表 (包含已解密的明文字段)
     * @throws SQLException SQL执行异常
     */
    List<Employee> findAllEmployees() throws SQLException; // 后续可添加分页和过滤参数

    /**
     * 根据部门ID查询员工列表
     * @param departmentId 部门ID
     * @return 该部门下的员工列表 (包含已解密的明文字段)
     * @throws SQLException SQL执行异常
     */
    List<Employee> findEmployeesByDepartmentId(int departmentId) throws SQLException;

    /**
     * 根据搜索条件查询员工信息
     * @param searchKeyword 搜索关键词（用于姓名和员工编号的模糊匹配，可为null）
     * @param departmentId 部门ID（为null或0表示不按部门筛选）
     * @return 符合条件的员工列表 (包含已解密的明文字段)
     * @throws SQLException SQL执行异常
     */
    List<Employee> searchEmployees(String searchKeyword, Integer departmentId) throws SQLException;
}