<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>发生错误</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">错误</li>
        </ol>
    </nav>
</div>

<div class="alert alert-danger" role="alert">
    <h4 class="alert-heading">系统遇到问题！</h4>
    <p>
        <c:choose>
            <c:when test="${not empty requestScope.errorMessage}">
                <c:out value="${requestScope.errorMessage}"/>
            </c:when>
            <c:when test="${not empty param.errorMessage}"> <%-- 用于从URL参数传递的简单错误消息 --%>
                <c:out value="${param.errorMessage}"/>
            </c:when>
            <c:when test="${not empty pageContext.errorData}">
                发生了一个错误： <c:out value="${pageContext.errorData.throwable.message}"/> <br/>
                请求的URI： <c:out value="${pageContext.errorData.requestURI}"/> <br/>
            </c:when>
            <c:otherwise>
                抱歉，系统处理您的请求时发生了一个未知的错误。请稍后重试或联系管理员。
            </c:otherwise>
        </c:choose>
    </p>
    <hr>
    <p class="mb-0">
        您可以尝试 <a href="${pageContext.request.contextPath}/home" class="alert-link">返回首页</a>。
    </p>
</div>

