<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="pagetitle mb-3">
    <h1>安全审计日志</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">审计日志</li>
        </ol>
    </nav>
</div>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">日志筛选</h5>

        <form action="${pageContext.request.contextPath}/audit" method="get" class="row g-3">
            <input type="hidden" name="action" value="view_logs">

            <div class="col-md-3">
                <label for="usernameFilter" class="form-label">操作用户:</label>
                <input type="text" class="form-control" id="usernameFilter" name="username" value="${param.username}" placeholder="用户名(可模糊)">
            </div>
            <div class="col-md-3">
                <label for="actionTypeFilter" class="form-label">操作类型:</label>
                <input type="text" class="form-control" id="actionTypeFilter" name="actionType" value="${param.actionType}" placeholder="例如: 用户登录成功">
            </div>
            <div class="col-md-3">
                <label for="startDateFilter" class="form-label">开始日期:</label>
                <input type="date" class="form-control" id="startDateFilter" name="startDate" value="${param.startDate}">
            </div>
            <div class="col-md-3">
                <label for="endDateFilter" class="form-label">结束日期:</label>
                <input type="date" class="form-control" id="endDateFilter" name="endDate" value="${param.endDate}">
            </div>

            <div class="col-12 text-end">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-search me-1"></i> 筛选日志
                </button>
                <a href="${pageContext.request.contextPath}/audit?action=view_logs" class="btn btn-secondary">清除条件</a>
            </div>
        </form>
    </div>
</div>


<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">日志记录 (共 ${totalRecords} 条)</h5>

        <c:choose>
            <c:when test="${not empty logs}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">时间</th>
                            <th scope="col">操作用户</th>
                            <th scope="col">IP地址</th>
                            <th scope="col">操作类型</th>
                            <th scope="col">状态</th>
                            <th scope="col">操作对象</th>
                            <th scope="col">详情</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="log" items="${logs}" varStatus="status">
                            <tr>
                                <th scope="row">${status.count + (currentPage - 1) * 20}</th>
                                <td><fmt:formatDate value="${log.logTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><c:out value="${log.username}"/></td>
                                <td><c:out value="${log.ipAddress}"/></td>
                                <td><c:out value="${log.actionType}"/></td>
                                <td>
                                    <c:if test="${log.status eq 'SUCCESS'}"><span class="badge bg-success">成功</span></c:if>
                                    <c:if test="${log.status eq 'FAILURE'}"><span class="badge bg-danger">失败</span></c:if>
                                </td>
                                <td><c:out value="${log.targetResource}"/></td>
                                <td><c:out value="${log.details}"/></td>
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
                                        <a class="page-link px-2 text-center" style="min-width: 60px;" href="${pageContext.request.contextPath}/audit?action=view_logs&pageNum=${startPage - 1}&${searchParams}" title="上一组页码">
                                            上一组
                                        </a>
                                    </li>
                                </c:if>

                                <%-- 上一页按钮 --%>
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link px-2 text-center" style="min-width: 50px;" href="${pageContext.request.contextPath}/audit?action=view_logs&pageNum=${currentPage - 1}&${searchParams}" title="上一页">
                                        上页
                                    </a>
                                </li>

                                <%-- 页码按钮组 --%>
                                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link px-2 text-center" style="min-width: 40px;" href="${pageContext.request.contextPath}/audit?action=view_logs&pageNum=${i}&${searchParams}">${i}</a>
                                    </li>
                                </c:forEach>

                                <%-- 下一页按钮 --%>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link px-2 text-center" style="min-width: 50px;" href="${pageContext.request.contextPath}/audit?action=view_logs&pageNum=${currentPage + 1}&${searchParams}" title="下一页">
                                        下页
                                    </a>
                                </li>

                                <%-- 下一组按钮 --%>
                                <c:if test="${endPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link px-2 text-center" style="min-width: 60px;" href="${pageContext.request.contextPath}/audit?action=view_logs&pageNum=${endPage + 1}&${searchParams}" title="下一组页码">
                                            下一组
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </nav>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning mt-3" role="alert">
                    <i class="bi bi-exclamation-circle-fill me-2"></i>
                    未查询到任何符合条件的审计日志记录。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>