<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pagetitle mb-3">
    <h1>部门管理</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">部门列表</li>
        </ol>
    </nav>
</div>

<%-- 显示成功或错误消息 --%>
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
<c:if test="${not empty requestScope.errorMessage}"> <%-- 也处理来自request的错误信息 --%>
    <div class="alert alert-danger" role="alert">
            ${requestScope.errorMessage}
    </div>
</c:if>


<div class="card">
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3 mt-3">
            <h5 class="card-title mb-0">部门列表</h5>
            <a href="${pageContext.request.contextPath}/departments?action=add_form" class="btn btn-primary btn-sm">
                <i class="bi bi-plus-circle me-1"></i> 添加新部门
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty departments && fn:length(departments) > 0}">
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">部门名称</th>
                        <th scope="col">上级部门ID</th>
                        <th scope="col">创建时间</th>
                        <th scope="col">更新时间</th>
                        <th scope="col" class="text-center">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="dept" items="${departments}" varStatus="status">
                        <tr>
                            <th scope="row">${status.count}</th>
                            <td><c:out value="${dept.deptName}"/></td>
                            <td><c:out value="${dept.parentDeptId eq null ? '无' : dept.parentDeptId}"/></td>
                            <td><c:out value="${dept.createdAt}"/></td>
                            <td><c:out value="${dept.updatedAt}"/></td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/departments?action=edit_form&id=${dept.id}" class="btn btn-outline-primary btn-sm me-1" title="编辑">
                                    <i class="bi bi-pencil-square"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/departments?action=delete&id=${dept.id}" class="btn btn-outline-danger btn-sm" title="删除"
                                   onclick="return confirm('确定要删除部门 “${fn:escapeXml(dept.deptName)}” 吗？此操作不可恢复。');">
                                    <i class="bi bi-trash3"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info" role="alert">
                    当前没有部门信息。请先添加一个部门。
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>