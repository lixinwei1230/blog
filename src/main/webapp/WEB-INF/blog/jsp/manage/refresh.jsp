<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<sec:authentication property='principal.space' var="space" />
<title>文件刷新</title>
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8  text">
				<c:choose>
					<c:when test="${not empty refreshs }">
						<div class="table-responsive">
							<table class="table">
								<tr>
									<th>对象名</th>
									<th>&nbsp;</th>
								</tr>
								<c:forEach items="${refreshs }" var="r">
									<tr>
										<td>${r }</td>
										<td><a href="javascript:void(0)" onclick="refresh('${r}')"><span class="glyphicon glyphicon-refresh"
											aria-hidden="true"></span></a></td>
									</tr>
								</c:forEach>
								<tr>
									<td><strong>全部</strong></td>
									<td><a href="javascript:void(0)" onclick="refresh('&&all')"><span class="glyphicon glyphicon-refresh"
										aria-hidden="true"></span></a></td>
								</tr>
							</table>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">当前没有可刷新对象</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		function refresh(bean){
			$.post(contextPath + "/manage/refresh",{"bean":bean},function callBack(data){
				if(data.success){
					$.messager.popup("刷新成功");
				}else{
					$.messager.popup("刷新失败");
				}
			});
		}
	</script>
</body>
<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
</html>
