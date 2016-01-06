<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
<title>梦海澜心</title>
</head>
<body>
	<jsp:include page="/WEB-INF/navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-md-8 text">
				<c:if test="${success != null }">
					<div class="alert alert-success" role="alert">
						<spring:message code="${success.code }"
							arguments="${success.params }" />
					</div>
				</c:if>
				<c:set value="${page.datas }" var="blogs" />
				<c:choose>
					<c:when test="${not empty blogs }">
						<c:forEach var="blog" items="${blogs}">
							<c:set value="${blog.space.user }" var="user"/>
							<div class="well" id="blog-${blog.id }">
								<h4>
									<c:if test="${blog.isPrivate }">
										<span title="私人" class="glyphicon glyphicon-eye-close"
											aria-hidden="true"></span>
									</c:if>
									<c:if test="${blog.from == 'COPIED' }">
									【转载】
									</c:if>
									<a href="<u:url space="${blog.space }"/>/blog/${blog.id}"><c:out
											value="${blog.title }" /> </a>
								</h4>
								<c:set var="tags" value="${blog.tags }" scope="page" />
								<c:if test="${not empty tags }">
									<div style="margin-top: 5px; margin-bottom: 10px">
										<span class="glyphicon glyphicon-tag" aria-hidden="true"></span>
										<c:forEach var="tag" items="${tags }">
											<span style="margin-right: 10px"><a
												href="${ctx }/blog/list/1?tags=${tag.name}">${tag.name }</a>
											</span>
										</c:forEach>
									</div>
								</c:if>
								<div style="margin-top: 5px; margin-bottom: 10px">
									<span title="<spring:message code="page.blog.category" />"
										class="glyphicon glyphicon-book" aria-hidden="true"></span>&nbsp;<a
										href="<u:url space="${blog.space }"/>/blog/list/1?category.id=${blog.category.id}">${blog.category.name
										}</a>
								</div>
								<div style="margin-top: 5px; margin-bottom: 10px">
									<span title="<spring:message code="page.blog.writeDate" />"
										class="glyphicon glyphicon-time" aria-hidden="true"></span>
									<fmt:formatDate value="${blog.writeDate }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</div>
								<div style="margin-top: 5px; margin-bottom: 10px">
									<span title="<spring:message code="page.blog.hitTimes" />"
										class="glyphicon glyphicon-fire" aria-hidden="true"></span>&nbsp;${blog.hits
									} &nbsp;&nbsp;<a
										href="<u:url space="${blog.space }"/>/blog/${blog.id}#comment-container">
										<span class="glyphicon glyphicon-comment"
										title="<spring:message code="page.blog.comments" />"
										aria-hidden="true"></span> </a>&nbsp;${blog.comments }
								</div>
								<div>
									<c:out value="${blog.summary }..." />
								</div>
								<div style="float: right">
									<c:choose>
										<c:when test="${user.avatar!=null}">
											<a href="<u:url space="${blog.space }"/>/index"><img src="${user.avatar.url }/64" class="img-circle"/></a>
										</c:when>
										<c:otherwise>
											<a href="<u:url space="${blog.space }"/>/index"><img src="${ctx }/static/imgs/guest_64.png" class="img-circle"/></a>
										</c:otherwise>
									</c:choose>
								</div>
								<div style="clear: both"></div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">
							<spring:message code="page.blog.none" />
						</div>
					</c:otherwise>
				</c:choose>
				<c:if test="${page.totalPage >1 }">
					<div class="alert alert-info">
						<a href="${ctx }/blog/list/2?recommend=true">查看更多</a>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 text">
				<sec:authorize ifAnyGranted="ROLE_USER,ROLE_OAUTH" var="isLogin" />
				<c:if test="${!isLogin }">
					<div style="margin-bottom: 20px">
						<fieldset>
							<legend>快速登录</legend>
							<form class="form-signin" action="${ctx }/login-check"
								method="POST">
								<div class="form-group">
									<label>姓名|邮箱</label> <input type="text" class="form-control"
										name="j_username" placeholder="">
								</div>
								<div class="form-group">
									<label>密码</label> <input type="password" class="form-control"
										name="j_password" placeholder="">
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
								<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
							</form>
						</fieldset>
					</div>
				</c:if>
				<div class="alert alert-danger">
					2016年学习android，2017年转向android开发
				</div>
				<div class="alert alert-warning">
					只在新版的chrome浏览器开发测试，别的浏览器不会做测试和支持<br /> 
					鄙视用IE浏览器的(包括EDGE)
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
</body>
</html>
