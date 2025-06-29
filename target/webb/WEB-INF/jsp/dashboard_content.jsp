<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>仪表盘</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">仪表盘</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty sessionScope.currentUser}">
    <div class="alert alert-info" role="alert">
        <h4 class="alert-heading">欢迎回来, ${sessionScope.username}!</h4>
        <p>您的角色是：<strong>${sessionScope.roleName}</strong>。</p>
        <hr>
        <p class="mb-0">请从左侧导航栏选择您需要操作的功能模块。</p>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">快速开始</h5>
                    <p class="card-text">您可以通过左侧菜单快速访问系统的各项功能。</p>
                        <%-- 根据角色显示不同的快捷方式 --%>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">系统通知</h5>
                    <p class="card-text">暂无新的系统通知。</p>
                </div>
            </div>
        </div>
    </div>
</c:if>