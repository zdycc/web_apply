package com.webb.service.impl;

import com.webb.dao.EmployeeDAO;
import com.webb.dao.impl.EmployeeDAOImpl;
import com.webb.model.Employee;
import com.webb.service.EmployeeService;
import com.webb.service.EmployeeServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private EmployeeDAO employeeDAO = new EmployeeDAOImpl();

    // 正则表达式用于数据校验
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{17}(\\d|X)$"); // 简单18位身份证校验
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[3-9]\\d{9}$"); // 简单手机号校验

    @Override
    public boolean addEmployee(Employee employee) throws EmployeeServiceException {
        if (employee == null) {
            throw new EmployeeServiceException("员工信息不能为空。");
        }
        validateEmployeeData(employee, true); // true表示是新增操作，需要检查唯一性

        try {
            // 检查员工编号是否已存在
            if (employeeDAO.findEmployeeByEmployeeNumber(employee.getEmployeeNumber()) != null) {
                throw new EmployeeServiceException("员工编号 '" + employee.getEmployeeNumber() + "' 已存在。");
            }
            // 检查身份证号是否已存在
            if (employeeDAO.findEmployeeByIdCardNumber(employee.getIdCardNumber()) != null) {
                throw new EmployeeServiceException("身份证号已存在。");
            }
            return employeeDAO.addEmployee(employee) > 0;
        } catch (SQLException e) {
            logger.error("添加员工业务逻辑失败: {}", employee.getEmployeeNumber(), e);
            throw new EmployeeServiceException("添加员工失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) throws EmployeeServiceException {
        if (employee == null || employee.getId() <= 0) {
            throw new EmployeeServiceException("无效的员工信息或ID。");
        }
        validateEmployeeData(employee, false); // false表示是更新操作

        try {
            // 检查员工编号是否与其他员工冲突
            Employee existingByNumber = employeeDAO.findEmployeeByEmployeeNumber(employee.getEmployeeNumber());
            if (existingByNumber != null && existingByNumber.getId() != employee.getId()) {
                throw new EmployeeServiceException("员工编号 '" + employee.getEmployeeNumber() + "' 已被其他员工使用。");
            }
            // 检查身份证号是否与其他员工冲突
            Employee existingByIdCard = employeeDAO.findEmployeeByIdCardNumber(employee.getIdCardNumber());
            if (existingByIdCard != null && existingByIdCard.getId() != employee.getId()) {
                throw new EmployeeServiceException("身份证号已被其他员工使用。");
            }
            return employeeDAO.updateEmployee(employee);
        } catch (SQLException e) {
            logger.error("更新员工业务逻辑失败: ID={}", employee.getId(), e);
            throw new EmployeeServiceException("更新员工失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws EmployeeServiceException {
        if (employeeId <= 0) {
            throw new EmployeeServiceException("无效的员工ID。");
        }
        try {
            return employeeDAO.deleteEmployee(employeeId);
        } catch (SQLException e) {
            logger.error("删除员工业务逻辑失败: ID={}", employeeId, e);
            if (e.getSQLState().startsWith("23")) {
                throw new EmployeeServiceException("删除员工失败：该员工可能被其他数据引用（如工资记录、用户账户等）。", e);
            }
            throw new EmployeeServiceException("删除员工失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) throws EmployeeServiceException {
        if (employeeId <= 0) {
            throw new EmployeeServiceException("无效的员工ID。");
        }
        try {
            return employeeDAO.findEmployeeById(employeeId);
        } catch (SQLException e) {
            logger.error("获取员工信息业务逻辑失败: ID={}", employeeId, e);
            throw new EmployeeServiceException("获取员工信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Employee getEmployeeByEmployeeNumber(String employeeNumber) throws EmployeeServiceException {
        if (employeeNumber == null || employeeNumber.trim().isEmpty()) {
            throw new EmployeeServiceException("员工编号不能为空。");
        }
        try {
            return employeeDAO.findEmployeeByEmployeeNumber(employeeNumber);
        } catch (SQLException e) {
            logger.error("按员工编号获取员工信息业务逻辑失败: {}", employeeNumber, e);
            throw new EmployeeServiceException("获取员工信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws EmployeeServiceException {
        try {
            return employeeDAO.findAllEmployees();
        } catch (SQLException e) {
            logger.error("获取所有员工列表业务逻辑失败", e);
            throw new EmployeeServiceException("获取员工列表失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<Employee> getEmployeesByDepartmentId(int departmentId) throws EmployeeServiceException {
        if (departmentId <= 0) {
            throw new EmployeeServiceException("无效的部门ID。");
        }
        try {
            return employeeDAO.findEmployeesByDepartmentId(departmentId);
        } catch (SQLException e) {
            logger.error("按部门ID获取员工列表业务逻辑失败: DeptID={}", departmentId, e);
            throw new EmployeeServiceException("获取员工列表失败：" + e.getMessage(), e);
        }
    }

    private void validateEmployeeData(Employee employee, boolean isNew) throws EmployeeServiceException {
        if (employee.getEmployeeNumber() == null || employee.getEmployeeNumber().trim().isEmpty()) {
            throw new EmployeeServiceException("员工编号不能为空。");
        }
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new EmployeeServiceException("员工姓名不能为空。");
        }
        if (employee.getIdCardNumber() == null || employee.getIdCardNumber().trim().isEmpty()) {
            throw new EmployeeServiceException("身份证号不能为空。");
        }
        if (!ID_CARD_PATTERN.matcher(employee.getIdCardNumber()).matches()) {
            throw new EmployeeServiceException("身份证号格式不正确。");
        }
        if (employee.getPhoneNumber() != null && !employee.getPhoneNumber().isEmpty() &&
                !PHONE_NUMBER_PATTERN.matcher(employee.getPhoneNumber()).matches()) {
            throw new EmployeeServiceException("手机号码格式不正确。");
        }
        if (employee.getDepartmentId() <= 0) {
            throw new EmployeeServiceException("必须选择一个所属部门。");
        }
        if (employee.getHireDate() == null) {
            throw new EmployeeServiceException("入职日期不能为空。");
        }
    }

    @Override
    public List<Employee> searchEmployees(String searchKeyword, Integer departmentId) throws EmployeeServiceException {
        try {
            return employeeDAO.searchEmployees(searchKeyword, departmentId);
        } catch (SQLException e) {
            logger.error("搜索员工业务逻辑失败: keyword={}, deptId={}", searchKeyword, departmentId, e);
            throw new EmployeeServiceException("搜索员工失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Employee desensitizeEmployeeData(Employee employee) {
        if (employee == null) {
            return null;
        }

        if (employee.getName() != null && employee.getName().length() > 1) {
            employee.setName(employee.getName().charAt(0) + "**");
        }

        if (employee.getIdCardNumber() != null && employee.getIdCardNumber().length() == 18) {
            employee.setIdCardNumber(employee.getIdCardNumber().replaceAll("(\\d{3})\\d{12}(\\w{3})", "$1************$2"));
        } else if (employee.getIdCardNumber() != null && employee.getIdCardNumber().length() == 15) {
            employee.setIdCardNumber(employee.getIdCardNumber().replaceAll("(\\d{3})\\d{9}(\\w{3})", "$1*********$2"));
        }

        if (employee.getPhoneNumber() != null && employee.getPhoneNumber().length() == 11) {
            employee.setPhoneNumber(employee.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }

        if (employee.getAddress() != null && employee.getAddress().length() > 10) { // 简单示例
            employee.setAddress(employee.getAddress().substring(0, 5) + "**********");
        }
        return employee;
    }
}