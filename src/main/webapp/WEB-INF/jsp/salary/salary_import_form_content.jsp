<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagetitle mb-3">
    <h1>工资批量导入</h1>
    <nav>
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">首页</a></li>
            <li class="breadcrumb-item active">工资批量导入</li>
        </ol>
    </nav>
</div>

<c:if test="${not empty requestScope.successMessage}">
    <div class="alert alert-success">
        <pre style="white-space: pre-wrap; margin: 0;">${requestScope.successMessage}</pre>
    </div>
</c:if>
<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-danger">${requestScope.errorMessage}</div>
</c:if>

<div class="card">
    <div class="card-body">
        <h5 class="card-title mt-3">上传工资Excel文件</h5>
        <p>请上传符合系统模板的Excel文件 (.xls 或 .xlsx) 来批量导入员工的月度工资数据。</p>

        <form action="${pageContext.request.contextPath}/importSalary" method="post" enctype="multipart/form-data" class="row g-3">
            <div class="col-md-6">
                <label for="yearMonth" class="form-label">工资所属月份 <span class="text-danger">*</span></label>
                <input type="month" class="form-control" id="yearMonth" name="yearMonth" required
                       value="<%= java.time.YearMonth.now().toString() %>">
            </div>

            <div class="col-md-12">
                <label for="salaryFile" class="form-label">选择Excel文件 <span class="text-danger">*</span></label>
                <input class="form-control" type="file" id="salaryFile" name="salaryFile" accept=".xls,.xlsx" required>
            </div>

            <div class="col-12 mt-4">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-upload me-1"></i> 开始导入
                </button>
            </div>
        </form>
    </div>
</div>

<div class="card mt-4">
    <div class="card-body">
        <h5 class="card-title mt-3">导入说明和模板</h5>
        <ul>
            <li>请严格按照模板的 **15列顺序** 填写数据，第一行为标题行。</li>
            <li>**员工编号 (B列)** 为必填项，且必须已存在于员工信息中。</li>
            <li>系统将根据 **员工编号** 和您选择的 **工资所属月份** 来创建或覆盖工资记录。</li>
            <li>系统将 **自动计算** 个人所得税和实发工资，Excel中的“扣税”和“实发工资”列将被 **忽略**。</li>
            <li>
                <a href="${pageContext.request.contextPath}/exp/模板.xlsx" download="工资导入模板.xlsx">
                    [点击此处下载Excel模板文件]
                </a>
            </li>
        </ul>
        <h6>模板列顺序:</h6>
        <p><code>部门 | 员工编号 | 姓名 | 本月应出勤天数 | 实际出勤 | 基本工资 | 岗位津贴 | 午餐补贴 | 加班工资 | 全勤工资 | 扣社保 | 扣公积金 | 扣税 | 迟到、请假等扣 | 实发工资</code></p>
    </div>
</div>