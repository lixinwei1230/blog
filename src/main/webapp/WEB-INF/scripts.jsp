<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<input type="hidden" id="domainAndPort"
	value="${urlHelper.domainAndPort }" />
<input type="hidden" id="domain" value="${urlHelper.domain }" />
<input type="hidden" id="contextPath" value="${urlHelper.contextPath }" />
<input type="hidden" id="protocol" value="${urlHelper.protocol }" />
<input type="hidden" id="enableSpaceDomain"
	value="${urlHelper.enableSpaceDomain }" />
<sec:authorize ifAnyGranted="ROLE_USER,ROLE_OAUTH" var="isLogin" />
<input type="hidden" id="isLogin" value="${isLogin }" />
<input type="hidden" id="staticSourcePrefix" value="${staticSourcePrefix }" />
<script type="text/javascript"
	src="${staticSourcePrefix }/plugins/jquery/1.11/jquery.min.js"></script>
<script type="text/javascript"
	src="${staticSourcePrefix }/plugins/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${staticSourcePrefix }/plugins/jquery.bootstrap/jquery.bootstrap.js"></script>
<script type="text/javascript" src="${staticSourcePrefix }/common/urls.js"></script>
<script type="text/javascript" src="${staticSourcePrefix }/common/common.js"></script>
