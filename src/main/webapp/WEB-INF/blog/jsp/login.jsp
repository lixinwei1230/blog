<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="u" uri="/url"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>登录</title>
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
	<div class="container">
		<div class="col-lg-8 col-md-8 col-sm-8">
			<fieldset>
				<legend>
					<spring:message code="page.login.pleaseLogin" />
				</legend>
				<c:if
					test="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'] != null }">
					<div style="margin-top: 10px; margin-bottom: 10px"
						class="alert alert-danger" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign"
							aria-hidden="true"></span> <span class="sr-only"><spring:message
								code="global.error" /></span>
						${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'] }
					</div>
				</c:if>
				<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
				<c:if test="${error != null }">
					<div style="margin-top: 10px; margin-bottom: 10px"
						class="alert alert-danger errorField" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign"
							aria-hidden="true"></span> <span class="sr-only"><spring:message
								code="global.error" />:</span>
						<spring:message code="${error.code }" arguments="${error.params }" />
					</div>
				</c:if>
				<form class="form-signin" action="${ctx }/login-check" method="POST">
					<div class="form-group">
						<label>姓名|邮箱</label> <input type="text" class="form-control"
							name="j_username" placeholder="" id="inputName">
					</div>
					<div class="form-group">
						<label>密码</label> <input type="password" class="form-control"
							name="j_password" placeholder="" id="inputPassword">
					</div>
					<div class="form-group">
						<label>第三方登录</label>
						<div>
							<table class="table">
								<tr>
									<td><a href="<u:url/>/oauth2/qq/login"><img
											src="${ctx }/static/imgs/oauth_qq.png" />QQ登录</a>
									</td>
									<td><a href="<u:url/>/oauth2/sina/login"><img
											src="${ctx }/static/imgs/oauth_sina.png" />新浪微博登录</a>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="form-group">
						<input type="checkbox" value="on"
							name="_spring_security_remember_me"> 记住我
					</div>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<button class="btn btn-lg btn-primary btn-block" id="submit-login"
						type="button">登录</button>
				</form>
			</fieldset>
			<div style="text-align: right; margin-top: 10px">
				还没有账号？<a href="<u:url/>/register">立即注册</a>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$("#submit-login").click(function() {
			$(".errorField").remove();
			var name = $("#inputName").val();
			if ($.trim(name).length == 0) {
				writeErrorField("表单错误", "用户名或者邮箱不能为空");
				return;
			}
			var password = $("#inputPassword").val();
			if ($.trim(password).length == 0) {
				writeErrorField("表单错误", "密码不能为空");
				return;
			}
			$(".form-signin").submit();
		});

		function writeErrorField(title, content) {
			$(".errorField").remove();
			var html = getHtml(title, content);
			$("legend").after(html);
		}

		function getHtml(title, content) {
			var html = '<div  style="margin-top: 10px; margin-bottom: 10px" ';
			html += 'class="alert alert-danger errorField" role="alert">';
			html += '<span class="glyphicon glyphicon-exclamation-sign"';
			html += 'aria-hidden="true"></span> <span class="sr-only">'
					+ title + ':</span>';
			html += content;
			html += '</div>';
			return html;
		}
	</script>
</body>
</html>