<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>${dependent.id > 0 ? '编辑' : '添加'}被抚养人信息 - <c:out value="${currentEmployee.name}"/> (<c:out value="${currentEmployee.employeeNumber}"/>)</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees?action=list">人员列表</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/specialDeductions?action=list_employees_for_deduction">专项附加扣除</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/specialDeductions?action=list_dependents&employeeId=${currentEmployee.id}">被抚养人列表</a></li>
            <li class="breadcrumb-item active">${dependent.id > 0 ? '编辑' : '添加'}</li>
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
        <h5 class="card-title mt-3">${dependent.id > 0 ? '修改被抚养人信息' : '填写被抚养人信息'}</h5>

        <form action="${pageContext.request.contextPath}/specialDeductions" method="post" class="row g-3 needs-validation" novalidate>
            <input type="hidden" name="action" value="save_dependent">
            <input type="hidden" name="employeeId" value="${currentEmployee.id}">
            <c:if test="${dependent.id > 0}">
                <input type="hidden" name="dependentId" value="${dependent.id}">
            </c:if>

            <div class="col-md-6">
                <label for="dependentName" class="form-label">被抚养人姓名 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="dependentName" name="dependentName" value="<c:out value='${dependent.dependentName}'/>" required>
                <div class="invalid-feedback">请输入被抚养人姓名。</div>
            </div>

            <div class="col-md-6">
                <label for="dependentIdCardNumber" class="form-label">被抚养人身份证号 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="dependentIdCardNumber" name="dependentIdCardNumber" value="<c:out value='${dependent.dependentIdCardNumber}'/>" required pattern="^\d{17}(\d|X|x)$">
                <div class="invalid-feedback">请输入有效的18位身份证号。</div>
            </div>

            <div class="col-md-6">
                <label for="relationship" class="form-label">与员工关系 <span class="text-danger">*</span></label>
                <select class="form-select" id="relationship" name="relationship" required>
                    <option value="" ${empty dependent.relationship ? 'selected' : ''} disabled>-- 请选择关系 --</option>
                    <option value="子女" ${dependent.relationship eq '子女' ? 'selected' : ''}>子女</option>
                    <option value="父亲" ${dependent.relationship eq '父亲' ? 'selected' : ''}>父亲</option>
                    <option value="母亲" ${dependent.relationship eq '母亲' ? 'selected' : ''}>母亲</option>
                    <option value="配偶" ${dependent.relationship eq '配偶' ? 'selected' : ''}>配偶</option>
                    <option value="配偶父亲" ${dependent.relationship eq '配偶父亲' ? 'selected' : ''}>配偶父亲</option>
                    <option value="配偶母亲" ${dependent.relationship eq '配偶母亲' ? 'selected' : ''}>配偶母亲</option>
                    <option value="其他" ${dependent.relationship eq '其他' ? 'selected' : ''}>其他</option>
                </select>
                <div class="invalid-feedback">请选择与员工的关系。</div>
            </div>

            <div class="col-md-6">
                <label for="birthDate" class="form-label">出生日期</label>
                <fmt:formatDate value="${dependent.birthDate}" pattern="yyyy-MM-dd" var="formattedBirthDate"/>
                <input type="date" class="form-control" id="birthDate" name="birthDate" value="${formattedBirthDate}">
            </div>

            <div class="col-md-12">
                <label class="form-label">涉及的扣除项目:</label>

                <c:set var="selectedTypesArray" value="${fn:split(dependent.deductionTypeInvolved, ',')}" />
                <div class="mt-2">
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" name="deductionTypeInvolved" id="typeChildrenEdu" value="子女教育" <c:forEach var="type" items="${selectedTypesArray}"><c:if test="${type eq '子女教育'}">checked</c:if></c:forEach>>
                        <label class="form-check-label" for="typeChildrenEdu">子女教育</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" name="deductionTypeInvolved" id="typeContinuingEdu" value="继续教育" <c:forEach var="type" items="${selectedTypesArray}"><c:if test="${type eq '继续教育'}">checked</c:if></c:forEach>>
                        <label class="form-check-label" for="typeContinuingEdu">继续教育 (员工本人)</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" name="deductionTypeInvolved" id="typeInfantCare" value="婴幼儿照护" <c:forEach var="type" items="${selectedTypesArray}"><c:if test="${type eq '婴幼儿照护'}">checked</c:if></c:forEach>>
                        <label class="form-check-label" for="typeInfantCare">3岁以下婴幼儿照护</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" name="deductionTypeInvolved" id="typeElderlyCare" value="赡养老人" <c:forEach var="type" items="${selectedTypesArray}"><c:if test="${type eq '赡养老人'}">checked</c:if></c:forEach>>
                        <label class="form-check-label" for="typeElderlyCare">赡养老人</label>
                    </div>
                </div>
                <div class="form-text">请选择此被抚养人涉及的一个或多个专项附加扣除项目。</div>
            </div>


            <%-- 关联到特定的年度扣除记录 --%>
            <div class="col-md-6">
                <label for="annualDeductionId" class="form-label">关联年度申报记录 (可选)</label>
                <select class="form-select" id="annualDeductionId" name="annualDeductionId">
                    <option value="0" ${empty dependent.annualDeductionId or dependent.annualDeductionId == 0 ? 'selected' : ''}>-- 不直接关联特定年度 --</option>
                    <c:if test="${not empty availableAnnualDeductions}">
                        <c:forEach var="annualRec" items="${availableAnnualDeductions}">
                            <option value="${annualRec.id}" ${dependent.annualDeductionId eq annualRec.id ? 'selected' : ''}>
                                    ${annualRec.year}年度申报 (总额: <fmt:formatNumber value="${annualRec.totalAnnualDeduction}" type="currency" currencySymbol="¥"/>)
                            </option>
                        </c:forEach>
                    </c:if>
                </select>
                <div class="form-text">如果此被抚养人的信息是为某一特定年度的专项附加扣除申报准备的，可以在此关联。</div>
            </div>
            <div class="col-md-6"></div> <%-- Placeholder for alignment --%>


            <div class="col-md-12">
                <label for="notes" class="form-label">备注</label>
                <textarea class="form-control" id="notes" name="notes" rows="3"><c:out value="${dependent.notes}"/></textarea>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-check-circle me-1"></i> ${dependent.id > 0 ? '确认修改' : '确认添加'}
                </button>
                <a href="${pageContext.request.contextPath}/specialDeductions?action=list_dependents&employeeId=${currentEmployee.id}" class="btn btn-secondary">
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