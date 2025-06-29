<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>修改密码</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">修改密码</li>
        </ol>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title mt-3">为您的账户设置新密码</h5>

                <%-- 显示来自 session 的提示信息 (用于强制修改密码时) --%>
                <c:if test="${not empty sessionScope.infoMessage}">
                    <div class="alert alert-info">${sessionScope.infoMessage}</div>
                    <c:remove var="infoMessage" scope="session"/>
                </c:if>

                <%-- 显示操作结果消息 --%>
                <c:if test="${not empty requestScope.successMessage}"><div class="alert alert-success">${requestScope.successMessage}</div></c:if>
                <c:if test="${not empty requestScope.errorMessage}"><div class="alert alert-danger">${requestScope.errorMessage}</div></c:if>

                <form action="${pageContext.request.contextPath}/changePassword" method="post" class="needs-validation" novalidate>
                    <div class="mb-3">
                        <label for="oldPassword" class="form-label">当前密码 <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="oldPassword" name="oldPassword" required>
                    </div>
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">新密码 <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                        <div class="form-text">长度8位以上，包含数字、大小写字母、特殊字符。</div>
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">确认新密码 <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary">确认修改</button>
                </form>
            </div>
        </div>
    </div>
</div>