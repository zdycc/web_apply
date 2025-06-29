<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="pagetitle mb-3">
    <h1>人员管理</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">人员列表</li>

        </ol>
    </nav>
</div>

<%-- 显示成功或错误消息 --%>
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i>
            ${sessionScope.successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
            ${sessionScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>
<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
            ${requestScope.errorMessage}
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3 mt-3">
            <h5 class="card-title mb-0">在职人员列表</h5>
            <a href="${pageContext.request.contextPath}/employees?action=add_form" class="btn btn-primary btn-sm">
                <i class="bi bi-person-plus-fill me-1"></i> 添加新员工
            </a>
        </div>

        <%-- 搜索表单 --%>
        <form action="${pageContext.request.contextPath}/employees" method="get" class="row g-3 mb-4">
            <input type="hidden" name="action" value="list">
            
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
                <a href="${pageContext.request.contextPath}/employees?action=list" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-clockwise me-1"></i> 重置
                </a>
            </div>
        </form>

        <c:choose>
            <c:when test="${not empty employees && fn:length(employees) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">员工编号</th>
                            <th scope="col">姓名</th>
                            <th scope="col">部门</th>
                            <th scope="col">岗位</th>
                            <th scope="col">职务</th>
                            <th scope="col">手机号</th>
                            <th scope="col">入职日期</th>
                            <th scope="col">状态</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="emp" items="${employees}" varStatus="status">
                            <tr>
                                <th scope="row">${status.count}</th>
                                <td><c:out value="${emp.employeeNumber}"/></td>
                                <td><c:out value="${emp.name}"/></td> <%-- Service层已脱敏 --%>
                                <td><c:out value="${emp.department.deptName}"/></td>
                                <td><c:out value="${emp.position}"/></td>
                                <td><c:out value="${emp.jobTitle}"/></td>
                                <td><c:out value="${emp.phoneNumber}"/></td> <%-- Service层已脱敏 --%>
                                <td>
                                    <fmt:formatDate value="${emp.hireDate}" pattern="yyyy-MM-dd"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${emp.status eq 'active'}"><span class="badge bg-success">在职</span></c:when>
                                        <c:when test="${emp.status eq 'resigned'}"><span class="badge bg-secondary">离职</span></c:when>
                                        <c:when test="${emp.status eq 'on_leave'}"><span class="badge bg-warning text-dark">休假</span></c:when>
                                        <c:otherwise><span class="badge bg-light text-dark">${emp.status}</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/employees?action=edit_form&id=${emp.id}" class="btn btn-outline-primary btn-sm me-1" title="编辑">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                        <%-- 默认跳转到为该员工录入当前月份的工资 --%>
                                    <a href="${pageContext.request.contextPath}/salaries?action=edit_form&employeeId=${emp.id}&yearMonth=<%= java.time.YearMonth.now().toString() %>" class="btn btn-outline-success btn-sm me-1" title="录入/编辑工资">
                                        <i class="bi bi-currency-dollar"></i>
                                    </a>

                                    <a href="${pageContext.request.contextPath}/specialDeductions?action=list_annual_deductions&employeeId=${emp.id}" class="btn btn-outline-info btn-sm me-1" title="专项扣除管理">
                                        <i class="bi bi-card-checklist"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/employees?action=delete&id=${emp.id}" class="btn btn-outline-danger btn-sm" title="删除"
                                       onclick="return confirm('确定要删除员工 “${fn:escapeXml(emp.name)}” (编号: ${fn:escapeXml(emp.employeeNumber)}) 吗？此操作不可恢复。');">
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
                    当前没有员工信息。请先添加新员工。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>