<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="t" uri="/token"%>
<%@taglib prefix="u" uri="/url"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>注册</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
	<div class="container ">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8">
				<form action="${ctx }/register" method="post" id="registerForm">
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
					<div class="form-group">
						<label for="inputnickname"><spring:message
								code="page.item.username" />
						</label> <input type="text" class="form-control" id="inputnickname"
							name="username" value="<c:out value="${user.nickname }"/>"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.username"/>">
						<span class="help-block"><spring:message
								code="page.register.username.length" arguments="2,8"
								argumentSeparator="," />
						</span>
					</div>
					<div class="form-group">
						<label for="inputEmail"><spring:message
								code="page.item.email" />
						</label> <input type="text" class="form-control" id="inputEmail"
							name="email" value="<c:out value="${user.email }"/>"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.email"/>">
					</div>
					<div class="form-group">
						<label for="inputPassword"><spring:message
								code="page.item.password" />
						</label> <input type="password" class="form-control" id="inputPassword"
							name="password"
							placeholder="<spring:message code="global.pleaseInput"/><spring:message code="page.item.password"/>">
						<span class="help-block"><spring:message
								code="page.register.password.length" arguments="6,16"
								argumentSeparator="," />
						</span>
					</div>
					<div class="form-group">
						<label for="reInputPassword"><spring:message
								code="page.register.reInputPassword" />
						</label> <input type="password" class="form-control" id="reInputPassword"
							placeholder="<spring:message code="page.register.reInputPassword"/>">
					</div>
					<div class="form-group">
						<label for="inputValidateCode"><spring:message
								code="page.item.validateCode" />
						</label> <input type="text" class="form-control" id="inputValidateCode"
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
						<spring:message code="page.register" />
					</button>
				</form>
				<div style="text-align: right; margin-top: 10px">
					已有帐号？<a href="<u:url/>/login">点我登录</a>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$("#submit-register").click(function(){
			$(".errorField").remove();
			var name = $("#inputnickname").val();
			if($.trim(name).length == 0){
				writeErrorField("表单错误","用户名不能为空");
				return;
			}
			if(name.length <2 || name.length >8){
				writeErrorField("表单错误","用户名长度在2～8之间");
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
			var password = $("#inputPassword").val();
			if($.trim(password).length == 0){
				writeErrorField("表单错误","密码不能为空");
				return;
			}
			if(password.length <6 || password.length > 16){
				writeErrorField("表单错误","密码长度在6~16之间");
				return;
			}
			var repassword = $("#reInputPassword").val();
			if(password != repassword){
				writeErrorField("表单错误","两次密码不一致");
				return;
			}
			var validateCode = $("#inputValidateCode").val();
			if($.trim(validateCode).length == 0){
				writeErrorField("表单错误","请输入验证码");
				return;
			}
			$("#registerForm").submit();
		});
		
		function writeErrorField(title,content){
			$(".errorField").remove();
			var html = getHtml(title,content);
			$("#registerForm").prepend(html);
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