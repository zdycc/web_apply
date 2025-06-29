<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>选择员工进行专项附加扣除管理</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">专项附加扣除 - 选择员工</li>
        </ol>
    </nav>
</div>

<%-- 通用消息显示 --%>
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
        <h5 class="card-title mt-3">请选择要管理专项附加扣除的员工</h5>

        <%-- 搜索表单 --%>
        <form action="${pageContext.request.contextPath}/specialDeductions" method="get" class="row g-3 mb-4">
            <input type="hidden" name="action" value="list_employees_for_deduction">
            
            <div class="col-md-4">
                <label for="searchKeyword" class="form-label">员工编号搜索:</label>
                <input type="text" class="form-control" id="searchKeyword" name="searchKeyword" 
                       value="${searchKeyword}" placeholder="请输入员工编号">
            </div>
            
            <div class="col-md-3">
                <label for="departmentId" class="form-label">部门:</label>
                <select class="form-select" id="departmentId" name="departmentId">
                    <option value="">所有部门</option>
                    <c:forEach var="dept" items="${departments}">
                        <option value="${dept.id}" ${selectedDepartmentId == dept.id ? 'selected' : ''}>
                            <c:out value="${dept.deptName}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="col-md-5 d-flex align-items-end">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="bi bi-search me-1"></i> 搜索
                </button>
                <a href="${pageContext.request.contextPath}/specialDeductions?action=list_employees_for_deduction" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-clockwise me-1"></i> 重置
                </a>
            </div>
        </form>

        <c:choose>
            <c:when test="${not empty employeesForSelection && fn:length(employeesForSelection) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">员工编号</th>
                            <th scope="col">姓名</th>
                            <th scope="col">部门</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="emp" items="${employeesForSelection}">
                            <tr>
                                <td><c:out value="${emp.employeeNumber}"/></td>
                                <td><c:out value="${emp.name}"/></td> <%-- Servlet中应传递脱敏后的姓名 --%>
                                <td><c:out value="${emp.department.deptName}"/></td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=list_annual_deductions&employeeId=${emp.id}" class="btn btn-primary btn-sm me-1" title="管理年度扣除">
                                        <i class="bi bi-calendar-check"></i> 年度扣除
                                    </a>
                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=list_dependents&employeeId=${emp.id}" class="btn btn-info btn-sm text-white" title="管理被抚养人">
                                        <i class="bi bi-people"></i> 被抚养人
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
                    当前没有员工信息可供选择。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>