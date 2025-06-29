<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>${role.id > 0 ? '编辑角色' : '添加新角色'}</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin?action=list_roles">角色列表</a></li>
            <li class="breadcrumb-item active">${role.id > 0 ? '编辑' : '添加'}</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger">${requestScope.errorMessage}</div>
</c:if>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">${role.id > 0 ? '修改角色信息' : '填写新角色信息'}</h5>

        <form action="${pageContext.request.contextPath}/admin" method="post" class="row g-3 needs-validation" novalidate>
            <input type="hidden" name="action" value="save_role">
            <c:if test="${role.id > 0}">
                <input type="hidden" name="id" value="${role.id}">
            </c:if>

            <div class="col-md-12">
                <label for="roleName" class="form-label">角色名称 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="roleName" name="roleName" value="<c:out value='${role.roleName}'/>" required>
                <div class="invalid-feedback">请输入角色名称。</div>
            </div>

            <div class="col-md-12">
                <label for="description" class="form-label">描述</label>
                <textarea class="form-control" id="description" name="description" rows="3"><c:out value='${role.description}'/></textarea>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-check-circle me-1"></i> ${role.id > 0 ? '保存更改' : '确认添加'}
                </button>
                <a href="${pageContext.request.contextPath}/admin?action=list_roles" class="btn btn-secondary">
                    <i class="bi bi-x-circle me-1"></i> 取消
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>