package com.webb.dao.impl;

import com.webb.dao.DepartmentDAO;
import com.webb.dao.EmployeeDAO;
import com.webb.model.Department;
import com.webb.model.Employee;
import com.webb.util.CryptoUtils;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOImpl.class);
    private DepartmentDAO departmentDAO = new DepartmentDAOImpl();

    @Override
    public int addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (employee_number, name_encrypted, id_card_number_encrypted, " +
                "phone_number_encrypted, address_encrypted, department_id, position, job_title, " +
                "hire_date, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int employeeId = -1;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, employee.getEmployeeNumber());

            byte[] nameBytes = employee.getName().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(2, CryptoUtils.sm4Encrypt(nameBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            byte[] idCardBytes = employee.getIdCardNumber().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(3, CryptoUtils.sm4Encrypt(idCardBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            if (employee.getPhoneNumber() != null && !employee.getPhoneNumber().isEmpty()) {
                byte[] phoneBytes = employee.getPhoneNumber().getBytes(StandardCharsets.UTF_8);
                pstmt.setBytes(4, CryptoUtils.sm4Encrypt(phoneBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));
            } else {
                pstmt.setNull(4, Types.VARBINARY);
            }

            if (employee.getAddress() != null && !employee.getAddress().isEmpty()) {
                byte[] addressBytes = employee.getAddress().getBytes(StandardCharsets.UTF_8);
                pstmt.setBytes(5, CryptoUtils.sm4Encrypt(addressBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));
            } else {
                pstmt.setNull(5, Types.VARBINARY);
            }

            pstmt.setInt(6, employee.getDepartmentId());
            pstmt.setString(7, employee.getPosition());
            pstmt.setString(8, employee.getJobTitle());
            pstmt.setDate(9, employee.getHireDate());
            pstmt.setString(10, employee.getStatus() != null ? employee.getStatus() : "active");


            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    employeeId = generatedKeys.getInt(1);
                    employee.setId(employeeId);
                }
            }
            conn.commit();
            logger.info("员工添加成功: {}", employee.getEmployeeNumber());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加员工失败: {}", employee.getEmployeeNumber(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return employeeId;
    }

    @Override
    public boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET employee_number = ?, name_encrypted = ?, id_card_number_encrypted = ?, " +
                "phone_number_encrypted = ?, address_encrypted = ?, department_id = ?, position = ?, " +
                "job_title = ?, hire_date = ?, status = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, employee.getEmployeeNumber());

            byte[] nameBytes = employee.getName().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(2, CryptoUtils.sm4Encrypt(nameBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            byte[] idCardBytes = employee.getIdCardNumber().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(3, CryptoUtils.sm4Encrypt(idCardBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            if (employee.getPhoneNumber() != null && !employee.getPhoneNumber().isEmpty()) {
                byte[] phoneBytes = employee.getPhoneNumber().getBytes(StandardCharsets.UTF_8);
                pstmt.setBytes(4, CryptoUtils.sm4Encrypt(phoneBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));
            } else {
                pstmt.setNull(4, Types.VARBINARY);
            }

            if (employee.getAddress() != null && !employee.getAddress().isEmpty()) {
                byte[] addressBytes = employee.getAddress().getBytes(StandardCharsets.UTF_8);
                pstmt.setBytes(5, CryptoUtils.sm4Encrypt(addressBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));
            } else {
                pstmt.setNull(5, Types.VARBINARY);
            }

            pstmt.setInt(6, employee.getDepartmentId());
            pstmt.setString(7, employee.getPosition());
            pstmt.setString(8, employee.getJobTitle());
            pstmt.setDate(9, employee.getHireDate());
            pstmt.setString(10, employee.getStatus());
            pstmt.setInt(11, employee.getId());

            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("员工信息更新成功: ID={}", employee.getId());
            else logger.warn("员工信息更新失败或未找到: ID={}", employee.getId());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新员工信息失败: ID={}", employee.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("员工删除成功: ID={}", employeeId);
            else logger.warn("员工删除失败或未找到: ID={}", employeeId);
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("删除员工失败: ID={}", employeeId, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public Employee findEmployeeById(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        return findSingleEmployeeBySql(sql, employeeId);
    }

    @Override
    public Employee findEmployeeByEmployeeNumber(String employeeNumber) throws SQLException {
        String sql = "SELECT * FROM employees WHERE employee_number = ?";
        return findSingleEmployeeBySql(sql, employeeNumber);
    }

    @Override
    public Employee findEmployeeByIdCardNumber(String idCardNumber) throws SQLException {
        byte[] encryptedIdCardBytes = CryptoUtils.sm4Encrypt(idCardNumber.getBytes(StandardCharsets.UTF_8), CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
        if (encryptedIdCardBytes == null) {
            logger.warn("身份证号 {} 加密失败，无法查询。", idCardNumber);
            return null;
        }
        String sql = "SELECT * FROM employees WHERE id_card_number_encrypted = ?";
        return findSingleEmployeeBySql(sql, (Object)encryptedIdCardBytes);
    }

    private Employee findSingleEmployeeBySql(String sql, Object parameter) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Employee employee = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            if (parameter instanceof Integer) {
                pstmt.setInt(1, (Integer) parameter);
            } else if (parameter instanceof String) {
                pstmt.setString(1, (String) parameter);
            } else if (parameter instanceof byte[]) {
                pstmt.setBytes(1, (byte[]) parameter);
            } else {
                throw new SQLException("不支持的查询参数类型: " + parameter.getClass().getName());
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                employee = mapRowToEmployee(rs);
            }
        } catch (SQLException e) {
            String paramString = (parameter != null) ? parameter.toString() : "null";
            logger.error("查询单个员工失败, SQL (部分): {}, Param (部分): {}",
                    sql.split("\\?")[0],
                    paramString.substring(0,Math.min(20,paramString.length())),
                    e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return employee;
    }

    @Override
    public List<Employee> findAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees ORDER BY employee_number ASC";
        return findEmployeesBySql(sql); // 调用通用的列表查询方法
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(int departmentId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE department_id = ? ORDER BY employee_number ASC";
        return findEmployeesBySql(sql, departmentId); // 调用通用的列表查询方法
    }

    // 通用的列表查询方法，用于 findAllEmployees 和 findEmployeesByDepartmentId
    private List<Employee> findEmployeesBySql(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Employee> employees = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            logger.error("查询员工列表失败, SQL (部分): {}", sql.split("\\?")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return employees;
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setEmployeeNumber(rs.getString("employee_number"));

        byte[] nameEncrypted = rs.getBytes("name_encrypted");
        if (nameEncrypted != null) {
            byte[] nameDecrypted = CryptoUtils.sm4Decrypt(nameEncrypted, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
            employee.setName(nameDecrypted != null ? new String(nameDecrypted, StandardCharsets.UTF_8) : "[解密失败]");
        } else {
            employee.setName("[无姓名记录]");
        }

        byte[] idCardEncrypted = rs.getBytes("id_card_number_encrypted");
        if (idCardEncrypted != null) {
            byte[] idCardDecrypted = CryptoUtils.sm4Decrypt(idCardEncrypted, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
            employee.setIdCardNumber(idCardDecrypted != null ? new String(idCardDecrypted, StandardCharsets.UTF_8) : "[解密失败]");
        } else {
            employee.setIdCardNumber("[无身份证记录]");
        }

        byte[] phoneEncrypted = rs.getBytes("phone_number_encrypted");
        if (phoneEncrypted != null) {
            byte[] phoneDecrypted = CryptoUtils.sm4Decrypt(phoneEncrypted, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
            employee.setPhoneNumber(phoneDecrypted != null ? new String(phoneDecrypted, StandardCharsets.UTF_8) : null);
        }

        byte[] addressEncrypted = rs.getBytes("address_encrypted");
        if (addressEncrypted != null) {
            byte[] addressDecrypted = CryptoUtils.sm4Decrypt(addressEncrypted, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
            employee.setAddress(addressDecrypted != null ? new String(addressDecrypted, StandardCharsets.UTF_8) : null);
        }

        employee.setDepartmentId(rs.getInt("department_id"));
        employee.setPosition(rs.getString("position"));
        employee.setJobTitle(rs.getString("job_title"));
        employee.setHireDate(rs.getDate("hire_date"));
        employee.setStatus(rs.getString("status"));
        employee.setCreatedAt(rs.getTimestamp("created_at"));
        employee.setUpdatedAt(rs.getTimestamp("updated_at"));

        if (employee.getDepartmentId() > 0) {
            try {
                Department department = departmentDAO.findDepartmentById(employee.getDepartmentId());
                employee.setDepartment(department);
            } catch (SQLException e) {
                logger.warn("为员工 ID: {} 获取部门信息 (DeptID: {}) 失败: {}", employee.getId(), employee.getDepartmentId(), e.getMessage());
            }
        }
        return employee;
    }

    @Override
    public List<Employee> searchEmployees(String searchKeyword, Integer departmentId) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM employees e LEFT JOIN departments d ON e.department_id = d.id WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        
        // 添加部门过滤条件
        if (departmentId != null && departmentId > 0) {
            sql.append(" AND e.department_id = ?");
            params.add(departmentId);
        }
        
        // 添加关键词搜索条件（员工编号）
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND e.employee_number LIKE ?");
            params.add("%" + searchKeyword.trim() + "%");
        }
        
        sql.append(" ORDER BY e.employee_number ASC");
        
        return findEmployeesBySql(sql.toString(), params.toArray());
    }
}