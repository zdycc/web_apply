<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>公司人员工资管理系统 - <c:out value="${pageTitle eq null ? '首页' : pageTitle}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { padding-top: 56px; /* 为固定导航栏预留空间 */ }
        .sidebar {
            position: fixed;
            top: 56px;
            bottom: 0;
            left: 0;
            z-index: 1000;
            width: 250px;
            padding: 20px;
            padding-top: 10px;
            background-color: #f8f9fa;
            border-right: 1px solid #dee2e6;
            overflow-y: auto;
        }
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        .navbar-brand { font-weight: bold; }
        .sidebar .list-group-item {
            border-radius: .25rem;
            margin-bottom: 5px;
        }
        .sidebar .list-group-item.active {
            background-color: #0d6efd;
            border-color: #0d6efd;
        }
        .sidebar .list-group-item-action:hover, .sidebar .list-group-item-action:focus {
            background-color: #e9ecef;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">工资管理系统</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav ms-auto">
                <c:if test="${not empty sessionScope.currentUser}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="bi bi-person-circle"></i> ${sessionScope.username} (${sessionScope.roleName})
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownMenuLink">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/changePassword">修改密码</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">退出登录</a></li>
                        </ul>
                    </li>
                </c:if>
                <c:if test="${empty sessionScope.currentUser}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">登录</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>

<div class="d-flex">
    <nav class="sidebar">
        <div class="list-group">
            <h5 class="text-muted ps-2 pt-2 pb-1 small text-uppercase">主导航</h5>
            <a href="${pageContext.request.contextPath}/home" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "home"}'>active</c:if>">
                <i class="bi bi-house-door-fill me-2"></i> 首页
            </a>
            <%-- layout.jsp 中 --%>
            <c:if test="${sessionScope.roleName eq '系统管理员'}">
                <h6 class="text-muted ps-2 pt-3 pb-1 small text-uppercase">系统管理</h6>
                <a href="${pageContext.request.contextPath}/admin?action=list_users" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "users"}'>active</c:if>">
                    <i class="bi bi-people-fill me-2"></i> 用户管理
                </a>
                <a href="${pageContext.request.contextPath}/admin?action=list_roles" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "roles"}'>active</c:if>">
                    <i class="bi bi-person-rolodex me-2"></i> 角色管理
                </a>
            </c:if>
            <c:if test="${sessionScope.roleName eq '人事管理员' or sessionScope.roleName eq '系统管理员'}">
                <h6 class="text-muted ps-2 pt-3 pb-1 small text-uppercase">人事管理</h6>
                <a href="${pageContext.request.contextPath}/departments?action=list" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "departments"}'>active</c:if>">
                    <i class="bi bi-diagram-3-fill me-2"></i> 部门管理
                </a>
                <a href="${pageContext.request.contextPath}/employees?action=list" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "personnel"}'>active</c:if>">
                    <i class="bi bi-person-vcard-fill me-2"></i> 人员管理
                </a>
                <a href="${pageContext.request.contextPath}/specialDeductions?action=list_employees_for_deduction" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "special_deductions_main" or activeNavItem eq "special_deductions_annual" or activeNavItem eq "special_deductions_dependent"}'>active</c:if>">
                    <i class="bi bi-card-checklist me-2"></i> 专项附加扣除管理
                </a>
            </c:if>
            <c:if test="${sessionScope.roleName eq '财务管理员' or sessionScope.roleName eq '系统管理员' or sessionScope.roleName eq '总经理'}">
                <h6 class="text-muted ps-2 pt-3 pb-1 small text-uppercase">财务管理</h6>
                <a href="${pageContext.request.contextPath}/salaries?action=list_current_month" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "salaries"}'>active</c:if>">
                    <i class="bi bi-wallet2 me-2"></i> 月度工资管理
                </a>
                <a href="${pageContext.request.contextPath}/salaries?action=history_query" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "salary_history"}'>active</c:if>">
                    <i class="bi bi-search me-2"></i> 历史工资查询
                </a>
                <a href="${pageContext.request.contextPath}/importSalary" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "salary_import"}'>active</c:if>">
                    <i class="bi bi-file-earmark-arrow-up-fill me-2"></i> 工资批量导入
                </a>
            </c:if>
            <c:if test="${sessionScope.roleName eq '审计员' or sessionScope.roleName eq '系统管理员'}">
                <h6 class="text-muted ps-2 pt-3 pb-1 small text-uppercase">审计</h6>
                <a href="${pageContext.request.contextPath}/audit?action=view_logs" class="list-group-item list-group-item-action <c:if test='${activeNavItem eq "audit"}'>active</c:if>">
                    <i class="bi bi-journal-check me-2"></i> 审计日志
                </a>
            </c:if>
        </div>
    </nav>

    <main class="main-content flex-grow-1">
        <div class="container-fluid">
            <%-- 动态包含页面内容 --%>
            <c:if test="${not empty contentPage}">
                <jsp:include page="${contentPage}" />
            </c:if>
            <c:if test="${empty contentPage and empty sessionScope.currentUser}">
                <div class="alert alert-warning" role="alert">
                    您尚未登录，请 <a href="${pageContext.request.contextPath}/login" class="alert-link">点击这里登录</a>。
                </div>
            </c:if>
            <c:if test="${empty contentPage and not empty sessionScope.currentUser}">
                <p>欢迎使用系统！请从左侧选择功能。</p>
            </c:if>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>