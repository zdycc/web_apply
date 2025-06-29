package com.webb.dao.impl;

import com.webb.dao.EmployeeDAO;
import com.webb.dao.MonthlySalaryDAO;
import com.webb.dao.UserDAO;
import com.webb.model.Department;
import com.webb.model.Employee;
import com.webb.model.MonthlySalary;
import com.webb.model.User;
import com.webb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public class MonthlySalaryDAOImpl implements MonthlySalaryDAO {
    private static final Logger logger = LoggerFactory.getLogger(MonthlySalaryDAOImpl.class);
    private EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public long addMonthlySalary(MonthlySalary salary) throws SQLException {
        String sql = "INSERT INTO monthly_salaries (employee_id, `year_month`, basic_salary, post_allowance, " +
                "lunch_subsidy, overtime_pay, attendance_bonus, other_earnings, total_earnings_manual, " +
                "social_security_personal, provident_fund_personal, special_additional_deduction_monthly, " +
                "enterprise_annuity_personal, other_pre_tax_deductions, total_pre_tax_deductions_manual, " +
                "taxable_income_before_threshold, tax_threshold_amount, taxable_income, personal_income_tax, " +
                "late_leave_deduction, other_post_tax_deductions, net_salary_manual, salary_status, " +
                "calculation_method_notes, remarks, created_by_user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        long generatedId = -1L;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            mapSalaryToStatementForInsert(pstmt, salary);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                    salary.setId(generatedId);
                }
            }
            conn.commit();
            logger.info("月度工资记录添加成功: EmployeeID={}, YearMonth={}", salary.getEmployeeId(), salary.getYearMonth());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("添加月度工资记录失败: EmployeeID={}, YearMonth={}", salary.getEmployeeId(), salary.getYearMonth(), e);
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return generatedId;
    }

    @Override
    public boolean updateMonthlySalary(MonthlySalary salary) throws SQLException {
        String sql = "UPDATE monthly_salaries SET employee_id = ?, `year_month` = ?, basic_salary = ?, " +
                "post_allowance = ?, lunch_subsidy = ?, overtime_pay = ?, attendance_bonus = ?, " +
                "other_earnings = ?, total_earnings_manual = ?, social_security_personal = ?, " +
                "provident_fund_personal = ?, special_additional_deduction_monthly = ?, " +
                "enterprise_annuity_personal = ?, other_pre_tax_deductions = ?, total_pre_tax_deductions_manual = ?, " +
                "taxable_income_before_threshold = ?, tax_threshold_amount = ?, taxable_income = ?, " +
                "personal_income_tax = ?, late_leave_deduction = ?, other_post_tax_deductions = ?, " +
                "net_salary_manual = ?, salary_status = ?, calculation_method_notes = ?, remarks = ?, " +
                "approved_by_user_id = ?, paid_by_user_id = ?, paid_date = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            int paramIndex = 1;
            pstmt.setInt(paramIndex++, salary.getEmployeeId());
            pstmt.setString(paramIndex++, salary.getYearMonth());
            pstmt.setBigDecimal(paramIndex++, salary.getBasicSalary());
            pstmt.setBigDecimal(paramIndex++, salary.getPostAllowance());
            pstmt.setBigDecimal(paramIndex++, salary.getLunchSubsidy());
            pstmt.setBigDecimal(paramIndex++, salary.getOvertimePay());
            pstmt.setBigDecimal(paramIndex++, salary.getAttendanceBonus());
            pstmt.setBigDecimal(paramIndex++, salary.getOtherEarnings());
            pstmt.setBigDecimal(paramIndex++, salary.getTotalEarningsManual());
            pstmt.setBigDecimal(paramIndex++, salary.getSocialSecurityPersonal());
            pstmt.setBigDecimal(paramIndex++, salary.getProvidentFundPersonal());
            pstmt.setBigDecimal(paramIndex++, salary.getSpecialAdditionalDeductionMonthly());
            pstmt.setBigDecimal(paramIndex++, salary.getEnterpriseAnnuityPersonal());
            pstmt.setBigDecimal(paramIndex++, salary.getOtherPreTaxDeductions());
            pstmt.setBigDecimal(paramIndex++, salary.getTotalPreTaxDeductionsManual());
            pstmt.setBigDecimal(paramIndex++, salary.getTaxableIncomeBeforeThreshold());
            pstmt.setBigDecimal(paramIndex++, salary.getTaxThresholdAmount());
            pstmt.setBigDecimal(paramIndex++, salary.getTaxableIncome());
            pstmt.setBigDecimal(paramIndex++, salary.getPersonalIncomeTax());
            pstmt.setBigDecimal(paramIndex++, salary.getLateLeaveDeduction());
            pstmt.setBigDecimal(paramIndex++, salary.getOtherPostTaxDeductions());
            pstmt.setBigDecimal(paramIndex++, salary.getNetSalaryManual());
            pstmt.setString(paramIndex++, salary.getSalaryStatus());
            pstmt.setString(paramIndex++, salary.getCalculationMethodNotes());
            pstmt.setString(paramIndex++, salary.getRemarks());

            setIntegerOrNull(pstmt, paramIndex++, salary.getApprovedByUserId());
            setIntegerOrNull(pstmt, paramIndex++, salary.getPaidByUserId());
            setTimestampOrNull(pstmt, paramIndex++, salary.getPaidDate());
            pstmt.setLong(paramIndex, salary.getId());

            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("月度工资记录更新成功: ID={}", salary.getId());
            else logger.warn("月度工资记录更新失败或未找到: ID={}", salary.getId());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("更新月度工资记录失败: ID={}", salary.getId(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    private void mapSalaryToStatementForInsert(PreparedStatement pstmt, MonthlySalary salary) throws SQLException {
        int i = 1;
        pstmt.setInt(i++, salary.getEmployeeId());
        pstmt.setString(i++, salary.getYearMonth());
        pstmt.setBigDecimal(i++, salary.getBasicSalary());
        pstmt.setBigDecimal(i++, salary.getPostAllowance());
        pstmt.setBigDecimal(i++, salary.getLunchSubsidy());
        pstmt.setBigDecimal(i++, salary.getOvertimePay());
        pstmt.setBigDecimal(i++, salary.getAttendanceBonus());
        pstmt.setBigDecimal(i++, salary.getOtherEarnings());
        pstmt.setBigDecimal(i++, salary.getTotalEarningsManual());
        pstmt.setBigDecimal(i++, salary.getSocialSecurityPersonal());
        pstmt.setBigDecimal(i++, salary.getProvidentFundPersonal());
        pstmt.setBigDecimal(i++, salary.getSpecialAdditionalDeductionMonthly());
        pstmt.setBigDecimal(i++, salary.getEnterpriseAnnuityPersonal());
        pstmt.setBigDecimal(i++, salary.getOtherPreTaxDeductions());
        pstmt.setBigDecimal(i++, salary.getTotalPreTaxDeductionsManual());
        pstmt.setBigDecimal(i++, salary.getTaxableIncomeBeforeThreshold());
        pstmt.setBigDecimal(i++, salary.getTaxThresholdAmount());
        pstmt.setBigDecimal(i++, salary.getTaxableIncome());
        pstmt.setBigDecimal(i++, salary.getPersonalIncomeTax());
        pstmt.setBigDecimal(i++, salary.getLateLeaveDeduction());
        pstmt.setBigDecimal(i++, salary.getOtherPostTaxDeductions());
        pstmt.setBigDecimal(i++, salary.getNetSalaryManual());
        pstmt.setString(i++, salary.getSalaryStatus());
        pstmt.setString(i++, salary.getCalculationMethodNotes());
        pstmt.setString(i++, salary.getRemarks());
        setIntegerOrNull(pstmt, i++, salary.getCreatedByUserId());
    }

    private void setIntegerOrNull(PreparedStatement pstmt, int parameterIndex, Integer value) throws SQLException {
        if (value != null) {
            pstmt.setInt(parameterIndex, value);
        } else {
            pstmt.setNull(parameterIndex, Types.INTEGER);
        }
    }

    private void setTimestampOrNull(PreparedStatement pstmt, int parameterIndex, Timestamp value) throws SQLException {
        if (value != null) {
            pstmt.setTimestamp(parameterIndex, value);
        } else {
            pstmt.setNull(parameterIndex, Types.TIMESTAMP);
        }
    }

    @Override
    public boolean deleteMonthlySalary(long id) throws SQLException {
        String sql = "DELETE FROM monthly_salaries WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            success = pstmt.executeUpdate() > 0;
            conn.commit();
            if(success) logger.info("月度工资记录删除成功: ID={}", id);
            else logger.warn("月度工资记录删除失败或未找到: ID={}", id);
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("删除月度工资记录失败: ID={}", id, e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            DBUtils.close(pstmt, conn);
        }
        return success;
    }

    @Override
    public MonthlySalary findMonthlySalaryById(long id) throws SQLException {
        String sql = "SELECT ms.*, d.dept_name " +
                "FROM monthly_salaries ms " +
                "LEFT JOIN employees e ON ms.employee_id = e.id " +
                "LEFT JOIN departments d ON e.department_id = d.id " +
                "WHERE ms.id = ?";
        List<MonthlySalary> salaries = findSalariesWithJoinsBySql(sql, id);
        return salaries.isEmpty() ? null : salaries.get(0);
    }

    @Override
    public MonthlySalary findMonthlySalaryByEmployeeAndYearMonth(int employeeId, String yearMonth) throws SQLException {
        String sql = "SELECT ms.*, d.dept_name " +
                "FROM monthly_salaries ms " +
                "LEFT JOIN employees e ON ms.employee_id = e.id " +
                "LEFT JOIN departments d ON e.department_id = d.id " +
                "WHERE ms.employee_id = ? AND ms.`year_month` = ?";
        List<MonthlySalary> salaries = findSalariesWithJoinsBySql(sql, employeeId, yearMonth);
        return salaries.isEmpty() ? null : salaries.get(0);
    }

    @Override
    public List<MonthlySalary> findMonthlySalariesByEmployeeId(int employeeId) throws SQLException {
        String sql = "SELECT ms.*, d.dept_name " +
                "FROM monthly_salaries ms " +
                "LEFT JOIN employees e ON ms.employee_id = e.id " +
                "LEFT JOIN departments d ON e.department_id = d.id " +
                "WHERE ms.employee_id = ? ORDER BY ms.`year_month` DESC";
        return findSalariesWithJoinsBySql(sql, employeeId);
    }

    @Override
    public List<MonthlySalary> findMonthlySalariesByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT ms.*, d.dept_name FROM monthly_salaries ms ");
        sqlBuilder.append("LEFT JOIN employees e ON ms.employee_id = e.id ");
        sqlBuilder.append("LEFT JOIN departments d ON e.department_id = d.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (filters != null) {
            String yearMonthExact = (String) filters.get("yearMonthExact");
            if (yearMonthExact != null && !yearMonthExact.isEmpty()) {
                sqlBuilder.append(" AND ms.`year_month` = ?");
                params.add(yearMonthExact);
            }
            String yearMonthStart = (String) filters.get("yearMonthStart");
            if (yearMonthStart != null && !yearMonthStart.isEmpty()) {
                sqlBuilder.append(" AND ms.`year_month` >= ?");
                params.add(yearMonthStart);
            }
            String yearMonthEnd = (String) filters.get("yearMonthEnd");
            if (yearMonthEnd != null && !yearMonthEnd.isEmpty()) {
                sqlBuilder.append(" AND ms.`year_month` <= ?");
                params.add(yearMonthEnd);
            }
            Integer departmentId = (Integer) filters.get("departmentId");
            if (departmentId != null && departmentId > 0) {
                sqlBuilder.append(" AND e.department_id = ?");
                params.add(departmentId);
            }
            String employeeNumber = (String) filters.get("employeeNumber");
            if (employeeNumber != null && !employeeNumber.isEmpty()) {
                sqlBuilder.append(" AND e.employee_number = ?");
                params.add(employeeNumber);
            }
        }

        if (orderBy != null && !orderBy.trim().isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(orderBy.replace("year_month", "`year_month`"));
        } else {
            sqlBuilder.append(" ORDER BY ms.`year_month` DESC, e.employee_number ASC");
        }

        if (pageNum > 0 && pageSize > 0) {
            sqlBuilder.append(" LIMIT ? OFFSET ?");
            params.add(pageSize);
            params.add((pageNum - 1) * pageSize);
        }

        return findSalariesWithJoinsBySql(sqlBuilder.toString(), params.toArray());
    }

    @Override
    public int countMonthlySalariesByFilters(Map<String, Object> filters) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(ms.id) FROM monthly_salaries ms ");
        sqlBuilder.append("LEFT JOIN employees e ON ms.employee_id = e.id ");
        sqlBuilder.append("LEFT JOIN departments d ON e.department_id = d.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (filters != null) {
            String yearMonthExact = (String) filters.get("yearMonthExact");
            if (yearMonthExact != null && !yearMonthExact.isEmpty()) {
                sqlBuilder.append(" AND ms.`year_month` = ?");
                params.add(yearMonthExact);
            }
            String yearMonthStart = (String) filters.get("yearMonthStart");
            if (yearMonthStart != null && !yearMonthStart.isEmpty()) {
                sqlBuilder.append(" AND ms.`year_month` >= ?");
                params.add(yearMonthStart);
            }
            String yearMonthEnd = (String) filters.get("yearMonthEnd");
            if (yearMonthEnd != null && !yearMonthEnd.isEmpty()) {
                sqlBuilder.append(" AND ms.`year_month` <= ?");
                params.add(yearMonthEnd);
            }
            Integer departmentId = (Integer) filters.get("departmentId");
            if (departmentId != null && departmentId > 0) {
                sqlBuilder.append(" AND e.department_id = ?");
                params.add(departmentId);
            }
            String employeeNumber = (String) filters.get("employeeNumber");
            if (employeeNumber != null && !employeeNumber.isEmpty()) {
                sqlBuilder.append(" AND e.employee_number = ?");
                params.add(employeeNumber);
            }
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("统计月度工资记录总数失败, SQL (部分): {}", sqlBuilder.toString().split(" WHERE")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return count;
    }

    private List<MonthlySalary> findSalariesWithJoinsBySql(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MonthlySalary> salaries = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();
            if (conn == null) throw new SQLException("无法获取数据库连接");
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                salaries.add(mapRowToMonthlySalary(rs, true));
            }
        } catch (SQLException e) {
            logger.error("查询月度工资记录列表失败 (带JOIN), SQL (部分): {}", sql.split(" WHERE")[0], e);
            throw e;
        } finally {
            DBUtils.close(rs, pstmt, conn);
        }
        return salaries;
    }

    private MonthlySalary mapRowToMonthlySalary(ResultSet rs, boolean loadJoinedEntities) throws SQLException {
        MonthlySalary salary = new MonthlySalary();
        salary.setId(rs.getLong("id"));
        salary.setEmployeeId(rs.getInt("employee_id"));
        salary.setYearMonth(rs.getString("year_month"));
        salary.setBasicSalary(rs.getBigDecimal("basic_salary"));
        salary.setPostAllowance(rs.getBigDecimal("post_allowance"));
        salary.setLunchSubsidy(rs.getBigDecimal("lunch_subsidy"));
        salary.setOvertimePay(rs.getBigDecimal("overtime_pay"));
        salary.setAttendanceBonus(rs.getBigDecimal("attendance_bonus"));
        salary.setOtherEarnings(rs.getBigDecimal("other_earnings"));
        salary.setTotalEarningsManual(rs.getBigDecimal("total_earnings_manual"));
        salary.setSocialSecurityPersonal(rs.getBigDecimal("social_security_personal"));
        salary.setProvidentFundPersonal(rs.getBigDecimal("provident_fund_personal"));
        salary.setSpecialAdditionalDeductionMonthly(rs.getBigDecimal("special_additional_deduction_monthly"));
        salary.setEnterpriseAnnuityPersonal(rs.getBigDecimal("enterprise_annuity_personal"));
        salary.setOtherPreTaxDeductions(rs.getBigDecimal("other_pre_tax_deductions"));
        salary.setTotalPreTaxDeductionsManual(rs.getBigDecimal("total_pre_tax_deductions_manual"));
        salary.setTaxableIncomeBeforeThreshold(rs.getBigDecimal("taxable_income_before_threshold"));
        salary.setTaxThresholdAmount(rs.getBigDecimal("tax_threshold_amount"));
        salary.setTaxableIncome(rs.getBigDecimal("taxable_income"));
        salary.setPersonalIncomeTax(rs.getBigDecimal("personal_income_tax"));
        salary.setLateLeaveDeduction(rs.getBigDecimal("late_leave_deduction"));
        salary.setOtherPostTaxDeductions(rs.getBigDecimal("other_post_tax_deductions"));
        salary.setNetSalaryManual(rs.getBigDecimal("net_salary_manual"));
        salary.setSalaryStatus(rs.getString("salary_status"));
        salary.setCalculationMethodNotes(rs.getString("calculation_method_notes"));
        salary.setRemarks(rs.getString("remarks"));
        salary.setCreatedByUserId(rs.getObject("created_by_user_id", Integer.class));
        salary.setApprovedByUserId(rs.getObject("approved_by_user_id", Integer.class));
        salary.setPaidByUserId(rs.getObject("paid_by_user_id", Integer.class));
        salary.setPaidDate(rs.getTimestamp("paid_date"));
        salary.setCreatedAt(rs.getTimestamp("created_at"));
        salary.setUpdatedAt(rs.getTimestamp("updated_at"));

        if (loadJoinedEntities) {
            try {
                Employee employee = employeeDAO.findEmployeeById(salary.getEmployeeId());
                if (employee != null) {
                    try {
                        String deptNameFromJoin = rs.getString("dept_name");
                        if (deptNameFromJoin != null) {
                            if (employee.getDepartment() == null) {
                                Department dept = new Department();
                                dept.setDeptName(deptNameFromJoin);
                                employee.setDepartment(dept);
                            } else {
                                employee.getDepartment().setDeptName(deptNameFromJoin);
                            }
                        }
                    } catch (SQLException e) {
                        logger.trace("未能从JOIN的ResultSet中获取dept_name (SalaryID: {})", salary.getId());
                    }
                }
                salary.setEmployee(employee);
            } catch (SQLException e) {
                logger.warn("加载关联员工信息失败 for salary ID: {}", salary.getId(), e);
            }

            if (salary.getCreatedByUserId() != null && salary.getCreatedByUserId() > 0) {
                try {
                    User createdBy = userDAO.findById(salary.getCreatedByUserId());
                    salary.setCreatedByUser(createdBy);
                } catch (SQLException e) {
                    logger.warn("加载创建人信息失败 for salary ID: {}, UserID: {}", salary.getId(), salary.getCreatedByUserId(), e);
                }
            }
            if (salary.getApprovedByUserId() != null && salary.getApprovedByUserId() > 0) {
                try {
                    User approvedBy = userDAO.findById(salary.getApprovedByUserId());
                    salary.setApprovedByUser(approvedBy);
                } catch (SQLException e) {
                    logger.warn("加载审核人信息失败 for salary ID: {}, UserID: {}", salary.getId(), salary.getApprovedByUserId(), e);
                }
            }
            if (salary.getPaidByUserId() != null && salary.getPaidByUserId() > 0) {
                try {
                    User paidBy = userDAO.findById(salary.getPaidByUserId());
                    salary.setPaidByUser(paidBy);
                } catch (SQLException e) {
                    logger.warn("加载支付人信息失败 for salary ID: {}, UserID: {}", salary.getId(), salary.getPaidByUserId(), e);
                }
            }
        }
        return salary;
    }
}