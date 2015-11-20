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
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>BUG</title>
</head>
<body>
	<jsp:include page="/WEB-INF/navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8 text">
				<div id="editor-container"></div>
				<div id="comment-container"></div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
<script type="text/javascript" src="${ctx }/static/common/comment.js"></script>
<script type="text/javascript"
	src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script>

<script>
var user = {"id":1,"space":{"id":"mhlx"}};
var space = {"id":"mhlx"};
var hit = true;
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
});
$(document).ready(function(){
	$("#comment-container").comment({
		scopeId:function(){
			return 'http://'+getUrl()+'/bug';
		},
		scope:"PAGE",
		comment_container : "comment-container",
		editor_container : 'editor-container'
	});
});
</script>

</html>