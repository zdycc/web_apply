package com.webb.model;

import java.math.BigDecimal; // 使用BigDecimal处理金额
import java.sql.Timestamp;

public class EmployeeAnnualDeduction {
    private int id;
    private int employeeId; // 关联的员工ID
    private int year; // 扣除所属年度

    private BigDecimal childrenEducationAmount; // 子女教育年度扣除总额
    private BigDecimal continuingEducationAmount; // 继续教育年度扣除总额
    private BigDecimal seriousIllnessMedicalAmount; // 大病医疗年度扣除总额
    private BigDecimal housingLoanInterestAmount; // 住房贷款利息年度扣除总额
    private BigDecimal housingRentAmount; // 住房租金年度扣除总额
    private BigDecimal elderlyCareAmount; // 赡养老人年度扣除总额
    private BigDecimal infantCareAmount; // 3岁以下婴幼儿照护年度扣除总额

    private BigDecimal totalAnnualDeduction; // 年度专项附加扣除总计
    private BigDecimal monthlyDeductionCalculated; // 月度平均扣除额

    private String remarks; // 备注信息

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Employee employee;

    // 构造函数
    public EmployeeAnnualDeduction() {
        // 初始化BigDecimal字段，避免NullPointerException
        this.childrenEducationAmount = BigDecimal.ZERO;
        this.continuingEducationAmount = BigDecimal.ZERO;
        this.seriousIllnessMedicalAmount = BigDecimal.ZERO;
        this.housingLoanInterestAmount = BigDecimal.ZERO;
        this.housingRentAmount = BigDecimal.ZERO;
        this.elderlyCareAmount = BigDecimal.ZERO;
        this.infantCareAmount = BigDecimal.ZERO;
        this.totalAnnualDeduction = BigDecimal.ZERO;
        this.monthlyDeductionCalculated = BigDecimal.ZERO;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getChildrenEducationAmount() {
        return childrenEducationAmount;
    }

    public void setChildrenEducationAmount(BigDecimal childrenEducationAmount) {
        this.childrenEducationAmount = childrenEducationAmount != null ? childrenEducationAmount : BigDecimal.ZERO;
    }

    public BigDecimal getContinuingEducationAmount() {
        return continuingEducationAmount;
    }

    public void setContinuingEducationAmount(BigDecimal continuingEducationAmount) {
        this.continuingEducationAmount = continuingEducationAmount != null ? continuingEducationAmount : BigDecimal.ZERO;
    }

    public BigDecimal getSeriousIllnessMedicalAmount() {
        return seriousIllnessMedicalAmount;
    }

    public void setSeriousIllnessMedicalAmount(BigDecimal seriousIllnessMedicalAmount) {
        this.seriousIllnessMedicalAmount = seriousIllnessMedicalAmount != null ? seriousIllnessMedicalAmount : BigDecimal.ZERO;
    }

    public BigDecimal getHousingLoanInterestAmount() {
        return housingLoanInterestAmount;
    }

    public void setHousingLoanInterestAmount(BigDecimal housingLoanInterestAmount) {
        this.housingLoanInterestAmount = housingLoanInterestAmount != null ? housingLoanInterestAmount : BigDecimal.ZERO;
    }

    public BigDecimal getHousingRentAmount() {
        return housingRentAmount;
    }

    public void setHousingRentAmount(BigDecimal housingRentAmount) {
        this.housingRentAmount = housingRentAmount != null ? housingRentAmount : BigDecimal.ZERO;
    }

    public BigDecimal getElderlyCareAmount() {
        return elderlyCareAmount;
    }

    public void setElderlyCareAmount(BigDecimal elderlyCareAmount) {
        this.elderlyCareAmount = elderlyCareAmount != null ? elderlyCareAmount : BigDecimal.ZERO;
    }

    public BigDecimal getInfantCareAmount() {
        return infantCareAmount;
    }

    public void setInfantCareAmount(BigDecimal infantCareAmount) {
        this.infantCareAmount = infantCareAmount != null ? infantCareAmount : BigDecimal.ZERO;
    }

    public BigDecimal getTotalAnnualDeduction() {
        return totalAnnualDeduction;
    }

    public void setTotalAnnualDeduction(BigDecimal totalAnnualDeduction) {
        this.totalAnnualDeduction = totalAnnualDeduction != null ? totalAnnualDeduction : BigDecimal.ZERO;
    }

    public BigDecimal getMonthlyDeductionCalculated() {
        return monthlyDeductionCalculated;
    }

    public void setMonthlyDeductionCalculated(BigDecimal monthlyDeductionCalculated) {
        this.monthlyDeductionCalculated = monthlyDeductionCalculated != null ? monthlyDeductionCalculated : BigDecimal.ZERO;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "EmployeeAnnualDeduction{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", year=" + year +
                ", totalAnnualDeduction=" + totalAnnualDeduction +
                ", monthlyDeductionCalculated=" + monthlyDeductionCalculated +
                '}';
    }
}