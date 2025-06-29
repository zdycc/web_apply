package com.webb.model;

import java.sql.Date; // 用于出生日期
import java.sql.Timestamp;

public class EmployeeDependent {
    private int id;
    private int employeeId; // 关联的员工ID
    private Integer annualDeductionId; // 关联的年度专项附加扣除总表ID

    // 以下字段将SM4加密存储，在模型中仍然使用 String
    private String dependentName; // 被抚养人姓名
    private String dependentIdCardNumber; // 被抚养人身份证号

    private String relationship; // 与员工关系
    private String deductionTypeInvolved; // 涉及的扣除项目 (例如: "子女教育,婴幼儿照护")
    private Date birthDate; // 被抚养人出生日期
    private String notes; // 备注

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Employee employee;

    // 构造函数
    public EmployeeDependent() {}

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

    public Integer getAnnualDeductionId() {
        return annualDeductionId;
    }

    public void setAnnualDeductionId(Integer annualDeductionId) {
        this.annualDeductionId = annualDeductionId;
    }

    public String getDependentName() {
        return dependentName;
    }

    public void setDependentName(String dependentName) {
        this.dependentName = dependentName;
    }

    public String getDependentIdCardNumber() {
        return dependentIdCardNumber;
    }

    public void setDependentIdCardNumber(String dependentIdCardNumber) {
        this.dependentIdCardNumber = dependentIdCardNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDeductionTypeInvolved() {
        return deductionTypeInvolved;
    }

    public void setDeductionTypeInvolved(String deductionTypeInvolved) {
        this.deductionTypeInvolved = deductionTypeInvolved;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "EmployeeDependent{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", dependentName='[PROTECTED]'" + // 脱敏或不显示
                ", relationship='" + relationship + '\'' +
                '}';
    }
}