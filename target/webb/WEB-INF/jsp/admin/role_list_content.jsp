<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>角色管理</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">角色列表</li>
        </ol>
    </nav>
</div>

<%-- 通用消息显示 --%>
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>

<div class="card">
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3 mt-3">
            <h5 class="card-title mb-0">系统角色列表</h5>
            <a href="${pageContext.request.contextPath}/admin?action=add_role_form" class="btn btn-primary btn-sm">
                <i class="bi bi-plus-circle me-1"></i> 添加新角色
            </a>
        </div>

        <table class="table table-hover">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">角色名称</th>
                <th scope="col">描述</th>
                <th scope="col" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="role" items="${roles}">
                <tr>
                    <th scope="row">${role.id}</th>
                    <td><c:out value="${role.roleName}"/></td>
                    <td><c:out value="${role.description}"/></td>
                    <td class="text-center">
                        <a href="${pageContext.request.contextPath}/admin?action=edit_role_form&id=${role.id}" class="btn btn-outline-primary btn-sm me-1" title="编辑">
                            <i class="bi bi-pencil-square"></i> 编辑
                        </a>
                        <a href="${pageContext.request.contextPath}/admin?action=delete_role&id=${role.id}" class="btn btn-outline-danger btn-sm" title="删除"
                           onclick="return confirm('确定要删除角色 “${fn:escapeXml(role.roleName)}” 吗？如果该角色已分配给用户，将无法删除。');">
                            <i class="bi bi-trash3"></i> 删除
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>