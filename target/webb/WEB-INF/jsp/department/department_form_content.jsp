<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>${empty department.id ? '添加新部门' : '编辑部门'}</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/departments?action=list">部门列表</a></li>
            <li class="breadcrumb-item active">${empty department.id ? '添加' : '编辑'}</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger" role="alert">
            ${requestScope.errorMessage}
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">${empty department.id ? '填写部门信息' : '修改部门信息'}</h5>

        <form action="${pageContext.request.contextPath}/departments" method="post" class="row g-3 needs-validation" novalidate>
            <%-- 隐藏域，用于提交表单动作 --%>
            <input type="hidden" name="action" value="${formAction}">

            <%-- 如果是编辑，则包含部门ID --%>
            <c:if test="${not empty department.id}">
                <input type="hidden" name="id" value="${department.id}">
            </c:if>

            <div class="col-md-12">
                <label for="deptName" class="form-label">部门名称 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="deptName" name="deptName" value="<c:out value='${department.deptName}'/>" required>
                <div class="invalid-feedback">
                    请输入部门名称。
                </div>
            </div>

            <div class="col-md-12">
                <label for="parentDeptId" class="form-label">上级部门</label>
                <select class="form-select" id="parentDeptId" name="parentDeptId">
                    <option value="0" ${department.parentDeptId eq null ? 'selected' : ''}>-- 无上级部门 (顶级部门) --</option>
                    <c:forEach var="parent" items="${allDepartments}">
                        <%-- 编辑时，不能选择自己作为上级部门 --%>
                        <c:if test="${department.id ne parent.id}">
                            <option value="${parent.id}" ${department.parentDeptId eq parent.id ? 'selected' : ''}>
                                <c:out value="${parent.deptName}"/> (ID: ${parent.id})
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-check-circle me-1"></i> ${empty department.id ? '确认添加' : '确认修改'}
                </button>
                <a href="${pageContext.request.contextPath}/departments?action=list" class="btn btn-secondary">
                    <i class="bi bi-x-circle me-1"></i> 取消
                </a>
            </div>
        </form>
    </div>
</div>

<%-- Bootstrap 表单验证脚本 --%>
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