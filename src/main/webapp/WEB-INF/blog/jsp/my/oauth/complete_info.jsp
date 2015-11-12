<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="t" uri="/token"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>完善账号信息</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
</head>
<body>
	<jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
	<div class="container ">
		<div class="row">
			<div class="col-lg-8 col-md-8">
				<c:if test="${error != null }">
					<div style="margin-top: 10px; margin-bottom: 10px"
						class="alert alert-danger errorField" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign"
							aria-hidden="true"></span> <span class="sr-only"><spring:message
								code="global.error" />:</span>
						<spring:message code="${error.code }" arguments="${error.params }" />
					</div>
				</c:if>
				<c:if test="${success != null }">
					<div style="margin-top: 10px; margin-bottom: 10px"
						class="alert alert-success" role="alert">
						<spring:message code="${success.code }" arguments="${success.params }" />
					</div>
				</c:if>
				<div class="alert alert-warning">
				每个账号只能完善一次信息,请谨慎操作
				</div>
				<form action="${ctx }/oauth/completeInfo" method="post">
					<div id="id-container">
						<div class="form-group">
							<label>用户名</label> <input type="text" class="form-control"
								name="username"
								placeholder="请输入用户名(2-8个字符)"
								value="">
						</div>
						<div class="form-group">
							<label>密码</label> <input type="password" class="form-control"
								name="password"
								id  = "password"
								placeholder="请输入密码(6~16个字符)"
								value="">
						</div>
						<div class="form-group">
							<label>再次输入密码</label> <input type="password" class="form-control"
								id="repassword"
								placeholder="请输入密码"
								value="">
						</div>
					</div>
					<t:token />
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
						<input type="hidden" name="code" value="${code }" />
						<input type="hidden" name="userid" value="${userid }">
						<input type="hidden" name="email" value="email@example.com">
					<button type="button" class="btn btn-lg btn-primary btn-block"
						id="submit-register" onclick="toSubmit()">完善信息</button>
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		function toSubmit(){
			var pass = $("#password").val();
			var repass = $("#repassword").val();
			if(pass == repass){
				$("form").submit();
			}else{
				$.messager.alert("两次输入的密码不一致");
			}
		}
	</script>
</body>
</html>