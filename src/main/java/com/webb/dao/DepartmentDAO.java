package com.webb.dao;

import com.webb.model.Department;
import java.sql.SQLException;
import java.util.List;

public interface DepartmentDAO {

    /**
     * 添加新部门
     * @param department 部门对象
     * @return 返回生成的部门ID，如果失败则返回-1或抛出异常
     * @throws SQLException SQL执行异常
     */
    int addDepartment(Department department) throws SQLException;

    /**
     * 更新部门信息
     * @param department 部门对象
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean updateDepartment(Department department) throws SQLException;

    /**
     * 根据ID删除部门
     * @param departmentId 部门ID
     * @return 删除成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean deleteDepartment(int departmentId) throws SQLException;

    /**
     * 根据ID查询部门信息
     * @param departmentId 部门ID
     * @return Department 对象，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    Department findDepartmentById(int departmentId) throws SQLException;

    /**
     * 查询所有部门信息
     * @return 部门列表
     * @throws SQLException SQL执行异常
     */
    List<Department> findAllDepartments() throws SQLException;

    /**
     * （可选）根据父部门ID查询子部门列表
     * @param parentId 父部门ID
     * @return 子部门列表
     * @throws SQLException SQL执行异常
     */
    List<Department> findDepartmentsByParentId(Integer parentId) throws SQLException;
}