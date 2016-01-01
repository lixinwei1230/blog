<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${ctx}/static/plugins/prettify/prettify.min.css"
	rel="stylesheet">
<title><c:out value="${blog.title }" /></title>
</head>
<body>
	<jsp:include page="/WEB-INF/user_navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8 text">
				<h3>
					<c:out value="${referer }"/>
					<c:out value="${blog.title }" />
					<sec:authorize ifAnyGranted="ROLE_SPACE">
						<sec:authentication property='principal.space.id' var="spaceId" />
						<c:if test="${blog.space.id == spaceId}">
							<a href="${ctx}/my/blog/update/${blog.id}"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
						</c:if>
					</sec:authorize>
				</h3>
				<c:set var="tags" value="${blog.tags }" scope="page" />
				<c:if test="${not empty tags }">
					<div style="margin-top: 5px; margin-bottom: 10px">
						<span class="glyphicon glyphicon-tag" aria-hidden="true"></span>
						<c:forEach var="tag" items="${tags }">
							<span style="margin-right: 10px"><a
								href="<u:url space="${space }"/>/blog/list/1?tags=${tag.name}">${tag.name }</a></span>
						</c:forEach>
					</div>
				</c:if>
				<div id="blog-content" style="margin-bottom: 20px">
					${blog.content }</div>
				<div class="clearfix">&nbsp;</div>
				<div id="around"></div>
				<div id="attContainer"></div>
				<div id="editor-container"></div>
				<div id="comment-container"></div>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 text">
				<jsp:include page="/WEB-INF/user_right_column.jsp" />
			</div>
		</div>
	</div>
</body>
<u:url space="${space }" var="spaceLinkPrefix" />
<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
<script type="text/javascript"
	src="${ctx }/static/plugins/prettify/prettify.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx }/static/common/comment.js"></script>
<script>
var hit = true;
$(document).ready(function(){
	var ids = [];
	$("pre").addClass("prettyprint");
 	prettyPrint();
	$("#comment-container").comment({
		scopeId:function(){
			return '${blog.id}';
		},
		scope:"Blog",
		comment_container : "comment-container",
		editor_container : 'editor-container'
	});
	var blogId = '${blog.id}';
	$.get('${ctx}/blog/'+blogId+'/around',{"space.id":'${space.id}'},function callBack(data){
		var blogs = data.result;
		if(blogs.length > 0){
			var html = '<div class="alert alert-info">';
			for(var i=0;i<blogs.length;i++){
				var blog = blogs[i];
				if(blog.id > blogId){
					html += '<p>下一篇：<a href="${spaceLinkPrefix}/blog/'+blog.id+'">'+blog.title+"</a></p>";
				}else{
					html += '<p>上一篇：<a href="${spaceLinkPrefix}/blog/'+blog.id+'">'+blog.title+"</a></p>";
				}
			}
			html += '</div>';
			$("#around").html(html);
		}
	});
});
$(window).load(function(){
	$("#blog-content").show();
})
</script>
<sec:authorize ifAnyGranted="ROLE_SPACE">
	<sec:authentication property='principal.space.id' var="spaceId" />
	<script type="text/javascript">
var spaceId = "${spaceId}";
if(spaceId == '${blog.space.id}'){
	hit = false;
}
</script>
</sec:authorize>
<script type="text/javascript">
var blogId = "blog_${blog.id}";
if(hit ){
	var item = sessionStorage.getItem(blogId);
	if(!item || item == null){
		$.post("${ctx}/blog/hit/${blog.id}",{},function callBack(data){
			sessionStorage.setItem(blogId, 'true');
		});
	}
}
</script>
</html>