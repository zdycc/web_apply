package com.webb.service.impl;

import com.webb.dao.EmployeeAnnualDeductionDAO;
import com.webb.dao.MonthlySalaryDAO;
import com.webb.dao.impl.EmployeeAnnualDeductionDAOImpl;
import com.webb.dao.impl.MonthlySalaryDAOImpl;
import com.webb.model.Employee;
import com.webb.model.EmployeeAnnualDeduction;
import com.webb.model.MonthlySalary;
import com.webb.service.EmployeeService;
import com.webb.service.EmployeeServiceException;
import com.webb.service.SalaryService;
import com.webb.service.SalaryServiceException;
import org.apache.poi.ss.usermodel.*; // 导入 POI核心包
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SalaryServiceImpl implements SalaryService {
    private static final Logger logger = LoggerFactory.getLogger(SalaryServiceImpl.class);
    private MonthlySalaryDAO monthlySalaryDAO = new MonthlySalaryDAOImpl();
    private EmployeeAnnualDeductionDAO annualDeductionDAO = new EmployeeAnnualDeductionDAOImpl();
    private EmployeeService employeeService = new EmployeeServiceImpl();

    private static final BigDecimal TAX_THRESHOLD = new BigDecimal("5000.00");
    private static final BigDecimal[] ANNUAL_TAXABLE_INCOME_LEVELS = {
            new BigDecimal("36000"), new BigDecimal("144000"), new BigDecimal("300000"),
            new BigDecimal("420000"), new BigDecimal("660000"), new BigDecimal("960000"),
            BigDecimal.valueOf(Long.MAX_VALUE)
    };
    private static final BigDecimal[] TAX_RATES = {
            new BigDecimal("0.03"), new BigDecimal("0.10"), new BigDecimal("0.20"),
            new BigDecimal("0.25"), new BigDecimal("0.30"), new BigDecimal("0.35"),
            new BigDecimal("0.45")
    };
    private static final BigDecimal[] QUICK_DEDUCTIONS_ANNUAL = {
            BigDecimal.ZERO, new BigDecimal("2520"), new BigDecimal("16920"),
            new BigDecimal("31920"), new BigDecimal("52920"), new BigDecimal("85920"),
            new BigDecimal("181920")
    };

    @Override
    public MonthlySalary saveOrUpdateMonthlySalary(MonthlySalary salary) throws SalaryServiceException {
        if (salary == null || salary.getEmployeeId() <= 0 || salary.getYearMonth() == null || salary.getYearMonth().isEmpty()) {
            throw new SalaryServiceException("员工ID和工资月份不能为空。");
        }
        validateSalaryInput(salary);
        MonthlySalary calculatedSalary = calculateSalaryComponents(salary);
        try {
            MonthlySalary existingSalary = monthlySalaryDAO.findMonthlySalaryByEmployeeAndYearMonth(
                    calculatedSalary.getEmployeeId(), calculatedSalary.getYearMonth());
            boolean success;
            if (existingSalary != null) {
                calculatedSalary.setId(existingSalary.getId());
                calculatedSalary.setCreatedByUserId(existingSalary.getCreatedByUserId());
                calculatedSalary.setCreatedAt(existingSalary.getCreatedAt());
                success = monthlySalaryDAO.updateMonthlySalary(calculatedSalary);
            } else {
                success = monthlySalaryDAO.addMonthlySalary(calculatedSalary) > 0;
            }
            if (success) {
                return calculatedSalary;
            } else {
                throw new SalaryServiceException("保存或更新工资记录失败，可能是记录未找到或数据无变化。");
            }
        } catch (SQLException e) {
            logger.error("保存或更新月度工资失败: EmployeeID={}, YearMonth={}", calculatedSalary.getEmployeeId(), calculatedSalary.getYearMonth(), e);
            throw new SalaryServiceException("保存或更新月度工资失败：" + e.getMessage(), e);
        }
    }

    @Override
    public MonthlySalary calculateSalaryComponents(long salaryId) throws SalaryServiceException {
        try {
            MonthlySalary salary = monthlySalaryDAO.findMonthlySalaryById(salaryId);
            if (salary == null) {
                throw new SalaryServiceException("未找到ID为 " + salaryId + " 的工资记录。");
            }
            return calculateSalaryComponents(salary);
        } catch (SQLException e) {
            logger.error("为工资记录ID {} 计算工资组成时发生数据库错误", salaryId, e);
            throw new SalaryServiceException("计算工资组成失败 (DB Error): " + e.getMessage(), e);
        }
    }

    @Override
    public MonthlySalary calculateSalaryComponents(MonthlySalary salary) throws SalaryServiceException {
        if (salary == null) throw new SalaryServiceException("用于计算的工资对象不能为空。");
        if (salary.getSpecialAdditionalDeductionMonthly() == null || salary.getSpecialAdditionalDeductionMonthly().compareTo(BigDecimal.ZERO) == 0) {
            try {
                int year = Integer.parseInt(salary.getYearMonth().substring(0, 4));
                EmployeeAnnualDeduction annualDeduction = annualDeductionDAO.findAnnualDeductionByEmployeeIdAndYear(salary.getEmployeeId(), year);
                if (annualDeduction != null && annualDeduction.getMonthlyDeductionCalculated() != null) {
                    salary.setSpecialAdditionalDeductionMonthly(annualDeduction.getMonthlyDeductionCalculated());
                }
            } catch (Exception e) {
                logger.warn("获取员工 {} 年份 {} 的专项附加扣除月度金额失败: {}", salary.getEmployeeId(), salary.getYearMonth(), e.getMessage());
                salary.setSpecialAdditionalDeductionMonthly(BigDecimal.ZERO);
            }
        }
        BigDecimal totalEarnings = salary.getBasicSalary().add(salary.getPostAllowance()).add(salary.getLunchSubsidy()).add(salary.getOvertimePay()).add(salary.getAttendanceBonus()).add(salary.getOtherEarnings());
        salary.setTotalEarningsManual(totalEarnings);
        BigDecimal totalPreTaxDeductions = salary.getSocialSecurityPersonal().add(salary.getProvidentFundPersonal()).add(salary.getSpecialAdditionalDeductionMonthly()).add(salary.getEnterpriseAnnuityPersonal()).add(salary.getOtherPreTaxDeductions());
        salary.setTotalPreTaxDeductionsManual(totalPreTaxDeductions);
        BigDecimal taxableIncomeBeforeThreshold = totalEarnings.subtract(totalPreTaxDeductions);
        salary.setTaxableIncomeBeforeThreshold(taxableIncomeBeforeThreshold.max(BigDecimal.ZERO));
        BigDecimal taxableIncome = taxableIncomeBeforeThreshold.subtract(TAX_THRESHOLD);
        salary.setTaxableIncome(taxableIncome.max(BigDecimal.ZERO));
        BigDecimal personalIncomeTax = calculateSimplifiedMonthlyTax(salary.getTaxableIncome());
        salary.setPersonalIncomeTax(personalIncomeTax);
        salary.setCalculationMethodNotes("简化月度计算法 (非标，仅供演示)");
        BigDecimal netSalary = totalEarnings.subtract(salary.getSocialSecurityPersonal()).subtract(salary.getProvidentFundPersonal()).subtract(personalIncomeTax).subtract(salary.getLateLeaveDeduction()).subtract(salary.getOtherPostTaxDeductions());
        salary.setNetSalaryManual(netSalary);
        return salary;
    }

    private BigDecimal calculateSimplifiedMonthlyTax(BigDecimal monthlyTaxableIncome) {
        if (monthlyTaxableIncome == null || monthlyTaxableIncome.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;
        BigDecimal annualizedIncome = monthlyTaxableIncome.multiply(new BigDecimal("12"));
        BigDecimal annualTax = BigDecimal.ZERO;
        for (int i = 0; i < ANNUAL_TAXABLE_INCOME_LEVELS.length; i++) {
            if (annualizedIncome.compareTo(ANNUAL_TAXABLE_INCOME_LEVELS[i]) <= 0) {
                annualTax = annualizedIncome.multiply(TAX_RATES[i]).subtract(QUICK_DEDUCTIONS_ANNUAL[i]);
                break;
            }
        }
        return annualTax.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP).max(BigDecimal.ZERO);
    }

    private void validateSalaryInput(MonthlySalary salary) throws SalaryServiceException {
        if (salary.getBasicSalary().compareTo(BigDecimal.ZERO) < 0 || salary.getSocialSecurityPersonal().compareTo(BigDecimal.ZERO) < 0 || salary.getProvidentFundPersonal().compareTo(BigDecimal.ZERO) < 0) {
            throw new SalaryServiceException("基本工资、个人社保、个人公积金等金额不能为负数。");
        }
    }

    @Override
    public MonthlySalary getMonthlySalaryById(long salaryId) throws SalaryServiceException {
        if (salaryId <= 0) throw new SalaryServiceException("无效的工资记录ID。");
        try {
            return monthlySalaryDAO.findMonthlySalaryById(salaryId);
        } catch (SQLException e) {
            throw new SalaryServiceException("获取工资记录失败：" + e.getMessage(), e);
        }
    }

    @Override
    public MonthlySalary getMonthlySalaryByEmployeeAndYearMonth(int employeeId, String yearMonth) throws SalaryServiceException {
        if (employeeId <= 0 || yearMonth == null || yearMonth.trim().isEmpty()) {
            throw new SalaryServiceException("员工ID和工资月份不能为空。");
        }
        try {
            return monthlySalaryDAO.findMonthlySalaryByEmployeeAndYearMonth(employeeId, yearMonth);
        } catch (SQLException e) {
            throw new SalaryServiceException("获取工资记录失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<MonthlySalary> getSalaryHistoryForEmployee(int employeeId) throws SalaryServiceException {
        if (employeeId <= 0) throw new SalaryServiceException("无效的员工ID。");
        try {
            return monthlySalaryDAO.findMonthlySalariesByEmployeeId(employeeId);
        } catch (SQLException e) {
            throw new SalaryServiceException("获取员工工资历史失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MonthlySalary> getSalariesByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws SalaryServiceException {
        try {
            return monthlySalaryDAO.findMonthlySalariesByFilters(filters, pageNum, pageSize, orderBy);
        } catch (SQLException e) {
            throw new SalaryServiceException("查询工资列表失败：" + e.getMessage(), e);
        }
    }

    @Override
    public int countSalariesByFilters(Map<String, Object> filters) throws SalaryServiceException {
        try {
            return monthlySalaryDAO.countMonthlySalariesByFilters(filters);
        } catch (SQLException e) {
            throw new SalaryServiceException("统计工资记录总数失败：" + e.getMessage(), e);
        }
    }

    //工资批量导入方法
    @Override
    public String batchImportSalaries(InputStream inputStream, String yearMonth) throws SalaryServiceException {
        StringBuilder resultMessage = new StringBuilder();
        int successCount = 0;
        int failureCount = 0;
        int totalCount = 0;

        try (Workbook workbook = WorkbookFactory.create(inputStream)) { // 使用 WorkbookFactory 自动检测格式
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // 跳过标题行
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isRowEmpty(row)) continue;
                totalCount++;
                try {
                    MonthlySalary salary = parseSalaryFromExcelRow(row, yearMonth);
                    saveOrUpdateMonthlySalary(salary);
                    successCount++;
                } catch (SalaryServiceException | EmployeeServiceException e) {
                    failureCount++;
                    String errorMsg = "第 " + (row.getRowNum() + 1) + " 行处理失败: " + e.getMessage();
                    logger.warn(errorMsg);
                    resultMessage.append(errorMsg).append("\n");
                }
            }
        } catch (IOException e) {
            throw new SalaryServiceException("读取Excel文件IO异常。", e);
        } catch (Exception e) {
            throw new SalaryServiceException("处理Excel文件时发生未知错误，请检查文件格式是否正确。", e);
        }

        resultMessage.insert(0, "总共处理 " + totalCount + " 行数据。\n成功导入并计算: " + successCount + " 条\n失败: " + failureCount + " 条\n\n失败详情:\n");
        return resultMessage.toString();
    }

    private MonthlySalary parseSalaryFromExcelRow(Row row, String yearMonth) throws SalaryServiceException, EmployeeServiceException {
        if (row.getCell(1) == null) throw new SalaryServiceException("员工编号为空。");

        String employeeNumber = getCellStringValue(row.getCell(1)).trim();
        if (employeeNumber.isEmpty()) throw new SalaryServiceException("员工编号不能为空。");

        Employee employee = employeeService.getEmployeeByEmployeeNumber(employeeNumber);
        if (employee == null) throw new SalaryServiceException("数据库中不存在员工编号为 '" + employeeNumber + "' 的员工。");

        String nameFromExcel = getCellStringValue(row.getCell(2)).trim();
        if (!nameFromExcel.isEmpty() && !employee.getName().equals(nameFromExcel)) {
            logger.warn("警告: 第 {} 行的员工姓名 '{}' 与数据库中的姓名不匹配 (员工编号: {})。", row.getRowNum() + 1, nameFromExcel, employeeNumber);
        }

        MonthlySalary salary = new MonthlySalary();
        salary.setEmployeeId(employee.getId());
        salary.setYearMonth(yearMonth);

        salary.setShouldAttendDays(getNumericCellValue(row.getCell(3)));
        salary.setActualAttendDays(getNumericCellValue(row.getCell(4)));
        salary.setBasicSalary(getNumericCellValue(row.getCell(5)));
        salary.setPostAllowance(getNumericCellValue(row.getCell(6)));
        salary.setLunchSubsidy(getNumericCellValue(row.getCell(7)));
        salary.setOvertimePay(getNumericCellValue(row.getCell(8)));
        salary.setAttendanceBonus(getNumericCellValue(row.getCell(9)));
        salary.setSocialSecurityPersonal(getNumericCellValue(row.getCell(10)));
        salary.setProvidentFundPersonal(getNumericCellValue(row.getCell(11)));
        salary.setLateLeaveDeduction(getNumericCellValue(row.getCell(13)));

        return salary;
    }

    private BigDecimal getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return BigDecimal.ZERO;
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        if (cell.getCellType() == CellType.STRING) {
            String value = cell.getStringCellValue().trim();
            if (value.isEmpty()) return BigDecimal.ZERO;
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                logger.warn("无法将字符串 '{}' 解析为数字，按0处理。", value);
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}