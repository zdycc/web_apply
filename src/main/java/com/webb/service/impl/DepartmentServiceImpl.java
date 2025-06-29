package com.webb.service.impl;

import com.webb.dao.DepartmentDAO;
import com.webb.dao.impl.DepartmentDAOImpl;
import com.webb.model.Department;
import com.webb.service.DepartmentService;
import com.webb.service.DepartmentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private DepartmentDAO departmentDAO = new DepartmentDAOImpl();

    @Override
    public boolean addDepartment(Department department) throws DepartmentServiceException {
        // 基本验证
        if (department == null || department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
            throw new DepartmentServiceException("部门名称不能为空。");
        }
        // 可以检查部门名称是否已存在等业务规则
        try {
            return departmentDAO.addDepartment(department) > 0;
        } catch (SQLException e) {
            logger.error("添加部门业务逻辑失败: {}", department.getDeptName(), e);
            throw new DepartmentServiceException("添加部门失败，数据库操作错误。", e);
        }
    }

    @Override
    public boolean updateDepartment(Department department) throws DepartmentServiceException {
        if (department == null || department.getId() <= 0 || department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
            throw new DepartmentServiceException("部门信息不完整或ID无效。");
        }
        // 检查 parent_dept_id 是否是其自身
        if (department.getParentDeptId() != null && department.getParentDeptId() == department.getId()) {
            throw new DepartmentServiceException("部门的上级部门不能是其自身。");
        }
        try {
            return departmentDAO.updateDepartment(department);
        } catch (SQLException e) {
            logger.error("更新部门业务逻辑失败: ID={}", department.getId(), e);
            throw new DepartmentServiceException("更新部门失败，数据库操作错误。", e);
        }
    }

    @Override
    public boolean deleteDepartment(int departmentId) throws DepartmentServiceException {
        if (departmentId <= 0) {
            throw new DepartmentServiceException("无效的部门ID。");
        }

        try {
            return departmentDAO.deleteDepartment(departmentId);
        } catch (SQLException e) {
            logger.error("删除部门业务逻辑失败: ID={}", departmentId, e);
            // 检查是否是外键约束违规，给出更友好的提示
            if (e.getSQLState().startsWith("23")) { // SQLState 23xxx 通常表示完整性约束违规
                throw new DepartmentServiceException("删除部门失败：该部门可能被其他数据引用（如员工或子部门）。", e);
            }
            throw new DepartmentServiceException("删除部门失败，数据库操作错误。", e);
        }
    }

    @Override
    public Department getDepartmentById(int departmentId) throws DepartmentServiceException {
        if (departmentId <= 0) {
            throw new DepartmentServiceException("无效的部门ID。");
        }
        try {
            return departmentDAO.findDepartmentById(departmentId);
        } catch (SQLException e) {
            logger.error("获取部门信息业务逻辑失败: ID={}", departmentId, e);
            throw new DepartmentServiceException("获取部门信息失败，数据库操作错误。", e);
        }
    }

    @Override
    public List<Department> getAllDepartments() throws DepartmentServiceException {
        try {
            return departmentDAO.findAllDepartments();
        } catch (SQLException e) {
            logger.error("获取所有部门列表业务逻辑失败", e);
            throw new DepartmentServiceException("获取部门列表失败，数据库操作错误。", e);
        }
    }

    @Override
    public List<Department> getDepartmentsByParentId(Integer parentId) throws DepartmentServiceException {
        try {
            return departmentDAO.findDepartmentsByParentId(parentId);
        } catch (SQLException e) {
            logger.error("根据父ID获取部门列表业务逻辑失败: ParentID={}", parentId, e);
            throw new DepartmentServiceException("获取子部门列表失败，数据库操作错误。", e);
        }
    }
}