package com.webb.model;

import java.sql.Timestamp;

public class Department {
    private int id;
    private String deptName; // 部门名称
    private Integer parentDeptId; // 上级部门ID
    private Department parentDepartment; // 上级部门对象 (可选)
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 构造函数
    public Department() {}

    // Getter 和 Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public Integer getParentDeptId() { return parentDeptId; }
    public void setParentDeptId(Integer parentDeptId) { this.parentDeptId = parentDeptId; }

    public Department getParentDepartment() { return parentDepartment; }
    public void setParentDepartment(Department parentDepartment) { this.parentDepartment = parentDepartment; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", deptName='" + deptName + '\'' +
                ", parentDeptId=" + parentDeptId +
                '}';
    }
}