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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
<title>${user.nickname }</title>
</head>
<body>
	<jsp:include page="/WEB-INF/user_navbar.jsp" />
	<div class="container text">
		<c:forEach items="${_page.rows }" var="rc">
			<div class="row text">
				<c:forEach items="${rc.columns }" var="cc">
					<div class="col-lg-${cc.width } col-md-${cc.width}">
						<div class="row">
							<c:forEach items="${cc.widgets }" var="lw">
								<c:if test="${!lw.config.hidden }">
									<div
										class="col-lg-<fmt:parseNumber integerOnly="true" value="${12*lw.width/cc.widthP }" /> col-md-<fmt:parseNumber integerOnly="true" value="${12*lw.width/cc.widthP }" />">
										${lw.widget.html }</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
</body>
<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
</html>