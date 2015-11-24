<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>500</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
			<div class="text-align:center" style="margin-top: 20px">
				<div class="alert alert-warning ">
					非常抱歉，在授权过程中遇到了一个错误，我已经记录该错误，您可以尝试重新登录，如果反复出现该问题，则该服务当前不可用，我会尽快解决</div>
				<div>
					<button type="button" class="btn btn-primary"
						onclick="toLogin('${OAUTH_TYPE}')">重新登录</button>
					<button type="button" style="margin-left: 20px"
						class="btn btn-primary" onclick="toHomePage()">返回首页</button>
				</div>
			</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		function toLogin(type){
			if(type == "QQ")
				window.location.href = getUrl()+"/oauth2/qq/login";
			else{
				window.location.href = getUrl()+"/login";
			}
		}
		function toHomePage(){
			window.location.href = getUrl();
		}
	</script>
</body>
</html>





