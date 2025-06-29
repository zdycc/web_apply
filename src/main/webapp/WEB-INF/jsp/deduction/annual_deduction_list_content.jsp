<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>年度专项附加扣除 - <c:out value="${currentEmployee.name}"/> (<c:out value="${currentEmployee.employeeNumber}"/>)</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees?action=list">人员列表</a></li>
            <li class="breadcrumb-item active">年度专项附加扣除</li>
        </ol>
    </nav>
</div>

<%-- 通用消息显示 --%>
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>
<c:if test="${not empty requestScope.errorMessage || not empty sessionScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
            ${not empty requestScope.errorMessage ? requestScope.errorMessage : sessionScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>


<div class="card">
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3 mt-3">
            <h5 class="card-title mb-0">年度扣除记录</h5>
            <a href="${pageContext.request.contextPath}/specialDeductions?action=add_annual_form&employeeId=${currentEmployee.id}" class="btn btn-primary btn-sm">
                <i class="bi bi-plus-circle me-1"></i> 添加新年度记录
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty annualDeductions && fn:length(annualDeductions) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">年份</th>
                            <th scope="col">子女教育(元)</th>
                            <th scope="col">继续教育(元)</th>
                            <th scope="col">大病医疗(元)</th>
                            <th scope="col">房贷利息(元)</th>
                            <th scope="col">住房租金(元)</th>
                            <th scope="col">赡养老人(元)</th>
                            <th scope="col">婴幼儿照护(元)</th>
                            <th scope="col">年度总计(元)</th>
                            <th scope="col">月均扣除(元)</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="deduction" items="${annualDeductions}">
                            <tr>
                                <td><strong><c:out value="${deduction.year}"/></strong></td>
                                <td><fmt:formatNumber value="${deduction.childrenEducationAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${deduction.continuingEducationAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${deduction.seriousIllnessMedicalAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${deduction.housingLoanInterestAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${deduction.housingRentAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${deduction.elderlyCareAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${deduction.infantCareAmount}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><strong><fmt:formatNumber value="${deduction.totalAnnualDeduction}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></strong></td>
                                <td><fmt:formatNumber value="${deduction.monthlyDeductionCalculated}" type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=edit_annual_form&employeeId=${currentEmployee.id}&id=${deduction.id}" class="btn btn-outline-primary btn-sm me-1" title="编辑">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=delete_annual&employeeId=${currentEmployee.id}&id=${deduction.id}" class="btn btn-outline-danger btn-sm" title="删除"
                                       onclick="return confirm('确定要删除 ${deduction.year} 年度的专项附加扣除记录吗？');">
                                        <i class="bi bi-trash3"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info mt-3" role="alert">
                    <i class="bi bi-info-circle-fill me-2"></i>
                    该员工暂无年度专项附加扣除记录。
                </div>
            </c:otherwise>
        </c:choose>
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/employees?action=list" class="btn btn-secondary btn-sm">
                <i class="bi bi-arrow-left-circle me-1"></i> 返回人员列表
            </a>
        </div>
    </div>
</div>