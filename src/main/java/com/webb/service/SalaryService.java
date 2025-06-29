package com.webb.service;

import com.webb.model.MonthlySalary;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface SalaryService {

    /**
     * 添加或更新月度工资记录。
     * 此方法会触发工资计算，并将完整的、计算后的工资对象进行保存。
     * @param salary 包含原始工资项的月度工资对象
     * @return 返回保存后的 MonthlySalary 对象  // <--- 修改点：返回类型改为 MonthlySalary
     * @throws SalaryServiceException 业务逻辑异常
     */
    MonthlySalary saveOrUpdateMonthlySalary(MonthlySalary salary) throws SalaryServiceException; // <--- 修改点

    /**
     * 根据ID获取月度工资详细信息
     * @param salaryId 工资记录ID
     * @return MonthlySalary 对象，包含所有计算后的项目
     * @throws SalaryServiceException 业务逻辑异常
     */
    MonthlySalary getMonthlySalaryById(long salaryId) throws SalaryServiceException;

    /**
     * 根据员工ID和月份获取月度工资详细信息
     * @param employeeId 员工ID
     * @param yearMonth 工资月份 (格式: "YYYY-MM")
     * @return MonthlySalary 对象
     * @throws SalaryServiceException 业务逻辑异常
     */
    MonthlySalary getMonthlySalaryByEmployeeAndYearMonth(int employeeId, String yearMonth) throws SalaryServiceException;

    /**
     * 根据员工ID获取其所有历史工资记录
     * @param employeeId 员工ID
     * @return 工资记录列表
     * @throws SalaryServiceException 业务逻辑异常
     */
    List<MonthlySalary> getSalaryHistoryForEmployee(int employeeId) throws SalaryServiceException;

    /**
     * 根据筛选条件分页查询工资列表（历史工资查询）
     * @param filters 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderBy 排序方式
     * @return 工资记录列表
     * @throws SalaryServiceException 业务逻辑异常
     */
    List<MonthlySalary> getSalariesByFilters(Map<String, Object> filters, int pageNum, int pageSize, String orderBy) throws SalaryServiceException;

    /**
     * 根据筛选条件统计工资记录总数
     * @param filters 查询条件
     * @return 记录总数
     * @throws SalaryServiceException 业务逻辑异常
     */
    int countSalariesByFilters(Map<String, Object> filters) throws SalaryServiceException;

    /**
     * 为指定的月度工资记录重新计算所有派生项（如总额、税、净额）。
     * @param salaryId 待计算的工资记录ID
     * @return 计算并更新后的 MonthlySalary 对象
     * @throws SalaryServiceException 如果记录不存在或计算失败
     */
    MonthlySalary calculateSalaryComponents(long salaryId) throws SalaryServiceException;

    /**
     * 为给定的 MonthlySalary 对象计算所有派生项。
     * 此方法不直接持久化，主要用于计算逻辑的复用。
     * @param salary 包含基础工资项的 MonthlySalary 对象
     * @return 已填充计算结果的 MonthlySalary 对象
     * @throws SalaryServiceException 如果基础数据不足或计算出错
     */
    MonthlySalary calculateSalaryComponents(MonthlySalary salary) throws SalaryServiceException;

    /**
     * 从文件流中批量导入工资数据
     * @param inputStream Excel文件输入流
     * @param yearMonth 工资所属月份 (格式: "YYYY-MM")
     * @return 返回导入结果的摘要信息
     * @throws SalaryServiceException 如果发生业务或解析错误
     */
    String batchImportSalaries(InputStream inputStream, String yearMonth) throws SalaryServiceException;

}

