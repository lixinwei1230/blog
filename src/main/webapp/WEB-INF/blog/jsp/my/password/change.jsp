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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title><sec:authentication property='principal.nickname' /></title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
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
					<div class="alert alert-success" role="alert">
						<spring:message code="${success.code }"
							arguments="${success.params }" />
					</div>
				</c:if>
				<form action="${ctx }/my/password/change" id="changePasswordForm"
					method="post">
					<div class="form-group">
						<label><spring:message code="page.item.oldPassword" /></label> <input
							type="password" class="form-control" name="oldPassword"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.oldPassword"/>">
						<span class="help-block"><spring:message
								code="page.register.password.length" arguments="6,16"
								argumentSeparator="," /></span>
					</div>
					<div class="form-group">
						<label for="inputPassword"><spring:message
								code="page.item.password" /></label> <input type="password"
							class="form-control" id="inputPassword" name="newPassword"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.password"/>">
						<span class="help-block"><spring:message
								code="page.register.password.length" arguments="6,16"
								argumentSeparator="," /></span>
					</div>
					<div class="form-group">
						<label for="reInputPassword"><spring:message
								code="page.register.reInputPassword" /></label> <input type="password"
							class="form-control" id="reInputPassword"
							placeholder="<spring:message code="page.register.reInputPassword"/>">
					</div>
					<div class="form-group">
						<label for="inputValidateCode"><spring:message
								code="page.item.validateCode" /></label> <input type="text"
							class="form-control" id="inputValidateCode"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.validateCode"/>"
							name="validateCode"> <img src="${ctx }/captcha/"
							class="img-responsive"
							onclick="this.src='${ctx}/captcha/'+new Date().getTime()" />
					</div>
					<input type="hidden" name="userId" value="${userId }" /> <input
						type="hidden" name="code" value="${code }" /> <input
						type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<t:token />
					<button type="button" class="btn btn-lg btn-primary btn-block"
						id="submit-change">
						<spring:message code="page.password.change" />
					</button>
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$(document).ready(function (){
			$("#submit-change").bind("click",function(){
				$("#changePasswordForm").submit();
			});
		});
	</script>
</body>
</html>