<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="pagetitle mb-3">
    <h1>${employee.id > 0 ? '编辑员工信息' : '添加新员工'}</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees?action=list">人员列表</a></li>
            <li class="breadcrumb-item active">${employee.id > 0 ? '编辑' : '添加'}</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
            ${requestScope.errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">${employee.id > 0 ? '修改员工基本信息' : '填写员工基本信息'}</h5>

        <form action="${pageContext.request.contextPath}/employees" method="post" class="row g-3 needs-validation" novalidate>
            <input type="hidden" name="action" value="${formAction}">
            <c:if test="${employee.id > 0}">
                <input type="hidden" name="id" value="${employee.id}">
            </c:if>

            <div class="col-md-6">
                <label for="employeeNumber" class="form-label">员工编号 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="employeeNumber" name="employeeNumber" value="<c:out value='${employee.employeeNumber}'/>" required <c:if test="${employee.id > 0}">readonly</c:if>>
                <div class="invalid-feedback">请输入员工编号。</div>
                <c:if test="${employee.id > 0}"><div class="form-text">员工编号不可修改。</div></c:if>
            </div>

            <div class="col-md-6">
                <label for="name" class="form-label">姓名 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="name" name="name" value="<c:out value='${employee.name}'/>" required>
                <div class="invalid-feedback">请输入姓名。</div>
            </div>

            <div class="col-md-6">
                <label for="idCardNumber" class="form-label">身份证号 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="idCardNumber" name="idCardNumber" value="<c:out value='${employee.idCardNumber}'/>" required pattern="^\d{17}(\d|X|x)$">
                <div class="invalid-feedback">请输入有效的18位身份证号。</div>
            </div>

            <div class="col-md-6">
                <label for="phoneNumber" class="form-label">手机号</label>
                <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" value="<c:out value='${employee.phoneNumber}'/>" pattern="^1[3-9]\d{9}$">
                <div class="invalid-feedback">请输入有效的11位手机号码。</div>
            </div>

            <div class="col-md-12">
                <label for="address" class="form-label">住址</label>
                <input type="text" class="form-control" id="address" name="address" value="<c:out value='${employee.address}'/>">
            </div>

            <div class="col-md-6">
                <label for="departmentId" class="form-label">所属部门 <span class="text-danger">*</span></label>
                <select class="form-select" id="departmentId" name="departmentId" required>
                    <option value="" ${employee.departmentId == 0 ? 'selected' : ''} disabled>-- 请选择部门 --</option> <%-- employee.departmentId == 0 (新员工) 或未选择时 --%>
                    <c:forEach var="dept" items="${departments}">
                        <option value="${dept.id}" ${employee.departmentId eq dept.id ? 'selected' : ''}>
                            <c:out value="${dept.deptName}"/>
                        </option>
                    </c:forEach>
                </select>
                <div class="invalid-feedback">请选择所属部门。</div>
            </div>

            <div class="col-md-6">
                <label for="hireDate" class="form-label">入职日期 <span class="text-danger">*</span></label>
                <fmt:formatDate value="${employee.hireDate}" pattern="yyyy-MM-dd" var="formattedHireDate"/>
                <input type="date" class="form-control" id="hireDate" name="hireDate" value="${formattedHireDate}" required>
                <div class="invalid-feedback">请选择入职日期。</div>
            </div>

            <div class="col-md-6">
                <label for="position" class="form-label">岗位</label>
                <input type="text" class="form-control" id="position" name="position" value="<c:out value='${employee.position}'/>">
            </div>

            <div class="col-md-6">
                <label for="jobTitle" class="form-label">职务</label>
                <input type="text" class="form-control" id="jobTitle" name="jobTitle" value="<c:out value='${employee.jobTitle}'/>">
            </div>

            <div class="col-md-6">
                <label for="status" class="form-label">员工状态 <span class="text-danger">*</span></label>
                <select class="form-select" id="status" name="status" required>
                    <option value="active" ${employee.status eq 'active' or empty employee.status ? 'selected' : ''}>在职</option>
                    <option value="on_leave" ${employee.status eq 'on_leave' ? 'selected' : ''}>休假</option>
                    <option value="resigned" ${employee.status eq 'resigned' ? 'selected' : ''}>离职</option>
                </select>
                <div class="invalid-feedback">请选择员工状态。</div>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary me-2">
                    <i class="bi bi-check-circle me-1"></i> ${employee.id > 0 ? '确认修改' : '确认添加'}
                </button>
                <a href="${pageContext.request.contextPath}/employees?action=list" class="btn btn-secondary">
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