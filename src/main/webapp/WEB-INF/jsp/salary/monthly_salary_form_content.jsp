<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="pagetitle mb-3">
    <h1>${(salary.id != null && salary.id > 0) ? '编辑' : '录入'}工资条目 - <c:out value="${currentEmployee.name}"/> (<c:out value="${currentEmployee.employeeNumber}"/>) - <c:out value="${salary.yearMonth}"/></h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/salaries?action=list_by_month&yearMonth=${salary.yearMonth}">月度工资列表 (<c:out value="${salary.yearMonth}"/>)</a></li>
            <li class="breadcrumb-item active">${(salary.id != null && salary.id > 0) ? '编辑' : '录入'}</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${requestScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">工资明细录入/编辑</h5>
        <p class="card-text">
            员工: <strong><c:out value="${currentEmployee.name}"/> (<c:out value="${currentEmployee.employeeNumber}"/>)</strong><br>
            部门: <strong><c:out value="${currentEmployee.department.deptName}"/></strong><br>
            工资月份: <strong><c:out value="${salary.yearMonth}"/></strong>
        </p>
        <hr>

        <form action="${pageContext.request.contextPath}/salaries" method="post" class="row g-3 needs-validation" novalidate>
            <input type="hidden" name="action" value="save_salary">
            <input type="hidden" name="employeeId" value="${currentEmployee.id}">
            <input type="hidden" name="yearMonth" value="${salary.yearMonth}">
            <c:if test="${salary.id != null && salary.id > 0}">
                <input type="hidden" name="id" value="${salary.id}">
            </c:if>

            <h6 class="mt-3 text-primary">应发项目</h6>
            <div class="col-md-4">
                <label for="basicSalary" class="form-label">基本工资 <span class="text-danger">*</span></label>
                <input type="number" step="0.01" class="form-control" id="basicSalary" name="basicSalary" value="<fmt:formatNumber value='${salary.basicSalary}' type='number' groupingUsed='false' minFractionDigits='2'/>" required>
            </div>
            <div class="col-md-4">
                <label for="postAllowance" class="form-label">岗位津贴</label>
                <input type="number" step="0.01" class="form-control" id="postAllowance" name="postAllowance" value="<fmt:formatNumber value='${salary.postAllowance}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-4">
                <label for="lunchSubsidy" class="form-label">午餐补贴</label>
                <input type="number" step="0.01" class="form-control" id="lunchSubsidy" name="lunchSubsidy" value="<fmt:formatNumber value='${salary.lunchSubsidy}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-4">
                <label for="overtimePay" class="form-label">加班工资</label>
                <input type="number" step="0.01" class="form-control" id="overtimePay" name="overtimePay" value="<fmt:formatNumber value='${salary.overtimePay}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-4">
                <label for="attendanceBonus" class="form-label">全勤工资</label>
                <input type="number" step="0.01" class="form-control" id="attendanceBonus" name="attendanceBonus" value="<fmt:formatNumber value='${salary.attendanceBonus}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-4">
                <label for="otherEarnings" class="form-label">其他应发/奖金</label>
                <input type="number" step="0.01" class="form-control" id="otherEarnings" name="otherEarnings" value="<fmt:formatNumber value='${salary.otherEarnings}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>

            <h6 class="mt-4 text-primary">税前法定扣除项目</h6>
            <div class="col-md-3">
                <label for="socialSecurityPersonal" class="form-label">个人社保 <span class="text-danger">*</span></label>
                <input type="number" step="0.01" class="form-control" id="socialSecurityPersonal" name="socialSecurityPersonal" value="<fmt:formatNumber value='${salary.socialSecurityPersonal}' type='number' groupingUsed='false' minFractionDigits='2'/>" required>
            </div>
            <div class="col-md-3">
                <label for="providentFundPersonal" class="form-label">个人公积金 <span class="text-danger">*</span></label>
                <input type="number" step="0.01" class="form-control" id="providentFundPersonal" name="providentFundPersonal" value="<fmt:formatNumber value='${salary.providentFundPersonal}' type='number' groupingUsed='false' minFractionDigits='2'/>" required>
            </div>
            <div class="col-md-3">
                <label for="specialAdditionalDeductionMonthly_manual" class="form-label">专项附加扣除</label>
                <input type="number" step="0.01" class="form-control" id="specialAdditionalDeductionMonthly_manual" name="specialAdditionalDeductionMonthly_manual" value="<fmt:formatNumber value='${salary.specialAdditionalDeductionMonthly}' type='number' groupingUsed='false' minFractionDigits='2'/>" readonly>
                <div class="form-text">由系统根据年度申报自动计算，此处仅为显示。</div>
            </div>
            <div class="col-md-3">
                <label for="enterpriseAnnuityPersonal" class="form-label">企业年金(个人)</label>
                <input type="number" step="0.01" class="form-control" id="enterpriseAnnuityPersonal" name="enterpriseAnnuityPersonal" value="<fmt:formatNumber value='${salary.enterpriseAnnuityPersonal}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-12">
                <label for="otherPreTaxDeductions" class="form-label">其他税前扣除</label>
                <input type="number" step="0.01" class="form-control" id="otherPreTaxDeductions" name="otherPreTaxDeductions" value="<fmt:formatNumber value='${salary.otherPreTaxDeductions}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>

            <h6 class="mt-4 text-primary">其他调整与备注</h6>
            <div class="col-md-6">
                <label for="lateLeaveDeduction" class="form-label">迟到请假等扣款</label>
                <input type="number" step="0.01" class="form-control" id="lateLeaveDeduction" name="lateLeaveDeduction" value="<fmt:formatNumber value='${salary.lateLeaveDeduction}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-6">
                <label for="otherPostTaxDeductions" class="form-label">其他税后扣除</label>
                <input type="number" step="0.01" class="form-control" id="otherPostTaxDeductions" name="otherPostTaxDeductions" value="<fmt:formatNumber value='${salary.otherPostTaxDeductions}' type='number' groupingUsed='false' minFractionDigits='2'/>">
            </div>
            <div class="col-md-6">
                <label for="salaryStatus" class="form-label">工资状态</label>
                <select class="form-select" id="salaryStatus" name="salaryStatus">
                    <option value="DRAFT" ${salary.salaryStatus eq 'DRAFT' ? 'selected' : ''}>草稿</option>
                    <option value="PENDING_APPROVAL" ${salary.salaryStatus eq 'PENDING_APPROVAL' ? 'selected' : ''}>待审核</option>
                    <option value="APPROVED" ${salary.salaryStatus eq 'APPROVED' ? 'selected' : ''}>已审核</option>
                    <option value="PAID" ${salary.salaryStatus eq 'PAID' ? 'selected' : ''}>已发放</option>
                    <option value="REJECTED" ${salary.salaryStatus eq 'REJECTED' ? 'selected' : ''}>已驳回</option>
                </select>
            </div>
            <div class="col-md-12">
                <label for="remarks" class="form-label">工资备注</label>
                <textarea class="form-control" id="remarks" name="remarks" rows="2"><c:out value='${salary.remarks}'/></textarea>
            </div>


            <h5 class="mt-4 text-success">计算结果预览 (保存后将基于输入项重新计算)</h5>
            <div class="col-md-3">
                <label class="form-label">应发合计:</label>
                <input type="text" class="form-control-plaintext" readonly value="<fmt:formatNumber value='${salary.totalEarningsManual}' type='currency' currencySymbol='¥'/>">
            </div>
            <div class="col-md-3">
                <label class="form-label">税前扣除合计:</label>
                <input type="text" class="form-control-plaintext" readonly value="<fmt:formatNumber value='${salary.totalPreTaxDeductionsManual}' type='currency' currencySymbol='¥'/>">
            </div>
            <div class="col-md-3">
                <label class="form-label">应纳税所得额:</label>
                <input type="text" class="form-control-plaintext" readonly value="<fmt:formatNumber value='${salary.taxableIncome}' type='currency' currencySymbol='¥'/>">
            </div>
            <div class="col-md-3">
                <label class="form-label">个人所得税:</label>
                <input type="text" class="form-control-plaintext" readonly value="<fmt:formatNumber value='${salary.personalIncomeTax}' type='currency' currencySymbol='¥'/>">
            </div>
            <div class="col-md-3">
                <label class="form-label"><strong>实发工资:</strong></label>
                <input type="text" class="form-control-plaintext fw-bold" readonly value="<fmt:formatNumber value='${salary.netSalaryManual}' type='currency' currencySymbol='¥'/>">
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-calculator-fill me-1"></i> 保存并计算工资
                </button>
                <a href="${pageContext.request.contextPath}/salaries?action=list_by_month&yearMonth=${salary.yearMonth}" class="btn btn-secondary">
                    <i class="bi bi-x-circle me-1"></i> 取消
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>