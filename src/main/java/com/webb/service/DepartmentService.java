package com.webb.service;

import com.webb.model.Department;
import java.util.List;

public interface DepartmentService {
    boolean addDepartment(Department department) throws DepartmentServiceException;
    boolean updateDepartment(Department department) throws DepartmentServiceException;
    boolean deleteDepartment(int departmentId) throws DepartmentServiceException;
    Department getDepartmentById(int departmentId) throws DepartmentServiceException;
    List<Department> getAllDepartments() throws DepartmentServiceException;
    List<Department> getDepartmentsByParentId(Integer parentId) throws DepartmentServiceException;
}

