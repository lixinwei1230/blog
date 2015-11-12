<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<title><sec:authentication property='principal.nickname' /></title>
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8">
				<div class="table-responsive">
					<table class="table">
						<tr>
							<td>主页</td>
							<td><a href="${ctx }/my/page/design?type=HOMEPAGE"
								class="btn btn-primary">配置</a></td>
						</tr>
						<sec:authorize ifAnyGranted="ROLE_SPACE">
							<tr>
								<td>博客侧边栏</td>
								<td><a href="${ctx }/my/page/design?type=BLOG"
									class="btn btn-primary">配置</a></td>
							</tr>
						</sec:authorize>
					</table>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
</body>
<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
</html>