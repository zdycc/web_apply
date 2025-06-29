<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>月度工资列表 - <c:out value="${currentYearMonth}"/></h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">月度工资</li>
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
<c:if test="${not empty requestScope.infoMessage}">
    <div class="alert alert-info alert-dismissible fade show" role="alert">
        <i class="bi bi-info-circle-fill me-2"></i> ${requestScope.infoMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3 mt-3">
            <h5 class="card-title mb-0">工资明细 (<c:out value="${currentYearMonth}"/>)</h5>
            <div>
                <form action="${pageContext.request.contextPath}/salaries" method="get" class="d-inline-flex align-items-center">
                    <input type="hidden" name="action" value="list_by_month">
                    <label for="yearMonthFilter" class="form-label me-2 mb-0">选择月份:</label>
                    <input type="month" class="form-control form-control-sm me-2" style="width: auto;"
                           id="yearMonthFilter" name="yearMonth" value="${currentYearMonth}">
                    <button type="submit" class="btn btn-secondary btn-sm">查询</button>
                </form>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty salaries && fn:length(salaries) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">员工编号</th>
                            <th scope="col">姓名</th>
                            <th scope="col">部门</th>
                            <th scope="col">应发合计</th>
                            <th scope="col">社保(个人)</th>
                            <th scope="col">公积金(个人)</th>
                            <th scope="col">专项附加扣除</th>
                            <th scope="col">应纳税所得额</th>
                            <th scope="col">个人所得税</th>
                            <th scope="col">实发工资</th>
                            <th scope="col">状态</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="salary" items="${salaries}">
                            <tr>
                                <td><c:out value="${salary.employee.employeeNumber}"/></td>
                                <td><c:out value="${salary.employee.name}"/></td>
                                <td><c:out value="${salary.employee.department.deptName}"/></td>
                                <td><fmt:formatNumber value="${salary.totalEarningsManual}" type="currency" currencySymbol="" minFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${salary.socialSecurityPersonal}" type="currency" currencySymbol="" minFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${salary.providentFundPersonal}" type="currency" currencySymbol="" minFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${salary.specialAdditionalDeductionMonthly}" type="currency" currencySymbol="" minFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${salary.taxableIncome}" type="currency" currencySymbol="" minFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${salary.personalIncomeTax}" type="currency" currencySymbol="" minFractionDigits="2"/></td>
                                <td><strong><fmt:formatNumber value="${salary.netSalaryManual}" type="currency" currencySymbol="¥" minFractionDigits="2"/></strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${salary.salaryStatus eq 'DRAFT'}"><span class="badge bg-light text-dark">草稿</span></c:when>
                                        <c:when test="${salary.salaryStatus eq 'PENDING_APPROVAL'}"><span class="badge bg-warning text-dark">待审核</span></c:when>
                                        <c:when test="${salary.salaryStatus eq 'APPROVED'}"><span class="badge bg-info">已审核</span></c:when>
                                        <c:when test="${salary.salaryStatus eq 'PAID'}"><span class="badge bg-success">已发放</span></c:when>
                                        <c:when test="${salary.salaryStatus eq 'REJECTED'}"><span class="badge bg-danger">已驳回</span></c:when>
                                        <c:otherwise><span class="badge bg-secondary">${salary.salaryStatus}</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/salaries?action=edit_form&employeeId=${salary.employeeId}&yearMonth=${salary.yearMonth}&salaryId=${salary.id}"
                                       class="btn btn-outline-primary btn-sm" title="编辑/查看详情">
                                        <i class="bi bi-pencil-square"></i>
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
                    <c:out value="${currentYearMonth}"/> 月份暂无已录入的工资记录。
                    您可以通过 <a href="${pageContext.request.contextPath}/employees?action=list" class="alert-link">员工列表</a> 为员工逐条添加当月工资。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>