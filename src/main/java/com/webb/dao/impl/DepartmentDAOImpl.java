package com.webb.dao.impl;

import com.webb.dao.DepartmentDAO;
import com.webb.model.Department;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOImpl implements DepartmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentDAOImpl.class);

    @Override
    public int addDepartment(Department department) throws SQLException {
        String sql = "INSERT INTO departments (dept_name, parent_dept_id, created_at, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int departmentId = -1;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, department.getDeptName());
            if (department.getParentDeptId() != null) {
                pstmt.setInt(2, department.getParentDeptId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    departmentId = generatedKeys.getInt(1);
                }
            }
            conn.commit();
            logger.info("部门添加成功: {}", department.getDeptName());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加部门失败: {}", department.getDeptName(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return departmentId;
    }

    @Override
    public boolean updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE departments SET dept_name = ?, parent_dept_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, department.getDeptName());
            if (department.getParentDeptId() != null) {
                pstmt.setInt(2, department.getParentDeptId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setInt(3, department.getId());

            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("部门更新成功: ID={}", department.getId());
            else logger.warn("部门更新失败或未找到: ID={}", department.getId());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新部门失败: ID={}", department.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public boolean deleteDepartment(int departmentId) throws SQLException {
        String sql = "DELETE FROM departments WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);

            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("部门删除成功: ID={}", departmentId);
            else logger.warn("部门删除失败或未找到: ID={}", departmentId);
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("删除部门失败: ID={}. 可能是因为存在子部门或关联员工。", departmentId, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public Department findDepartmentById(int departmentId) throws SQLException {
        String sql = "SELECT id, dept_name, parent_dept_id, created_at, updated_at FROM departments WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Department department = null;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, departmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                department = mapRowToDepartment(rs);
            }
        } catch (SQLException e) {
            logger.error("通过ID查询部门失败: ID={}", departmentId, e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return department;
    }

    @Override
    public List<Department> findAllDepartments() throws SQLException {
        // *** 修改点：添加 ORDER BY id ASC ***
        String sql = "SELECT id, dept_name, parent_dept_id, created_at, updated_at FROM departments ORDER BY id ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Department> departments = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                departments.add(mapRowToDepartment(rs));
            }
        } catch (SQLException e) {
            logger.error("查询所有部门失败", e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return departments;
    }

    @Override
    public List<Department> findDepartmentsByParentId(Integer parentId) throws SQLException {
        String sql;
        // *** 修改点：添加 ORDER BY id ASC ***
        if (parentId == null) {
            sql = "SELECT id, dept_name, parent_dept_id, created_at, updated_at FROM departments WHERE parent_dept_id IS NULL ORDER BY id ASC";
        } else {
            sql = "SELECT id, dept_name, parent_dept_id, created_at, updated_at FROM departments WHERE parent_dept_id = ? ORDER BY id ASC";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Department> departments = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            if (parentId != null) {
                pstmt.setInt(1, parentId);
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                departments.add(mapRowToDepartment(rs));
            }
        } catch (SQLException e) {
            logger.error("根据父ID查询部门失败: ParentID={}", parentId, e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return departments;
    }

    private Department mapRowToDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("id"));
        department.setDeptName(rs.getString("dept_name"));
        department.setParentDeptId(rs.getObject("parent_dept_id", Integer.class));
        department.setCreatedAt(rs.getTimestamp("created_at"));
        department.setUpdatedAt(rs.getTimestamp("updated_at"));
        return department;
    }
}