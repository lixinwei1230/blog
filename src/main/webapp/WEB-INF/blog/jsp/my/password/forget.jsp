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
<title>忘记密码</title>
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
				<form action="${ctx }/password/forget" method="post">
					<c:if test="${error != null }">
						<div style="margin-top: 10px; margin-bottom: 10px"
							class="alert alert-danger errorField" role="alert">
							<span class="glyphicon glyphicon-exclamation-sign"
								aria-hidden="true"></span> <span class="sr-only"><spring:message
									code="global.error" />:</span>
							<spring:message code="${error.code }"
								arguments="${error.params }" />
						</div>
					</c:if>
					<c:if test="${success != null }">
						<div class="alert alert-success" role="alert">${success }</div>
					</c:if>
					<div class="form-group">
						<label for="inputnickname"><spring:message
								code="page.item.username" /></label> <input type="text"
							class="form-control" id="inputnickname" name="name"
							value="<c:out value="${name }"/>"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.username"/>">
						<span class="help-block"><spring:message
								code="page.register.username.length" arguments="2,6"
								argumentSeparator="," /></span>
					</div>
					<div class="form-group">
						<label for="inputEmail"><spring:message
								code="page.item.email" /></label> <input type="text"
							class="form-control" id="inputEmail" name="email"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.email"/>">
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
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<t:token />
					<button type="button" class="btn btn-lg btn-primary btn-block"
						id="submit-register">
						<spring:message code="page.password.forget" />
					</button>
				</form>
			</div>
		</div>

	</div>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$("#submit-register").click(function(){
			$(".errorField").remove();
			var name = $("#inputnickname").val();
			if($.trim(name).length == 0){
				writeErrorField("表单错误","用户名不能为空");
				return;
			}
			if(name.length <2 || name.length >6){
				writeErrorField("表单错误","用户名长度在2～6之间");
				return;
			}
			var email = $("#inputEmail").val();
			if($.trim(email).length == 0){
				writeErrorField("表单错误","邮箱不能为空");
				return;
			}
			if(email.indexOf("@") == -1
					|| email.indexOf(".") == -1 || email.length > 100){
				writeErrorField("表单错误","邮箱不合法");
				return;
			}
			$("form").submit();
		});
		
		function writeErrorField(title,content){
			$(".errorField").remove();
			var html = getHtml(title,content);
			$("form").prepend(html);
		}
		
		function getHtml(title,content){
			var html = '<div  style="margin-top: 10px; margin-bottom: 10px" ';
			html += 'class="alert alert-danger errorField" role="alert">';
			html += '<span class="glyphicon glyphicon-exclamation-sign"';
			html += 'aria-hidden="true"></span> <span class="sr-only">'+title+':</span>';
			html += content;
			html += '</div>';
			return html ;
		}
	</script>
</body>
</html>