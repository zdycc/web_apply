<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="pagetitle mb-3">
    <h1>${annualDeduction.id > 0 ? '编辑' : '添加'}年度专项附加扣除 - <c:out value="${currentEmployee.name}"/> (<c:out value="${currentEmployee.employeeNumber}"/>)</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees?action=list">人员列表</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/specialDeductions?action=list_annual_deductions&employeeId=${currentEmployee.id}">年度专项附加扣除</a></li>
            <li class="breadcrumb-item active">${annualDeduction.id > 0 ? '编辑' : '添加'}</li>
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
        <h5 class="card-title mt-3">${annualDeduction.id > 0 ? '修改扣除信息' : '填写年度扣除信息'}</h5>

        <form action="${pageContext.request.contextPath}/specialDeductions" method="post" class="row g-3 needs-validation" novalidate>
            <input type="hidden" name="action" value="save_annual_deduction">
            <input type="hidden" name="employeeId" value="${currentEmployee.id}">
            <c:if test="${annualDeduction.id > 0}">
                <input type="hidden" name="annualDeductionId" value="${annualDeduction.id}">
            </c:if>

            <div class="col-md-4">
                <label for="year" class="form-label">所属年份 <span class="text-danger">*</span></label>
                <input type="number" class="form-control" id="year" name="year" value="${annualDeduction.year > 0 ? annualDeduction.year : java.time.Year.now().getValue()}" required min="2000" max="2099" <c:if test="${annualDeduction.id > 0}">readonly</c:if>>
                <div class="invalid-feedback">请输入有效的4位年份。</div>
                <c:if test="${annualDeduction.id > 0}"><div class="form-text">年份不可修改。</div></c:if>
            </div>
            <div class="col-md-8"></div> <%-- Placeholder for alignment --%>


            <div class="col-md-4">
                <label for="childrenEducationAmount" class="form-label">子女教育 (元)</label>
                <input type="number" step="0.01" class="form-control" id="childrenEducationAmount" name="childrenEducationAmount" value="<fmt:formatNumber value="${annualDeduction.childrenEducationAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
            </div>
            <div class="col-md-4">
                <label for="continuingEducationAmount" class="form-label">继续教育 (元)</label>
                <input type="number" step="0.01" class="form-control" id="continuingEducationAmount" name="continuingEducationAmount" value="<fmt:formatNumber value="${annualDeduction.continuingEducationAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
            </div>
            <div class="col-md-4">
                <label for="infantCareAmount" class="form-label">3岁以下婴幼儿照护 (元)</label>
                <input type="number" step="0.01" class="form-control" id="infantCareAmount" name="infantCareAmount" value="<fmt:formatNumber value="${annualDeduction.infantCareAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
            </div>


            <div class="col-md-4">
                <label for="housingLoanInterestAmount" class="form-label">住房贷款利息 (元)</label>
                <input type="number" step="0.01" class="form-control" id="housingLoanInterestAmount" name="housingLoanInterestAmount" value="<fmt:formatNumber value="${annualDeduction.housingLoanInterestAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
            </div>
            <div class="col-md-4">
                <label for="housingRentAmount" class="form-label">住房租金 (元)</label>
                <input type="number" step="0.01" class="form-control" id="housingRentAmount" name="housingRentAmount" value="<fmt:formatNumber value="${annualDeduction.housingRentAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
            </div>
            <div class="col-md-4">
                <label for="elderlyCareAmount" class="form-label">赡养老人 (元)</label>
                <input type="number" step="0.01" class="form-control" id="elderlyCareAmount" name="elderlyCareAmount" value="<fmt:formatNumber value="${annualDeduction.elderlyCareAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
            </div>

            <div class="col-md-12">
                <label for="seriousIllnessMedicalAmount" class="form-label">大病医疗 (元)</label>
                <input type="number" step="0.01" class="form-control" id="seriousIllnessMedicalAmount" name="seriousIllnessMedicalAmount" value="<fmt:formatNumber value="${annualDeduction.seriousIllnessMedicalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/>" placeholder="0.00">
                <div class="form-text">通常为医保报销后个人自付超过一定起付线的部分，按实际发生额填写，但年度有扣除上限。</div>
            </div>

            <div class="col-md-12">
                <label for="remarks" class="form-label">备注</label>
                <textarea class="form-control" id="remarks" name="remarks" rows="3"><c:out value="${annualDeduction.remarks}"/></textarea>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-check-circle me-1"></i> 保存记录
                </button>
                <a href="${pageContext.request.contextPath}/specialDeductions?action=list_annual_deductions&employeeId=${currentEmployee.id}" class="btn btn-secondary">
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