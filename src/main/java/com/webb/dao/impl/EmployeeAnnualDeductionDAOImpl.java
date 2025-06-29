package com.webb.dao.impl;

import com.webb.dao.EmployeeAnnualDeductionDAO;
import com.webb.model.EmployeeAnnualDeduction;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAnnualDeductionDAOImpl implements EmployeeAnnualDeductionDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeAnnualDeductionDAOImpl.class);

    @Override
    public int addAnnualDeduction(EmployeeAnnualDeduction deduction) throws SQLException {
        // 注意：total_annual_deduction 和 monthly_deduction_calculated 是数据库计算列，不需要在INSERT语句中显式提供
        String sql = "INSERT INTO employee_annual_deductions (employee_id, year, " +
                "children_education_amount, continuing_education_amount, serious_illness_medical_amount, " +
                "housing_loan_interest_amount, housing_rent_amount, elderly_care_amount, infant_care_amount, " +
                "remarks, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int generatedId = -1;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, deduction.getEmployeeId());
            pstmt.setInt(2, deduction.getYear());
            pstmt.setBigDecimal(3, deduction.getChildrenEducationAmount());
            pstmt.setBigDecimal(4, deduction.getContinuingEducationAmount());
            pstmt.setBigDecimal(5, deduction.getSeriousIllnessMedicalAmount());
            pstmt.setBigDecimal(6, deduction.getHousingLoanInterestAmount());
            pstmt.setBigDecimal(7, deduction.getHousingRentAmount());
            pstmt.setBigDecimal(8, deduction.getElderlyCareAmount());
            pstmt.setBigDecimal(9, deduction.getInfantCareAmount());
            pstmt.setString(10, deduction.getRemarks());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    deduction.setId(generatedId); // 更新对象ID
                }
            }
            conn.commit();
            logger.info("员工年度专项附加扣除记录添加成功: EmployeeID={}, Year={}", deduction.getEmployeeId(), deduction.getYear());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加员工年度专项附加扣除记录失败: EmployeeID={}, Year={}", deduction.getEmployeeId(), deduction.getYear(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return generatedId;
    }

    @Override
    public boolean updateAnnualDeduction(EmployeeAnnualDeduction deduction) throws SQLException {
        String sql = "UPDATE employee_annual_deductions SET " +
                "children_education_amount = ?, continuing_education_amount = ?, serious_illness_medical_amount = ?, " +
                "housing_loan_interest_amount = ?, housing_rent_amount = ?, elderly_care_amount = ?, " +
                "infant_care_amount = ?, remarks = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ? AND employee_id = ? AND year = ?"; // 通常通过ID更新，但employee_id和year可作额外校验
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, deduction.getChildrenEducationAmount());
            pstmt.setBigDecimal(2, deduction.getContinuingEducationAmount());
            pstmt.setBigDecimal(3, deduction.getSeriousIllnessMedicalAmount());
            pstmt.setBigDecimal(4, deduction.getHousingLoanInterestAmount());
            pstmt.setBigDecimal(5, deduction.getHousingRentAmount());
            pstmt.setBigDecimal(6, deduction.getElderlyCareAmount());
            pstmt.setBigDecimal(7, deduction.getInfantCareAmount());
            pstmt.setString(8, deduction.getRemarks());
            pstmt.setInt(9, deduction.getId());
            pstmt.setInt(10, deduction.getEmployeeId()); // 用于校验
            pstmt.setInt(11, deduction.getYear());       // 用于校验

            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("员工年度专项附加扣除记录更新成功: ID={}", deduction.getId());
            else logger.warn("员工年度专项附加扣除记录更新失败或未找到: ID={}", deduction.getId());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新员工年度专项附加扣除记录失败: ID={}", deduction.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public boolean deleteAnnualDeduction(int id) throws SQLException {
        String sql = "DELETE FROM employee_annual_deductions WHERE id = ?";
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
            if(success) logger.info("员工年度专项附加扣除记录删除成功: ID={}", id);
            else logger.warn("员工年度专项附加扣除记录删除失败或未找到: ID={}", id);
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("删除员工年度专项附加扣除记录失败: ID={}", id, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public EmployeeAnnualDeduction findAnnualDeductionById(int id) throws SQLException {
        String sql = "SELECT * FROM employee_annual_deductions WHERE id = ?";
        return findSingleDeductionBySql(sql, id);
    }

    @Override
    public EmployeeAnnualDeduction findAnnualDeductionByEmployeeIdAndYear(int employeeId, int year) throws SQLException {
        String sql = "SELECT * FROM employee_annual_deductions WHERE employee_id = ? AND year = ?";
        return findSingleDeductionBySql(sql, employeeId, year);
    }

    @Override
    public List<EmployeeAnnualDeduction> findAnnualDeductionsByEmployeeId(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employee_annual_deductions WHERE employee_id = ? ORDER BY year DESC";
        return findDeductionsBySql(sql, employeeId);
    }

    @Override
    public List<EmployeeAnnualDeduction> findAllAnnualDeductions() throws SQLException {
        String sql = "SELECT * FROM employee_annual_deductions ORDER BY employee_id ASC, year DESC";
        return findDeductionsBySql(sql);
    }

    private EmployeeAnnualDeduction findSingleDeductionBySql(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmployeeAnnualDeduction deduction = null;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                deduction = mapRowToAnnualDeduction(rs);
            }
        } catch (SQLException e) {
            logger.error("查询单个员工年度专项附加扣除记录失败, SQL: {}", sql.split("\\?")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return deduction;
    }

    private List<EmployeeAnnualDeduction> findDeductionsBySql(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<EmployeeAnnualDeduction> deductions = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                deductions.add(mapRowToAnnualDeduction(rs));
            }
        } catch (SQLException e) {
            logger.error("查询员工年度专项附加扣除记录列表失败, SQL: {}", sql.split("\\?")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return deductions;
    }

    private EmployeeAnnualDeduction mapRowToAnnualDeduction(ResultSet rs) throws SQLException {
        EmployeeAnnualDeduction deduction = new EmployeeAnnualDeduction();
        deduction.setId(rs.getInt("id"));
        deduction.setEmployeeId(rs.getInt("employee_id"));
        deduction.setYear(rs.getInt("year"));
        deduction.setChildrenEducationAmount(rs.getBigDecimal("children_education_amount"));
        deduction.setContinuingEducationAmount(rs.getBigDecimal("continuing_education_amount"));
        deduction.setSeriousIllnessMedicalAmount(rs.getBigDecimal("serious_illness_medical_amount"));
        deduction.setHousingLoanInterestAmount(rs.getBigDecimal("housing_loan_interest_amount"));
        deduction.setHousingRentAmount(rs.getBigDecimal("housing_rent_amount"));
        deduction.setElderlyCareAmount(rs.getBigDecimal("elderly_care_amount"));
        deduction.setInfantCareAmount(rs.getBigDecimal("infant_care_amount"));

        // 从数据库读取计算列的值
        deduction.setTotalAnnualDeduction(rs.getBigDecimal("total_annual_deduction"));
        deduction.setMonthlyDeductionCalculated(rs.getBigDecimal("monthly_deduction_calculated"));

        deduction.setRemarks(rs.getString("remarks"));
        deduction.setCreatedAt(rs.getTimestamp("created_at"));
        deduction.setUpdatedAt(rs.getTimestamp("updated_at"));

        return deduction;
    }
}