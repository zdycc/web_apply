<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>${user.id > 0 ? '编辑用户' : '添加新用户'}</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin?action=list_users">用户列表</a></li>
            <li class="breadcrumb-item active">${user.id > 0 ? '编辑' : '添加'}</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${requestScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<%-- 用户信息编辑表单 --%>
<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">${user.id > 0 ? '修改用户信息' : '填写新用户信息'}</h5>

        <form action="${pageContext.request.contextPath}/admin" method="post" class="row g-3 needs-validation" novalidate>
            <input type="hidden" name="action" value="${user.id > 0 ? 'edit_user_submit' : 'add_user_submit'}">
            <c:if test="${user.id > 0}">
                <input type="hidden" name="id" value="${user.id}">
            </c:if>

            <div class="col-md-6">
                <label for="username" class="form-label">用户名 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="username" name="username" value="<c:out value='${user.username}'/>" required <c:if test="${user.id > 0}">readonly</c:if>>
                <div class="invalid-feedback">请输入用户名。</div>
                <c:if test="${user.id > 0}"><div class="form-text">用户名不可修改。</div></c:if>
            </div>

            <c:if test="${user.id == 0}"> <%-- 只有新增用户时才显示密码输入框 --%>
                <div class="col-md-6">
                    <label for="password" class="form-label">密码 <span class="text-danger">*</span></label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <div class="invalid-feedback">请输入密码。</div>
                    <div class="form-text">长度8位以上，包含数字、大小写字母、特殊字符。</div>
                </div>
                <div class="col-md-6">
                    <label for="confirmPassword" class="form-label">确认密码 <span class="text-danger">*</span></label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                    <div class="invalid-feedback">请再次输入密码。</div>
                </div>
            </c:if>

            <div class="col-md-6">
                <label for="roleId" class="form-label">角色 <span class="text-danger">*</span></label>
                <select class="form-select" id="roleId" name="roleId" required>
                    <option value="" disabled ${empty user.roleId ? 'selected' : ''}>-- 请选择角色 --</option>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.id}" ${user.roleId == role.id ? 'selected' : ''}>
                            <c:out value="${role.roleName}"/>
                        </option>
                    </c:forEach>
                </select>
                <div class="invalid-feedback">请选择一个角色。</div>
            </div>

            <div class="col-md-6">
                <label for="employeeId" class="form-label">关联员工 (可选)</label>
                <select class="form-select" id="employeeId" name="employeeId">
                    <option value="0">-- 不关联任何员工 --</option>
                    <c:forEach var="employee" items="${employees}">
                        <option value="${employee.id}" ${user.employeeId == employee.id ? 'selected' : ''}>
                            <c:out value="${employee.name}"/> (<c:out value="${employee.employeeNumber}"/>)
                        </option>
                    </c:forEach>
                </select>
                <div class="form-text">将此系统账户与一个员工档案关联。</div>
            </div>

            <div class="col-md-6">
                <label class="form-label">账户状态</label>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="isActive" name="isActive" ${user.isActive or empty user.id ? 'checked' : ''}>
                    <label class="form-check-label" for="isActive">${user.isActive or empty user.id ? '已激活' : '已禁用'}</label>
                </div>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-check-circle me-1"></i> ${user.id > 0 ? '保存更改' : '确认添加'}
                </button>
                <a href="${pageContext.request.contextPath}/admin?action=list_users" class="btn btn-secondary">
                    <i class="bi bi-x-circle me-1"></i> 取消
                </a>
            </div>
        </form>
    </div>
</div>

<%-- 密码重置表单 (仅在编辑用户时显示) --%>
<c:if test="${user.id > 0}">
    <div class="card">
        <div class="card-body">
            <h5 class="card-title mt-3">重置密码</h5>
            <form action="${pageContext.request.contextPath}/admin" method="post" class="row g-3 needs-validation" novalidate>
                <input type="hidden" name="action" value="reset_password">
                <input type="hidden" name="id" value="${user.id}">

                <div class="col-md-6">
                    <label for="resetPassword" class="form-label">新密码 <span class="text-danger">*</span></label>
                    <input type="password" class="form-control" id="resetPassword" name="password" required>
                    <div class="invalid-feedback">请输入新密码。</div>
                    <div class="form-text">长度8位以上，包含数字、大小写字母、特殊字符。</div>
                </div>
                <div class="col-md-6">
                    <label for="resetConfirmPassword" class="form-label">确认新密码 <span class="text-danger">*</span></label>
                    <input type="password" class="form-control" id="resetConfirmPassword" name="confirmPassword" required>
                    <div class="invalid-feedback">请再次输入新密码。</div>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-warning">
                        <i class="bi bi-key-fill me-1"></i> 确认重置密码
                    </button>
                </div>
            </form>
        </div>
    </div>
</c:if>

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