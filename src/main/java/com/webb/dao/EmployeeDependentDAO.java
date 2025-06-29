package com.webb.dao;

import com.webb.model.EmployeeDependent;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDependentDAO {

    /**
     * 添加新的被抚养人信息
     * @param dependent 被抚养人对象 (包含需要加密的明文字段)
     * @return 返回生成的记录ID，如果失败则返回-1或抛出异常
     * @throws SQLException SQL执行异常
     */
    int addDependent(EmployeeDependent dependent) throws SQLException;

    /**
     * 更新被抚养人信息
     * @param dependent 被抚养人对象 (包含需要加密的明文字段)
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean updateDependent(EmployeeDependent dependent) throws SQLException;

    /**
     * 根据ID删除被抚养人信息
     * @param id 记录ID
     * @return 删除成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean deleteDependent(int id) throws SQLException;

    /**
     * 根据记录ID查询被抚养人信息
     * @param id 记录ID
     * @return EmployeeDependent 对象 (包含已解密的明文字段)，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    EmployeeDependent findDependentById(int id) throws SQLException;

    /**
     * 根据员工ID和被抚养人身份证号查询信息 (用于检查唯一性)
     * @param employeeId 员工ID
     * @param idCardNumber 明文身份证号
     * @return EmployeeDependent 对象 (包含已解密的明文字段)，未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    EmployeeDependent findDependentByEmployeeAndIdCard(int employeeId, String idCardNumber) throws SQLException;

    /**
     * 根据员工ID查询其所有的被抚养人信息
     * @param employeeId 员工ID
     * @return 该员工的被抚养人列表 (包含已解密的明文字段)
     * @throws SQLException SQL执行异常
     */
    List<EmployeeDependent> findDependentsByEmployeeId(int employeeId) throws SQLException;

    /**
     * (可选) 根据年度专项附加扣除总表ID查询关联的被抚养人信息
     * @param annualDeductionId 年度扣除总表ID
     * @return 关联的被抚养人列表
     * @throws SQLException SQL执行异常
     */
    List<EmployeeDependent> findDependentsByAnnualDeductionId(int annualDeductionId) throws SQLException;
}