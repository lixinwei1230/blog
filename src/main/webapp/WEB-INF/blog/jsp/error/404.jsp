<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>404</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<div class="text-align:center" style="margin-top: 20px">
					<div class="alert alert-warning ">
						<spring:message code="error.notFound" />
					</div>
					<div>
						<button type="button" class="btn btn-primary"
							onclick="toHomePage()">返回首页</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		function toHomePage() {
			window.location.href = getUrl();
		}
	</script>
</body>
</html>


