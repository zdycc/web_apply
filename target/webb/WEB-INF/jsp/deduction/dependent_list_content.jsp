<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>被抚养人信息管理 - <c:out value="${currentEmployee.name}"/> (<c:out value="${currentEmployee.employeeNumber}"/>)</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees?action=list">人员列表</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/specialDeductions?action=list_employees_for_deduction">专项附加扣除</a></li>
            <li class="breadcrumb-item active">被抚养人列表</li>
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
            <h5 class="card-title mb-0">被抚养人列表</h5>
            <div>
                <a href="${pageContext.request.contextPath}/specialDeductions?action=list_annual_deductions&employeeId=${currentEmployee.id}" class="btn btn-outline-secondary btn-sm me-2">
                    <i class="bi bi-calendar-check me-1"></i> 查看年度扣除记录
                </a>
                <a href="${pageContext.request.contextPath}/specialDeductions?action=add_dependent_form&employeeId=${currentEmployee.id}" class="btn btn-primary btn-sm">
                    <i class="bi bi-person-plus-fill me-1"></i> 添加被抚养人
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty dependents && fn:length(dependents) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">姓名</th>
                            <th scope="col">身份证号</th>
                            <th scope="col">与员工关系</th>
                            <th scope="col">出生日期</th>
                            <th scope="col">涉及扣除项目</th>
                            <th scope="col">备注</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="dep" items="${dependents}" varStatus="status">
                            <tr>
                                <th scope="row">${status.count}</th>
                                <td><c:out value="${dep.dependentName}"/></td> <%-- Service层已脱敏 --%>
                                <td><c:out value="${dep.dependentIdCardNumber}"/></td> <%-- Service层已脱敏 --%>
                                <td><c:out value="${dep.relationship}"/></td>
                                <td>
                                    <fmt:formatDate value="${dep.birthDate}" pattern="yyyy-MM-dd"/>
                                </td>
                                <td><c:out value="${dep.deductionTypeInvolved}"/></td>
                                <td><c:out value="${dep.notes}"/></td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=edit_dependent_form&employeeId=${currentEmployee.id}&id=${dep.id}" class="btn btn-outline-primary btn-sm me-1" title="编辑">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=delete_dependent&employeeId=${currentEmployee.id}&id=${dep.id}" class="btn btn-outline-danger btn-sm" title="删除"
                                       onclick="return confirm('确定要删除被抚养人 “${fn:escapeXml(dep.dependentName)}” 的信息吗？');">
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
                    该员工暂无被抚养人信息。
                </div>
            </c:otherwise>
        </c:choose>
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/specialDeductions?action=list_employees_for_deduction" class="btn btn-secondary btn-sm">
                <i class="bi bi-arrow-left-circle me-1"></i> 返回员工选择列表
            </a>
        </div>
    </div>
</div>