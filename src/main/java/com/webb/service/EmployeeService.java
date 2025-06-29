package com.webb.service;

import com.webb.model.Employee;
import java.util.List;

public interface EmployeeService {

    /**
     * 添加新员工
     * @param employee 员工对象
     * @return 添加成功返回 true，否则 false
     * @throws EmployeeServiceException 业务逻辑异常
     */
    boolean addEmployee(Employee employee) throws EmployeeServiceException;

    /**
     * 更新员工信息
     * @param employee 员工对象
     * @return 更新成功返回 true，否则 false
     * @throws EmployeeServiceException 业务逻辑异常
     */
    boolean updateEmployee(Employee employee) throws EmployeeServiceException;

    /**
     * 根据ID删除员工
     * @param employeeId 员工ID
     * @return 删除成功返回 true，否则 false
     * @throws EmployeeServiceException 业务逻辑异常
     */
    boolean deleteEmployee(int employeeId) throws EmployeeServiceException;

    /**
     * 根据ID查询员工信息
     * @param employeeId 员工ID
     * @return Employee 对象，未找到则返回 null
     * @throws EmployeeServiceException 业务逻辑异常
     */
    Employee getEmployeeById(int employeeId) throws EmployeeServiceException;

    /**
     * 根据员工编号查询员工信息
     * @param employeeNumber 员工编号
     * @return Employee 对象，未找到则返回 null
     * @throws EmployeeServiceException 业务逻辑异常
     */
    Employee getEmployeeByEmployeeNumber(String employeeNumber) throws EmployeeServiceException;

    /**
     * 查询所有员工信息
     * @return 员工列表
     * @throws EmployeeServiceException 业务逻辑异常
     */
    List<Employee> getAllEmployees() throws EmployeeServiceException;

    /**
     * 根据部门ID查询员工列表
     * @param departmentId 部门ID
     * @return 该部门下的员工列表
     * @throws EmployeeServiceException 业务逻辑异常
     */
    List<Employee> getEmployeesByDepartmentId(int departmentId) throws EmployeeServiceException;

    /**
     * 数据脱敏处理（如果需要在Service层统一处理）
     * @param employee 原始员工对象
     * @return 脱敏后的员工对象副本（或直接修改原对象）
     */
    Employee desensitizeEmployeeData(Employee employee);

    /**
     * 根据搜索条件查询员工信息
     * @param searchKeyword 搜索关键词（用于姓名和员工编号的模糊匹配）
     * @param departmentId 部门ID（为null或0表示不按部门筛选）
     * @return 符合条件的员工列表
     * @throws EmployeeServiceException 业务逻辑异常
     */
    List<Employee> searchEmployees(String searchKeyword, Integer departmentId) throws EmployeeServiceException;

    /**
     * （可选）批量导入员工
     * @param employees 员工列表
     * @return 导入结果摘要或详细报告
     * @throws EmployeeServiceException 业务逻辑异常
     */
    // String bulkImportEmployees(List<Employee> employees) throws EmployeeServiceException;
}

