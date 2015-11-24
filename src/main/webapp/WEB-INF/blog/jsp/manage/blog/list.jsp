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
<title>博客管理</title>
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
				<c:set value="${page.datas }" var="blogs" />
				<c:choose>
					<c:when test="${not empty blogs }">
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>标题</th>
										<th>作者</th>
										<th>点击</th>
										<th>评论</th>
										<th><spring:message code="page.item.option" /></th>
										<th>推荐到首页</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="blog" items="${blogs}">
										<tr>
											<td><c:if test="${blog.from == 'COPIED' }">
										【转载】
										</c:if> <c:choose>

													<c:when test="${fn:length(blog.title) > 10}">
														<c:set var="title"
															value="${fn:substring(blog.title, 0, 10)}..." />
													</c:when>
													<c:otherwise>
														<c:set var="title" value="${blog.title }" />
													</c:otherwise>
												</c:choose> <a href="<u:url space="${space }" />/blog/${blog.id}"><c:out
														value="${title }" /></a> (<fmt:formatDate
													value="${blog.writeDate }" pattern="yyyy-MM-dd HH:mm" />)</td>
											<td class="user-info" info-format="username" info-mode="simple" space-id="${blog.space.id }"></td>
											<td>${blog.hits }</td>
											<td>${blog.comments }</td>
											<td><a href="javascript:void(0)"
												onclick="deleteBlog('${blog.id}')"
												title="<spring:message code="page.item.delete" />"
												onclick="deleteBlogLogic('${blog.id}')"><span
													class="glyphicon glyphicon-remove-sign"
													style="margin-right: 10px" aria-hidden="true"></span></a></td>
											<td>
												<a onclick="updateRecommend('${blog.id }')" href="javascript:void(0)">
													<c:choose>
														<c:when test="${blog.recommend }">
															是
														</c:when>
														<c:otherwise>
															否
														</c:otherwise>
													</c:choose>
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">
							<spring:message code="page.blog.none" />
						</div>
					</c:otherwise>
				</c:choose>
				<c:if test="${page.totalPage >1 }">
					<div>
						<ul class="pagination">
							<c:forEach begin="${page.listbegin }" end="${page.listend-1 }"
								var="i">
								<c:if test="${page.currentPage != i }">
									<li><c:choose>
											<c:when test="${pageContext.request.queryString == null}">
												<a href="${ctx }/manage/blog/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/manage/blog/list/${i }?${pageContext.request.queryString}">${i }</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 text">
				<h5>标题搜索</h5>
				<div class="input-group" style="margin-bottom: 10px">
					<input type="text" class="form-control" placeholder="标题搜索"
						value="<c:out value="${param.title}" />"> <span
						class="input-group-btn">
						<button class="btn btn-default" id="title-search-btn"
							type="button">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						</button>
					</span>
				</div>
			</div>
		</div>
		<input type="hidden" value="${param.begin }" id="beginDate" />
	</div>
	<jsp:include page="../optionalMessage.jsp" />
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#title-search-btn").click(function(){
				var title = $(this).parent().prev().val();
				if($.trim(title) != ""){
					window.location.href = "${ctx}/manage/blog/list/1?title="+title;
				}else{
					window.location.href = "${ctx}/manage/blog/list/1";
				}
			});
		})
		function deleteBlog(id){
			$("#optionalMessageModal").modal("show");
			$("#optionalMessage-confirm").unbind().bind('click',function(){
				var btn = $(this);
				var data = {};
				var form = $("#optionalMessageForm");
				data.title = form.find("input[name='title']").val();
				data.content = form.find("textarea[name='content']").val();
				btn.button("loading")
				post(contextPath + "/manage/blog/delete?id="+id, data, function callBack(data){
					if(data.success){
						$.messager.popup("操作成功");
						window.location.reload();
					}else{
						$.messager.popup(data.result);
					}
				}, function(request){
					if(request.status == "400"){
	                	var res = $.parseJSON(request.responseText);
	                	if(res.result){
	                		var html = "";
	                		for(var i=0;i<res.result.length;i++){
		                		var ef = res.result[i];
		                		html += ef.error+"<br/>";
		                	}
	                		$.messager.popup(html);
	                	};
	                };
				}, function(){
					btn.button("reset");
				})
			})
		}
		
		function updateRecommend(id){
			$("#optionalMessageModal").modal("show");
			$("#optionalMessage-confirm").unbind().bind('click',function(){
				var btn = $(this);
				var data = {};
				var form = $("#optionalMessageForm");
				data.title = form.find("input[name='title']").val();
				data.content = form.find("textarea[name='content']").val();
				btn.button("loading")
				post(contextPath + "/manage/blog/recommend?id="+id, data, function callBack(data){
					if(data.success){
						$.messager.popup("操作成功");
						window.location.reload();
					}else{
						$.messager.popup(data.result);
					}
				}, function(request){
					if(request.status == "400"){
	                	var res = $.parseJSON(request.responseText);
	                	if(res.result){
	                		var html = "";
	                		for(var i=0;i<res.result.length;i++){
		                		var ef = res.result[i];
		                		html += ef.error+"<br/>";
		                	}
	                		$.messager.popup(html);
	                	};
	                };
				}, function(){
					btn.button("reset");
				})
			})
		}
	</script>
</body>
</html>
