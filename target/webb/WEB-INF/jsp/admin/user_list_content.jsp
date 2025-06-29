<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>用户管理</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">用户列表</li>
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
            <h5 class="card-title mb-0">系统用户列表</h5>
            <a href="${pageContext.request.contextPath}/admin?action=add_user_form" class="btn btn-primary btn-sm">
                <i class="bi bi-person-plus-fill me-1"></i> 添加新用户
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty users && fn:length(users) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-sm">
                        <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">用户名</th>
                            <th scope="col">关联员工ID</th>
                            <th scope="col">角色</th>
                            <th scope="col">状态</th>
                            <th scope="col">最后密码修改日期</th>
                            <th scope="col">创建日期</th>
                            <th scope="col" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%-- 在循环外获取当前时间，用于比较锁定时间是否已过 --%>
                        <c:set var="now" value="<%= new java.util.Date() %>" />
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <th scope="row">${user.id}</th>
                                <td><c:out value="${user.username}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty user.employeeId}">${user.employeeId}</c:when>
                                        <c:otherwise><span class="text-muted">未关联</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty user.role}">
                                            <c:out value="${user.role.roleName}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-danger">角色信息错误</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:choose>
                                        <%-- 优先判断是否被锁定 --%>
                                        <c:when test="${not empty user.lockoutUntil and user.lockoutUntil.time > now.time}">
                                            <span class="badge bg-danger">已锁定</span>
                                        </c:when>
                                        <%-- 如果未锁定，再判断是激活还是禁用 --%>
                                        <c:when test="${user.getIsActive()}">
                                            <span class="badge bg-success">已激活</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">已禁用</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td><fmt:formatDate value="${user.lastPasswordChangeDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd"/></td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/admin?action=edit_user_form&id=${user.id}" class="btn btn-outline-primary btn-sm" title="编辑">
                                        <i class="bi bi-pencil-square"></i> 编辑
                                    </a>
                                        <%-- 如果用户被锁定，则显示解锁按钮 --%>
                                    <c:if test="${not empty user.lockoutUntil and user.lockoutUntil.time > now.time}">
                                        <a href="${pageContext.request.contextPath}/admin?action=unlock_user&id=${user.id}" class="btn btn-outline-warning btn-sm" title="解锁"
                                           onclick="return confirm('确定要为用户 “${fn:escapeXml(user.username)}” 解锁吗？');">
                                            <i class="bi bi-unlock-fill"></i> 解锁
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info mt-3" role="alert">
                    当前没有系统用户。请先添加一个用户。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>