
<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<div class="container">
	<nav class="navbar navbar-default" style="margin-top: 1em">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<u:url/>">梦海澜心</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<sec:authorize ifNotGranted="ROLE_USER,ROLE_OAUTH">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-expanded="false"><spring:message
									code="page.menu.user" /><span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li><a href="<u:url/>/login"><spring:message
											code="page.menu.user.login" /></a></li>
								<li><a href="<u:url/>/register"><spring:message
											code="page.menu.user.register" /></a></li>
								<li><a href="<u:url/>/reactivate">激活</a></li>
								<li><a href="<u:url/>/password/forget"><spring:message
											code="page.menu.user.password.forget" /></a></li>
							</ul></li>
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_USER,ROLE_OAUTH">
						<sec:authentication property='principal' var="user" />
						<li><a
							href="<u:url user="${user }" myMenu="true"/>/message/receive/list/1?isRead=false"><spring:message
									code="page.menu.message" /><span class="badge"
								id="toReadMessageBadge">0</span></a></li>
						<li><a
							href="<u:url user="${user }" myMenu="true"/>/index">我的主页</a></li>
					</sec:authorize>
				</ul>
				<sec:authorize ifAnyGranted="ROLE_USER,ROLE_OAUTH">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="javascript:void(0)" onclick="document.getElementById('logoutForm').submit()">退出</a><form style="display: none" action="<u:url/>/logout" method="post" id="logoutForm"><input type="hidden" name="${_csrf.parameterName}"value="${_csrf.token}" /></form></li>
					</ul>
				</sec:authorize>
			</div>
			<!--/.nav-collapse -->
		</div>
		<!--/.container-fluid -->
	</nav>
</div>