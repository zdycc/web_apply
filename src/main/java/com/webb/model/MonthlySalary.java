package com.webb.model;

import java.math.BigDecimal;
import java.sql.Timestamp; // 用于时间戳
import java.time.YearMonth; // 用于表示工资月份

public class MonthlySalary {
    private Long id;
    private int employeeId;
    private String yearMonth; // 格式 "YYYY-MM"
    private BigDecimal shouldAttendDays; // 本月应出勤天数
    private BigDecimal actualAttendDays; // 实际出勤天数

    // 应发项目
    private BigDecimal basicSalary;
    private BigDecimal postAllowance;
    private BigDecimal lunchSubsidy;
    private BigDecimal overtimePay;
    private BigDecimal attendanceBonus;
    private BigDecimal otherEarnings;
    private BigDecimal totalEarningsManual; // 应发工资合计

    // 税前扣除项目
    private BigDecimal socialSecurityPersonal;
    private BigDecimal providentFundPersonal;
    private BigDecimal specialAdditionalDeductionMonthly;
    private BigDecimal enterpriseAnnuityPersonal;
    private BigDecimal otherPreTaxDeductions;
    private BigDecimal totalPreTaxDeductionsManual; // 税前扣除总额

    // 应纳税所得额相关
    private BigDecimal taxableIncomeBeforeThreshold;
    private BigDecimal taxThresholdAmount;
    private BigDecimal taxableIncome; // 应纳税所得额 (应用层计算或从数据库读取)

    // 税款及税后扣除
    private BigDecimal personalIncomeTax;
    private BigDecimal lateLeaveDeduction;
    private BigDecimal otherPostTaxDeductions;

    // 实发工资
    private BigDecimal netSalaryManual; // 实发工资

    // 状态与记录信息
    private String salaryStatus;
    private String calculationMethodNotes;
    private String remarks;
    private Integer createdByUserId;
    private Integer approvedByUserId;
    private Integer paidByUserId;
    private Timestamp paidDate; // 使用Timestamp以便包含时间
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Employee employee;
    private User createdByUser;
    private User approvedByUser;
    private User paidByUser;

    // 构造函数
    public MonthlySalary() {
        // 初始化BigDecimal字段为0，避免NullPointerException
        this.basicSalary = BigDecimal.ZERO;
        this.postAllowance = BigDecimal.ZERO;
        this.lunchSubsidy = BigDecimal.ZERO;
        this.overtimePay = BigDecimal.ZERO;
        this.attendanceBonus = BigDecimal.ZERO;
        this.otherEarnings = BigDecimal.ZERO;
        this.totalEarningsManual = BigDecimal.ZERO;
        this.socialSecurityPersonal = BigDecimal.ZERO;
        this.providentFundPersonal = BigDecimal.ZERO;
        this.specialAdditionalDeductionMonthly = BigDecimal.ZERO;
        this.enterpriseAnnuityPersonal = BigDecimal.ZERO;
        this.otherPreTaxDeductions = BigDecimal.ZERO;
        this.totalPreTaxDeductionsManual = BigDecimal.ZERO;
        this.taxableIncomeBeforeThreshold = BigDecimal.ZERO;
        this.taxThresholdAmount = new BigDecimal("5000.00"); // 默认当前起征点
        this.taxableIncome = BigDecimal.ZERO;
        this.personalIncomeTax = BigDecimal.ZERO;
        this.lateLeaveDeduction = BigDecimal.ZERO;
        this.otherPostTaxDeductions = BigDecimal.ZERO;
        this.netSalaryManual = BigDecimal.ZERO;
        this.salaryStatus = "DRAFT"; // 默认状态
        this.shouldAttendDays = BigDecimal.ZERO;
        this.actualAttendDays = BigDecimal.ZERO;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    // 为新字段添加 Getter 和 Setter
    public BigDecimal getShouldAttendDays() {
        return shouldAttendDays;
    }

    public void setShouldAttendDays(BigDecimal shouldAttendDays) {
        this.shouldAttendDays = (shouldAttendDays != null) ? shouldAttendDays : BigDecimal.ZERO;
    }

    public BigDecimal getActualAttendDays() {
        return actualAttendDays;
    }

    public void setActualAttendDays(BigDecimal actualAttendDays) {
        this.actualAttendDays = (actualAttendDays != null) ? actualAttendDays : BigDecimal.ZERO;
    }

    // --- 应发项目 Getters and Setters ---
    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = (basicSalary != null) ? basicSalary : BigDecimal.ZERO;
    }

    public BigDecimal getPostAllowance() {
        return postAllowance;
    }

    public void setPostAllowance(BigDecimal postAllowance) {
        this.postAllowance = (postAllowance != null) ? postAllowance : BigDecimal.ZERO;
    }

    public BigDecimal getLunchSubsidy() {
        return lunchSubsidy;
    }

    public void setLunchSubsidy(BigDecimal lunchSubsidy) {
        this.lunchSubsidy = (lunchSubsidy != null) ? lunchSubsidy : BigDecimal.ZERO;
    }

    public BigDecimal getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = (overtimePay != null) ? overtimePay : BigDecimal.ZERO;
    }

    public BigDecimal getAttendanceBonus() {
        return attendanceBonus;
    }

    public void setAttendanceBonus(BigDecimal attendanceBonus) {
        this.attendanceBonus = (attendanceBonus != null) ? attendanceBonus : BigDecimal.ZERO;
    }

    public BigDecimal getOtherEarnings() {
        return otherEarnings;
    }

    public void setOtherEarnings(BigDecimal otherEarnings) {
        this.otherEarnings = (otherEarnings != null) ? otherEarnings : BigDecimal.ZERO;
    }

    public BigDecimal getTotalEarningsManual() {
        return totalEarningsManual;
    }

    public void setTotalEarningsManual(BigDecimal totalEarningsManual) {
        this.totalEarningsManual = (totalEarningsManual != null) ? totalEarningsManual : BigDecimal.ZERO;
    }

    // --- 税前扣除项目 Getters and Setters ---
    public BigDecimal getSocialSecurityPersonal() {
        return socialSecurityPersonal;
    }

    public void setSocialSecurityPersonal(BigDecimal socialSecurityPersonal) {
        this.socialSecurityPersonal = (socialSecurityPersonal != null) ? socialSecurityPersonal : BigDecimal.ZERO;
    }

    public BigDecimal getProvidentFundPersonal() {
        return providentFundPersonal;
    }

    public void setProvidentFundPersonal(BigDecimal providentFundPersonal) {
        this.providentFundPersonal = (providentFundPersonal != null) ? providentFundPersonal : BigDecimal.ZERO;
    }

    public BigDecimal getSpecialAdditionalDeductionMonthly() {
        return specialAdditionalDeductionMonthly;
    }

    public void setSpecialAdditionalDeductionMonthly(BigDecimal specialAdditionalDeductionMonthly) {
        this.specialAdditionalDeductionMonthly = (specialAdditionalDeductionMonthly != null) ? specialAdditionalDeductionMonthly : BigDecimal.ZERO;
    }

    public BigDecimal getEnterpriseAnnuityPersonal() {
        return enterpriseAnnuityPersonal;
    }

    public void setEnterpriseAnnuityPersonal(BigDecimal enterpriseAnnuityPersonal) {
        this.enterpriseAnnuityPersonal = (enterpriseAnnuityPersonal != null) ? enterpriseAnnuityPersonal : BigDecimal.ZERO;
    }

    public BigDecimal getOtherPreTaxDeductions() {
        return otherPreTaxDeductions;
    }

    public void setOtherPreTaxDeductions(BigDecimal otherPreTaxDeductions) {
        this.otherPreTaxDeductions = (otherPreTaxDeductions != null) ? otherPreTaxDeductions : BigDecimal.ZERO;
    }

    public BigDecimal getTotalPreTaxDeductionsManual() {
        return totalPreTaxDeductionsManual;
    }

    public void setTotalPreTaxDeductionsManual(BigDecimal totalPreTaxDeductionsManual) {
        this.totalPreTaxDeductionsManual = (totalPreTaxDeductionsManual != null) ? totalPreTaxDeductionsManual : BigDecimal.ZERO;
    }

    // --- 应纳税所得额相关 Getters and Setters ---
    public BigDecimal getTaxableIncomeBeforeThreshold() {
        return taxableIncomeBeforeThreshold;
    }

    public void setTaxableIncomeBeforeThreshold(BigDecimal taxableIncomeBeforeThreshold) {
        this.taxableIncomeBeforeThreshold = (taxableIncomeBeforeThreshold != null) ? taxableIncomeBeforeThreshold : BigDecimal.ZERO;
    }

    public BigDecimal getTaxThresholdAmount() {
        return taxThresholdAmount;
    }

    public void setTaxThresholdAmount(BigDecimal taxThresholdAmount) {
        this.taxThresholdAmount = (taxThresholdAmount != null) ? taxThresholdAmount : BigDecimal.ZERO;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = (taxableIncome != null) ? taxableIncome : BigDecimal.ZERO;
    }

    // --- 税款及税后扣除 Getters and Setters ---
    public BigDecimal getPersonalIncomeTax() {
        return personalIncomeTax;
    }

    public void setPersonalIncomeTax(BigDecimal personalIncomeTax) {
        this.personalIncomeTax = (personalIncomeTax != null) ? personalIncomeTax : BigDecimal.ZERO;
    }

    public BigDecimal getLateLeaveDeduction() {
        return lateLeaveDeduction;
    }

    public void setLateLeaveDeduction(BigDecimal lateLeaveDeduction) {
        this.lateLeaveDeduction = (lateLeaveDeduction != null) ? lateLeaveDeduction : BigDecimal.ZERO;
    }

    public BigDecimal getOtherPostTaxDeductions() {
        return otherPostTaxDeductions;
    }

    public void setOtherPostTaxDeductions(BigDecimal otherPostTaxDeductions) {
        this.otherPostTaxDeductions = (otherPostTaxDeductions != null) ? otherPostTaxDeductions : BigDecimal.ZERO;
    }

    // --- 实发工资 Getters and Setters ---
    public BigDecimal getNetSalaryManual() {
        return netSalaryManual;
    }

    public void setNetSalaryManual(BigDecimal netSalaryManual) {
        this.netSalaryManual = (netSalaryManual != null) ? netSalaryManual : BigDecimal.ZERO;
    }

    // --- 状态与记录信息 Getters and Setters ---
    public String getSalaryStatus() {
        return salaryStatus;
    }

    public void setSalaryStatus(String salaryStatus) {
        this.salaryStatus = salaryStatus;
    }

    public String getCalculationMethodNotes() {
        return calculationMethodNotes;
    }

    public void setCalculationMethodNotes(String calculationMethodNotes) {
        this.calculationMethodNotes = calculationMethodNotes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Integer getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(Integer approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public Integer getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(Integer paidByUserId) {
        this.paidByUserId = paidByUserId;
    }

    public Timestamp getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Timestamp paidDate) {
        this.paidDate = paidDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- 关联对象 Getters and Setters ---
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public User getApprovedByUser() {
        return approvedByUser;
    }

    public void setApprovedByUser(User approvedByUser) {
        this.approvedByUser = approvedByUser;
    }

    public User getPaidByUser() {
        return paidByUser;
    }

    public void setPaidByUser(User paidByUser) {
        this.paidByUser = paidByUser;
    }

    @Override
    public String toString() {
        return "MonthlySalary{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", yearMonth='" + yearMonth + '\'' +
                ", totalEarningsManual=" + totalEarningsManual +
                ", personalIncomeTax=" + personalIncomeTax +
                ", netSalaryManual=" + netSalaryManual +
                ", salaryStatus='" + salaryStatus + '\'' +
                '}';
    }
}