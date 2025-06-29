package com.webb.dao.impl;

import com.webb.dao.EmployeeDependentDAO;
import com.webb.model.EmployeeDependent;
import com.webb.util.CryptoUtils;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDependentDAOImpl implements EmployeeDependentDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDependentDAOImpl.class);


    @Override
    public int addDependent(EmployeeDependent dependent) throws SQLException {
        String sql = "INSERT INTO employee_dependents (employee_id, annual_deduction_id, " +
                "dependent_name_encrypted, dependent_id_card_encrypted, relationship, " +
                "deduction_type_involved, birth_date, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int generatedId = -1;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, dependent.getEmployeeId());
            if (dependent.getAnnualDeductionId() != null) {
                pstmt.setInt(2, dependent.getAnnualDeductionId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            byte[] nameBytes = dependent.getDependentName().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(3, CryptoUtils.sm4Encrypt(nameBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            byte[] idCardBytes = dependent.getDependentIdCardNumber().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(4, CryptoUtils.sm4Encrypt(idCardBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            pstmt.setString(5, dependent.getRelationship());
            pstmt.setString(6, dependent.getDeductionTypeInvolved());
            pstmt.setDate(7, dependent.getBirthDate());
            pstmt.setString(8, dependent.getNotes());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    dependent.setId(generatedId);
                }
            }
            conn.commit();
            logger.info("被抚养人信息添加成功: EmployeeID={}, Name=[PROTECTED]", dependent.getEmployeeId());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加被抚养人信息失败: EmployeeID={}", dependent.getEmployeeId(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return generatedId;
    }

    @Override
    public boolean updateDependent(EmployeeDependent dependent) throws SQLException {
        String sql = "UPDATE employee_dependents SET annual_deduction_id = ?, " +
                "dependent_name_encrypted = ?, dependent_id_card_encrypted = ?, relationship = ?, " +
                "deduction_type_involved = ?, birth_date = ?, notes = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ? AND employee_id = ?"; // 通过ID和EmployeeID联合更新，更安全
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            if (dependent.getAnnualDeductionId() != null) {
                pstmt.setInt(1, dependent.getAnnualDeductionId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }

            byte[] nameBytes = dependent.getDependentName().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(2, CryptoUtils.sm4Encrypt(nameBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            byte[] idCardBytes = dependent.getDependentIdCardNumber().getBytes(StandardCharsets.UTF_8);
            pstmt.setBytes(3, CryptoUtils.sm4Encrypt(idCardBytes, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV));

            pstmt.setString(4, dependent.getRelationship());
            pstmt.setString(5, dependent.getDeductionTypeInvolved());
            pstmt.setDate(6, dependent.getBirthDate());
            pstmt.setString(7, dependent.getNotes());
            pstmt.setInt(8, dependent.getId());
            pstmt.setInt(9, dependent.getEmployeeId());


            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("被抚养人信息更新成功: ID={}", dependent.getId());
            else logger.warn("被抚养人信息更新失败或未找到: ID={}", dependent.getId());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新被抚养人信息失败: ID={}", dependent.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public boolean deleteDependent(int id) throws SQLException {
        String sql = "DELETE FROM employee_dependents WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("被抚养人信息删除成功: ID={}", id);
            else logger.warn("被抚养人信息删除失败或未找到: ID={}", id);
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("删除被抚养人信息失败: ID={}", id, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public EmployeeDependent findDependentById(int id) throws SQLException {
        String sql = "SELECT * FROM employee_dependents WHERE id = ?";
        return findSingleDependentBySql(sql, id);
    }

    @Override
    public EmployeeDependent findDependentByEmployeeAndIdCard(int employeeId, String idCardNumber) throws SQLException {
        byte[] encryptedIdCardBytes = CryptoUtils.sm4Encrypt(idCardNumber.getBytes(StandardCharsets.UTF_8), CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
        if (encryptedIdCardBytes == null) {
            logger.warn("被抚养人身份证号 {} 加密失败，无法查询。", idCardNumber);
            return null;
        }
        String sql = "SELECT * FROM employee_dependents WHERE employee_id = ? AND dependent_id_card_encrypted = ?";
        return findSingleDependentBySql(sql, employeeId, encryptedIdCardBytes);
    }

    @Override
    public List<EmployeeDependent> findDependentsByEmployeeId(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employee_dependents WHERE employee_id = ? ORDER BY relationship, created_at DESC";
        return findDependentsBySql(sql, employeeId);
    }

    @Override
    public List<EmployeeDependent> findDependentsByAnnualDeductionId(int annualDeductionId) throws SQLException {
        String sql = "SELECT * FROM employee_dependents WHERE annual_deduction_id = ? ORDER BY relationship, created_at DESC";
        return findDependentsBySql(sql, annualDeductionId);
    }

    private EmployeeDependent findSingleDependentBySql(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmployeeDependent dependent = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof byte[]) {
                    pstmt.setBytes(i + 1, (byte[]) params[i]);
                } else {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dependent = mapRowToDependent(rs);
            }
        } catch (SQLException e) {
            logger.error("查询单个被抚养人信息失败, SQL: {}", sql.split("\\?")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return dependent;
    }

    private List<EmployeeDependent> findDependentsBySql(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<EmployeeDependent> dependents = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                dependents.add(mapRowToDependent(rs));
            }
        } catch (SQLException e) {
            logger.error("查询被抚养人信息列表失败, SQL: {}", sql.split("\\?")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return dependents;
    }

    private EmployeeDependent mapRowToDependent(ResultSet rs) throws SQLException {
        EmployeeDependent dependent = new EmployeeDependent();
        dependent.setId(rs.getInt("id"));
        dependent.setEmployeeId(rs.getInt("employee_id"));
        dependent.setAnnualDeductionId(rs.getObject("annual_deduction_id", Integer.class)); // 处理可能的NULL

        byte[] nameEncrypted = rs.getBytes("dependent_name_encrypted");
        if (nameEncrypted != null) {
            byte[] nameDecrypted = CryptoUtils.sm4Decrypt(nameEncrypted, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
            dependent.setDependentName(nameDecrypted != null ? new String(nameDecrypted, StandardCharsets.UTF_8) : "[解密失败]");
        }

        byte[] idCardEncrypted = rs.getBytes("dependent_id_card_encrypted");
        if (idCardEncrypted != null) {
            byte[] idCardDecrypted = CryptoUtils.sm4Decrypt(idCardEncrypted, CryptoUtils.SM4_KEY, CryptoUtils.SM4_IV);
            dependent.setDependentIdCardNumber(idCardDecrypted != null ? new String(idCardDecrypted, StandardCharsets.UTF_8) : "[解密失败]");
        }

        dependent.setRelationship(rs.getString("relationship"));
        dependent.setDeductionTypeInvolved(rs.getString("deduction_type_involved"));
        dependent.setBirthDate(rs.getDate("birth_date"));
        dependent.setNotes(rs.getString("notes"));
        dependent.setCreatedAt(rs.getTimestamp("created_at"));
        dependent.setUpdatedAt(rs.getTimestamp("updated_at"));

        // 此处暂不加载关联的 Employee 对象，可在Service层按需加载
        return dependent;
    }
}