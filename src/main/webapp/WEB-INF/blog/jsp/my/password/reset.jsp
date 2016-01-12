<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="t" uri="/token"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>重置密码</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
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
				<div class="alert alert-warning">
					<spring:message code="page.password.reset.tip" />
				</div>
				<form action="${ctx }/password/reset" id="resetPasswordForm"
					method="post">
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
					<input type="hidden" name="userId" value="${userId }" /> <input
						type="hidden" name="code" value="${code }" /> <input
						type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<t:token />
					<button type="button" id="check-submit"
						class="btn btn-lg btn-primary btn-block" id="submit-register">
						<spring:message code="page.password.reset" />
					</button>
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#check-submit").click(function(){
			if($("#reInputPassword").val() != $("#inputPassword").val()){
				$.messager.popup("两次密码不一致");
				return ;
			}
			$("#resetPasswordForm").submit();
		});
	});
</script>
</body>
</html>