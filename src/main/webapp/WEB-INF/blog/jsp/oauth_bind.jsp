<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="t" uri="/token"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>注册</title>
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
				<div>
					<strong>感谢<c:out value="${oauthUser.nickname }" />的授权，现在你可以绑定已有账号或者一键创建账号
					</strong> <img src="${oauthUser.avatar.url }" class="img-responsive" />
				</div>
				<p>
					<strong>绑定已有账号</strong>
				</p>
				<form action="${ctx }/oauth/bind/specified" method="post">
					<div class="row" style="margin-bottom: 20px">
						<div class="col-lg-8 col-md-8 col-sm-8"
							style="margin-bottom: 10px">
							<input type="text" placeholder="请输入邮箱" name="email" id="mail"
								class="form-control" />
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4">
							<button class="btn btn-primary" type="button" id="mail-send">发送业务码</button>
						</div>
					</div>
					<div class="row" id="code-input-container"
						style="margin-bottom: 20px">
						<div class="col-lg-8 col-md-8 col-sm-8"
							style="margin-bottom: 10px">
							<input type="text" placeholder="请输入业务码" name="code"
								class="form-control" />
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4">
							<button class="btn btn-primary" type="submit">绑定</button>
						</div>
					</div>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<t:token />
					<input id="secretKey" type="hidden" name="secretKey"
						value="${secretKey }" />
				</form>
				<p>
					<strong>或者直接创建账号</strong>
				</p>
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12">
						<form action="${ctx }/oauth/bind/auto" method="post">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
							<t:token />
							<input type="hidden" name="secretKey" value="${secretKey }" />
							<button class="btn btn-primary" type="submit">创建账号</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		var wait=60;
		function time(btn) {
	        if (wait == 0) {
	        	btn.prop("disabled",false);
	            btn.val("发送业务码");
	            wait = 60;
	        } else {
	        	btn.prop("disabled",true);
	        	btn.val(wait+"");
	            wait--;
	            setTimeout(function() {
	                time(btn)
	            },
	            1000)
	        }
	    }
		$(document).ready(function(){
			$("#mail-send").click(function(){
				var btn = $(this);
				time(btn);
				$.post(contextPath+"/oauth/bind/sendCode",
						{"email":$("#mail").val(),"secretKey":$("#secretKey").val()},function callBack(data){
					if(data.success){
						$("#code").val("");
						$("#code-input-container").show();
					}else{
						btn.prop("disabled",false);
						wait = 0;
						$("#code-input-container").hide();
						$.messager.popup(data.result);
					}
				})
			});
		})
	</script>
</body>
</html>