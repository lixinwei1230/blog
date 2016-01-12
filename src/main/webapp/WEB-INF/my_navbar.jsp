<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<sec:authentication property='principal' var="user" />
<sec:authorize ifAnyGranted="ROLE_USER" var="isUser" />
<sec:authorize ifAnyGranted="ROLE_OAUTH" var="isOauth" />
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
				<a class="navbar-brand"
					href="<u:url user="${user }" myMenu="true"/>/index">${user.nickname }</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<c:if test="${isUser or isOauth }">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-expanded="false">信息管理<span
								class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li><a
									href="<u:url user="${user }" myMenu="true"/>/avatar/index">头像修改</a></li>
								<li><a
									href="<u:url user="${user }" myMenu="true"/>/nickname/index">昵称修改</a></li>
								<c:if test="${isUser }">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/password/change">密码修改</a></li>
								</c:if>
								<c:if test="${isUser }">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/oauth/list">社交账号管理</a></li>
								</c:if>
								<c:if test="${isOauth }">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/oauth/completeEmail">完善帐号信息</a></li>
								</c:if>
							</ul></li>
						<sec:authorize ifAnyGranted="ROLE_SPACE">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">博客管理<span
									class="caret"></span></a>
								<ul class="dropdown-menu" role="menu">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/blog/list/1">我的博客</a></li>
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/blog/write">写博客</a></li>
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/blog/recycler/list/1">回收站</a></li>
								</ul></li>
								<li><a
										href="<u:url user="${user }" myMenu="true"/>/file/list/1">文件管理</a></li>
						</sec:authorize>
						<c:if test="${isUser or isOauth }">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">页面管理<span
									class="caret"></span></a>
								<ul class="dropdown-menu" role="menu">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/page/index">自定义页面管理</a></li>
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/widget/index">挂件管理</a></li>
								</ul></li>
						</c:if>
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-expanded="false"><spring:message
									code="page.menu.message" /><span class="badge"
								id="toReadMessageBadge">0</span><span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<c:if test="${isUser }">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/message/send/list/1"><spring:message
												code="page.menu.message.send" /></a></li>
								</c:if>
								<c:if test="${isUser or isOauth }">
									<li><a
										href="<u:url user="${user }" myMenu="true"/>/message/receive/list/1?isRead=false"><spring:message
												code="page.menu.message.unread" /></a></li>
								</c:if>
							</ul></li>
						<c:if test="${isUser or isOauth }">
							<li><a href="javascript:void(0)" onclick="document.getElementById('logoutForm').submit()">退出</a><form style="display: none" action="<u:url/>/logout" method="post" id="logoutForm"><input type="hidden" name="${_csrf.parameterName}"value="${_csrf.token}" /></form></li>
						</c:if>
					</c:if>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="<u:url/>">主页</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
		<!--/.container-fluid -->
	</nav>
</div>