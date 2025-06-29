<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>历史工资查询</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">历史工资查询</li>
        </ol>
    </nav>
</div>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">查询条件</h5>

        <form action="${pageContext.request.contextPath}/salaries" method="get" class="row g-3">
            <input type="hidden" name="action" value="history_query">

            <div class="col-md-3">
                <label for="yearMonthStartFilter" class="form-label">开始月份:</label>
                <input type="month" class="form-control" id="yearMonthStartFilter" name="yearMonthStart" value="${param.yearMonthStart}">
            </div>
            <div class="col-md-3">
                <label for="yearMonthEndFilter" class="form-label">结束月份:</label>
                <input type="month" class="form-control" id="yearMonthEndFilter" name="yearMonthEnd" value="${param.yearMonthEnd}">
            </div>
            <div class="col-md-3">
                <label for="departmentIdFilter" class="form-label">部门:</label>
                <select class="form-select" id="departmentIdFilter" name="departmentId">
                    <option value="0">-- 所有部门 --</option>
                    <c:if test="${not empty departments}">
                        <c:forEach var="dept" items="${departments}">
                            <option value="${dept.id}" ${param.departmentId eq dept.id ? 'selected' : ''}>
                                <c:out value="${dept.deptName}"/>
                            </option>
                        </c:forEach>
                    </c:if>
                </select>
            </div>
            <div class="col-md-3">
                <label for="employeeNumberFilter" class="form-label">员工编号:</label>
                <input type="text" class="form-control" id="employeeNumberFilter" name="employeeNumber" value="${param.employeeNumber}" placeholder="精确匹配">
            </div>

            <div class="col-12 text-end">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-search me-1"></i> 查询
                </button>
                <a href="${pageContext.request.contextPath}/salaries?action=history_query" class="btn btn-secondary">重置条件</a>
            </div>
        </form>
    </div>
</div>


<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">查询结果</h5>

        <c:choose>
            <c:when test="${not empty salaries}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">工资月份</th>
                            <th scope="col">员工编号</th>
                            <th scope="col">姓名</th>
                            <th scope="col">部门</th>
                            <th scope="col">应发合计</th>
                            <th scope="col">个税</th>
                            <th scope="col">实发工资</th>
                            <th scope="col">状态</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="salary" items="${salaries}">
                            <tr>
                                <td><strong><c:out value="${salary.yearMonth}"/></strong></td>
                                <td><c:out value="${salary.employee.employeeNumber}"/></td>
                                <td><c:out value="${salary.employee.name}"/></td>
                                <td><c:out value="${salary.employee.department.deptName}"/></td>
                                <td><fmt:formatNumber value="${salary.totalEarningsManual}" type="currency" currencySymbol="¥"/></td>
                                <td><fmt:formatNumber value="${salary.personalIncomeTax}" type="currency" currencySymbol="¥"/></td>
                                <td><strong><fmt:formatNumber value="${salary.netSalaryManual}" type="currency" currencySymbol="¥"/></strong></td>
                                <td><span class="badge bg-success">${salary.salaryStatus}</span></td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/salaries?action=edit_form&employeeId=${salary.employeeId}&yearMonth=${salary.yearMonth}&salaryId=${salary.id}"
                                       class="btn btn-outline-primary btn-sm" title="查看详情/编辑">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <%-- 分页导航 --%>
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation">
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <div class="text-muted">
                                <small>共 ${totalPages} 页，当前第 ${currentPage} 页</small>
                            </div>
                            <ul class="pagination mb-0 pagination-sm">
                                <%-- 计算页码组 --%>
                                <c:set var="pageGroupSize" value="5" />
                                <c:set var="currentGroup" value="${(currentPage - 1) / pageGroupSize}" />
                                <c:set var="startPage" value="${currentGroup * pageGroupSize + 1}" />
                                <c:set var="endPage" value="${startPage + pageGroupSize - 1}" />
                                <c:if test="${endPage > totalPages}">
                                    <c:set var="endPage" value="${totalPages}" />
                                </c:if>

                                <%-- 上一组按钮 --%>
                                <c:if test="${startPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link px-2 text-center" style="min-width: 60px;" href="${pageContext.request.contextPath}/salaries?action=history_query&pageNum=${startPage - 1}&${searchParams}" title="上一组页码">
                                            上一组
                                        </a>
                                    </li>
                                </c:if>

                                <%-- 上一页按钮 --%>
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link px-2 text-center" style="min-width: 50px;" href="${pageContext.request.contextPath}/salaries?action=history_query&pageNum=${currentPage - 1}&${searchParams}" title="上一页">
                                        上页
                                    </a>
                                </li>

                                <%-- 页码按钮组 --%>
                                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link px-2 text-center" style="min-width: 40px;" href="${pageContext.request.contextPath}/salaries?action=history_query&pageNum=${i}&${searchParams}">${i}</a>
                                    </li>
                                </c:forEach>

                                <%-- 下一页按钮 --%>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link px-2 text-center" style="min-width: 50px;" href="${pageContext.request.contextPath}/salaries?action=history_query&pageNum=${currentPage + 1}&${searchParams}" title="下一页">
                                        下页
                                    </a>
                                </li>

                                <%-- 下一组按钮 --%>
                                <c:if test="${endPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link px-2 text-center" style="min-width: 60px;" href="${pageContext.request.contextPath}/salaries?action=history_query&pageNum=${endPage + 1}&${searchParams}" title="下一组页码">
                                            下一组
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </nav>
                </c:if>

            </c:when>
            <c:when test="${param.action eq 'history_query'}"> <%-- 只有在执行了查询后才显示 "未找到" --%>
                <div class="alert alert-warning mt-3" role="alert">
                    <i class="bi bi-exclamation-circle-fill me-2"></i>
                    根据您提供的条件，未查询到任何历史工资记录。
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-light mt-3" role="alert">
                    请输入查询条件以开始查询。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>