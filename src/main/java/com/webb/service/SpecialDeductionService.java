package com.webb.service;

import com.webb.model.EmployeeAnnualDeduction;
import com.webb.model.EmployeeDependent;
import java.util.List;

public interface SpecialDeductionService {

    // --- EmployeeAnnualDeduction 相关操作 ---

    /**
     * 添加或更新员工的年度专项附加扣除记录。
     * 如果记录已存在（根据员工ID和年份），则更新；否则，添加新记录。
     * @param deduction 包含扣除信息的对象
     * @return 操作成功返回 true，否则 false
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    boolean saveOrUpdateAnnualDeduction(EmployeeAnnualDeduction deduction) throws SpecialDeductionServiceException;

    /**
     * 根据记录ID获取年度专项附加扣除信息
     * @param id 记录ID
     * @return EmployeeAnnualDeduction 对象，未找到则返回 null
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    EmployeeAnnualDeduction getAnnualDeductionById(int id) throws SpecialDeductionServiceException;

    /**
     * 根据员工ID和年份获取年度专项附加扣除信息
     * @param employeeId 员工ID
     * @param year 年份
     * @return EmployeeAnnualDeduction 对象，未找到则返回 null
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    EmployeeAnnualDeduction getAnnualDeductionByEmployeeAndYear(int employeeId, int year) throws SpecialDeductionServiceException;

    /**
     * 根据员工ID获取其所有的年度专项附加扣除记录
     * @param employeeId 员工ID
     * @return 该员工的扣除记录列表
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    List<EmployeeAnnualDeduction> getAnnualDeductionsByEmployeeId(int employeeId) throws SpecialDeductionServiceException;

    /**
     * 根据ID删除年度专项附加扣除记录。
     * (可选：同时删除关联的被抚养人信息，或解除关联，具体取决于业务逻辑)
     * @param annualDeductionId 记录ID
     * @return 删除成功返回 true，否则 false
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    boolean deleteAnnualDeduction(int annualDeductionId) throws SpecialDeductionServiceException;


    // --- EmployeeDependent 相关操作 ---

    /**
     * 为指定员工添加被抚养人信息
     * @param dependent 被抚养人对象
     * @return 添加成功返回 true，否则 false
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    boolean addDependent(EmployeeDependent dependent) throws SpecialDeductionServiceException;

    /**
     * 更新被抚养人信息
     * @param dependent 被抚养人对象
     * @return 更新成功返回 true，否则 false
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    boolean updateDependent(EmployeeDependent dependent) throws SpecialDeductionServiceException;

    /**
     * 根据ID删除被抚养人信息
     * @param dependentId 被抚养人记录ID
     * @return 删除成功返回 true，否则 false
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    boolean deleteDependent(int dependentId) throws SpecialDeductionServiceException;

    /**
     * 根据ID获取被抚养人信息
     * @param dependentId 被抚养人记录ID
     * @return EmployeeDependent 对象，未找到则返回 null
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    EmployeeDependent getDependentById(int dependentId) throws SpecialDeductionServiceException;

    /**
     * 根据员工ID获取其所有被抚养人信息列表
     * @param employeeId 员工ID
     * @return 被抚养人列表，包含脱敏后的敏感信息
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    List<EmployeeDependent> getDependentsByEmployeeId(int employeeId) throws SpecialDeductionServiceException;

    /**
     * （可选）根据年度扣除记录ID获取关联的被抚养人信息列表
     * @param annualDeductionId 年度扣除记录ID
     * @return 被抚养人列表
     * @throws SpecialDeductionServiceException 业务逻辑异常
     */
    List<EmployeeDependent> getDependentsByAnnualDeductionId(int annualDeductionId) throws SpecialDeductionServiceException;


    /**
     * 对被抚养人敏感数据进行脱敏处理
     * @param dependent 原始被抚养人对象
     * @return 脱敏后的被抚养人对象副本（或直接修改原对象）
     */
    EmployeeDependent desensitizeDependentData(EmployeeDependent dependent);
}

