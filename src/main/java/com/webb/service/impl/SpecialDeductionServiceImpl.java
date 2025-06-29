package com.webb.service.impl;

import com.webb.dao.EmployeeAnnualDeductionDAO;
import com.webb.dao.EmployeeDependentDAO;
import com.webb.dao.impl.EmployeeAnnualDeductionDAOImpl;
import com.webb.dao.impl.EmployeeDependentDAOImpl;
import com.webb.model.EmployeeAnnualDeduction;
import com.webb.model.EmployeeDependent;
import com.webb.service.SpecialDeductionService;
import com.webb.service.SpecialDeductionServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SpecialDeductionServiceImpl implements SpecialDeductionService {
    private static final Logger logger = LoggerFactory.getLogger(SpecialDeductionServiceImpl.class);
    private EmployeeAnnualDeductionDAO annualDeductionDAO = new EmployeeAnnualDeductionDAOImpl();
    private EmployeeDependentDAO dependentDAO = new EmployeeDependentDAOImpl();

    // 简单身份证格式校验 (与EmployeeServiceImpl一致)
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{17}(\\d|X|x)$");


    // --- EmployeeAnnualDeduction 相关实现 ---

    @Override
    public boolean saveOrUpdateAnnualDeduction(EmployeeAnnualDeduction deduction) throws SpecialDeductionServiceException {
        if (deduction == null || deduction.getEmployeeId() <= 0 || deduction.getYear() <= 0) {
            throw new SpecialDeductionServiceException("员工ID和年份不能为空。");
        }
        validateAnnualDeductionAmounts(deduction); // 校验各项金额是否合规（例如，不为负数）

        try {
            EmployeeAnnualDeduction existingDeduction = annualDeductionDAO.findAnnualDeductionByEmployeeIdAndYear(deduction.getEmployeeId(), deduction.getYear());
            if (existingDeduction != null) {
                // 更新操作
                deduction.setId(existingDeduction.getId()); // 确保ID正确，用于更新
                return annualDeductionDAO.updateAnnualDeduction(deduction);
            } else {
                // 新增操作
                return annualDeductionDAO.addAnnualDeduction(deduction) > 0;
            }
        } catch (SQLException e) {
            logger.error("保存或更新员工年度专项附加扣除失败: EmployeeID={}, Year={}", deduction.getEmployeeId(), deduction.getYear(), e);
            throw new SpecialDeductionServiceException("操作员工年度专项附加扣除记录失败：" + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeAnnualDeduction getAnnualDeductionById(int id) throws SpecialDeductionServiceException {
        if (id <= 0) {
            throw new SpecialDeductionServiceException("无效的年度扣除记录ID。");
        }
        try {
            return annualDeductionDAO.findAnnualDeductionById(id);
        } catch (SQLException e) {
            logger.error("获取年度专项附加扣除失败: ID={}", id, e);
            throw new SpecialDeductionServiceException("获取年度专项附加扣除记录失败：" + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeAnnualDeduction getAnnualDeductionByEmployeeAndYear(int employeeId, int year) throws SpecialDeductionServiceException {
        if (employeeId <= 0 || year <= 1900) { // 简单年份校验
            throw new SpecialDeductionServiceException("无效的员工ID或年份。");
        }
        try {
            return annualDeductionDAO.findAnnualDeductionByEmployeeIdAndYear(employeeId, year);
        } catch (SQLException e) {
            logger.error("按员工和年份获取年度专项附加扣除失败: EmployeeID={}, Year={}", employeeId, year, e);
            throw new SpecialDeductionServiceException("获取年度专项附加扣除记录失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeAnnualDeduction> getAnnualDeductionsByEmployeeId(int employeeId) throws SpecialDeductionServiceException {
        if (employeeId <= 0) {
            throw new SpecialDeductionServiceException("无效的员工ID。");
        }
        try {
            return annualDeductionDAO.findAnnualDeductionsByEmployeeId(employeeId);
        } catch (SQLException e) {
            logger.error("按员工ID获取年度专项附加扣除列表失败: EmployeeID={}", employeeId, e);
            throw new SpecialDeductionServiceException("获取年度专项附加扣除列表失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteAnnualDeduction(int annualDeductionId) throws SpecialDeductionServiceException {
        if (annualDeductionId <= 0) {
            throw new SpecialDeductionServiceException("无效的年度扣除记录ID。");
        }
        try {
            return annualDeductionDAO.deleteAnnualDeduction(annualDeductionId);
        } catch (SQLException e) {
            logger.error("删除年度扣除记录失败: ID={}", annualDeductionId, e);
            throw new SpecialDeductionServiceException("删除年度扣除记录失败: " + e.getMessage(), e);
        }
    }


    private void validateAnnualDeductionAmounts(EmployeeAnnualDeduction deduction) throws SpecialDeductionServiceException {
        if (deduction.getChildrenEducationAmount().compareTo(BigDecimal.ZERO) < 0 ||
                deduction.getContinuingEducationAmount().compareTo(BigDecimal.ZERO) < 0 ||
                deduction.getSeriousIllnessMedicalAmount().compareTo(BigDecimal.ZERO) < 0 ||
                deduction.getHousingLoanInterestAmount().compareTo(BigDecimal.ZERO) < 0 ||
                deduction.getHousingRentAmount().compareTo(BigDecimal.ZERO) < 0 ||
                deduction.getElderlyCareAmount().compareTo(BigDecimal.ZERO) < 0 ||
                deduction.getInfantCareAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new SpecialDeductionServiceException("各项扣除金额不能为负数。");
        }
    }

    // --- EmployeeDependent 相关实现 ---

    @Override
    public boolean addDependent(EmployeeDependent dependent) throws SpecialDeductionServiceException {
        if (dependent == null || dependent.getEmployeeId() <= 0) {
            throw new SpecialDeductionServiceException("员工ID不能为空。");
        }
        validateDependentData(dependent);

        try {
            // 检查同一员工下身份证号是否已存在
            EmployeeDependent existing = dependentDAO.findDependentByEmployeeAndIdCard(dependent.getEmployeeId(), dependent.getDependentIdCardNumber());
            if (existing != null) {
                throw new SpecialDeductionServiceException("该员工已添加过此身份证号的被抚养人。");
            }
            return dependentDAO.addDependent(dependent) > 0;
        } catch (SQLException e) {
            logger.error("添加被抚养人信息失败: EmployeeID={}", dependent.getEmployeeId(), e);
            throw new SpecialDeductionServiceException("添加被抚养人信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateDependent(EmployeeDependent dependent) throws SpecialDeductionServiceException {
        if (dependent == null || dependent.getId() <= 0 || dependent.getEmployeeId() <= 0) {
            throw new SpecialDeductionServiceException("无效的被抚养人信息或ID。");
        }
        validateDependentData(dependent);

        try {
            // 检查同一员工下身份证号是否与其他被抚养人冲突
            EmployeeDependent existingByIdCard = dependentDAO.findDependentByEmployeeAndIdCard(dependent.getEmployeeId(), dependent.getDependentIdCardNumber());
            if (existingByIdCard != null && existingByIdCard.getId() != dependent.getId()) {
                throw new SpecialDeductionServiceException("该员工名下此身份证号已关联其他被抚养人。");
            }
            return dependentDAO.updateDependent(dependent);
        } catch (SQLException e) {
            logger.error("更新被抚养人信息失败: ID={}", dependent.getId(), e);
            throw new SpecialDeductionServiceException("更新被抚养人信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteDependent(int dependentId) throws SpecialDeductionServiceException {
        if (dependentId <= 0) {
            throw new SpecialDeductionServiceException("无效的被抚养人ID。");
        }
        try {
            return dependentDAO.deleteDependent(dependentId);
        } catch (SQLException e) {
            logger.error("删除被抚养人信息失败: ID={}", dependentId, e);
            throw new SpecialDeductionServiceException("删除被抚养人信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeDependent getDependentById(int dependentId) throws SpecialDeductionServiceException {
        if (dependentId <= 0) {
            throw new SpecialDeductionServiceException("无效的被抚养人ID。");
        }
        try {
            return dependentDAO.findDependentById(dependentId);
        } catch (SQLException e) {
            logger.error("获取被抚养人信息失败: ID={}", dependentId, e);
            throw new SpecialDeductionServiceException("获取被抚养人信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeDependent> getDependentsByEmployeeId(int employeeId) throws SpecialDeductionServiceException {
        if (employeeId <= 0) {
            throw new SpecialDeductionServiceException("无效的员工ID。");
        }
        try {
            List<EmployeeDependent> dependents = dependentDAO.findDependentsByEmployeeId(employeeId);
            List<EmployeeDependent> desensitizedDependents = new ArrayList<>();
            for (EmployeeDependent dep : dependents) {
                desensitizedDependents.add(desensitizeDependentData(dep)); // 创建新列表或修改原对象
            }
            return desensitizedDependents;
        } catch (SQLException e) {
            logger.error("按员工ID获取被抚养人列表失败: EmployeeID={}", employeeId, e);
            throw new SpecialDeductionServiceException("获取被抚养人列表失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeDependent> getDependentsByAnnualDeductionId(int annualDeductionId) throws SpecialDeductionServiceException {
        if (annualDeductionId <= 0) {
            throw new SpecialDeductionServiceException("无效的年度扣除记录ID。");
        }
        try {
            List<EmployeeDependent> dependents = dependentDAO.findDependentsByAnnualDeductionId(annualDeductionId);
            List<EmployeeDependent> desensitizedDependents = new ArrayList<>();
            for (EmployeeDependent dep : dependents) {
                desensitizedDependents.add(desensitizeDependentData(dep));
            }
            return desensitizedDependents;
        } catch (SQLException e) {
            logger.error("按年度扣除ID获取被抚养人列表失败: AnnualDeductionID={}", annualDeductionId, e);
            throw new SpecialDeductionServiceException("获取被抚养人列表失败：" + e.getMessage(), e);
        }
    }

    private void validateDependentData(EmployeeDependent dependent) throws SpecialDeductionServiceException {
        if (dependent.getDependentName() == null || dependent.getDependentName().trim().isEmpty()) {
            throw new SpecialDeductionServiceException("被抚养人姓名不能为空。");
        }
        if (dependent.getDependentIdCardNumber() == null || dependent.getDependentIdCardNumber().trim().isEmpty()) {
            throw new SpecialDeductionServiceException("被抚养人身份证号不能为空。");
        }
        if (!ID_CARD_PATTERN.matcher(dependent.getDependentIdCardNumber()).matches()) {
            throw new SpecialDeductionServiceException("被抚养人身份证号格式不正确。");
        }
        if (dependent.getRelationship() == null || dependent.getRelationship().trim().isEmpty()) {
            throw new SpecialDeductionServiceException("与员工关系不能为空。");
        }
    }

    @Override
    public EmployeeDependent desensitizeDependentData(EmployeeDependent dependent) {
        if (dependent == null) {
            return null;
        }
        if (dependent.getDependentName() != null && dependent.getDependentName().length() > 1) {
            dependent.setDependentName(dependent.getDependentName().charAt(0) + "**");
        }

        if (dependent.getDependentIdCardNumber() != null && dependent.getDependentIdCardNumber().length() >= 15) { // 至少15位才好脱敏
            dependent.setDependentIdCardNumber(dependent.getDependentIdCardNumber().replaceAll("(\\d{3})\\d{9,12}(\\w{3})", "$1***********$2"));
        }
        return dependent;
    }



}