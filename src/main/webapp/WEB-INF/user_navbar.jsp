<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<div class="container">
	<nav class="navbar navbar-default " style="margin-top: 1em">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="http://<u:url user="${user }"/>/index">${user.nickname }</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<c:if test="${user.hasRole('ROLE_SPACE') }">
					<ul class="nav navbar-nav">
						<li><a href="http://<u:url space="${space }"/>/blog/list/1">博客</a></li>
					</ul>
				</c:if>
				<sec:authorize ifAnyGranted="ROLE_USER,ROLE_OAUTH" var="login" />
				<c:choose>
					<c:when test="${login }">
						<sec:authentication property='principal' var="user" />
						<ul class="nav navbar-nav navbar-right">
							<li><a
								href="http://<u:url user="${user }" myMenu="true"/>/index">我的主页</a></li>
						</ul>
						<ul class="nav navbar-nav navbar-right">
							<li><a
								href="http://<u:url user="${user }" myMenu="true"/>/message/receive/list/1?isRead=false"><spring:message
										code="page.menu.message" /><span class="badge"
									id="toReadMessageBadge">0</span></a></li>
						</ul>
					</c:when>
					<c:otherwise>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="http://<u:url/>">主页</a></li>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>
			<!--/.nav-collapse -->
		</div>
		<!--/.container-fluid -->
	</nav>
</div>