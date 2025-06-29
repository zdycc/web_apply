package com.webb.model;

import java.sql.Date; // 用于入职日期
import java.sql.Timestamp;

public class Employee {
    private int id;
    private String employeeNumber; // 员工编号 (唯一)
    private String name; // 姓名
    private String idCardNumber; // 身份证号 (唯一)
    private String phoneNumber; // 手机号
    private String address; // 住址

    // 非加密字段
    private int departmentId; // 所属部门ID
    private String position; // 岗位 (例如：销售代表, 客服专员, 财务会计)
    private String jobTitle; // 职务 (例如：经理, 主管, 普通职员)
    private Date hireDate; // 入职日期
    private String status; // 员工状态 (例如：active, resigned, on_leave) - 可考虑枚举或固定值
    private Department department; // 员工所属部门对象

    // 时间戳
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 构造函数
    public Employee() {}

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    // toString 方法 (不要直接打印敏感信息)
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", name='[PROTECTED]'" + // 姓名脱敏或不显示
                ", departmentId=" + departmentId +
                ", position='" + position + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", hireDate=" + hireDate +
                ", status='" + status + '\'' +
                '}';
    }
}