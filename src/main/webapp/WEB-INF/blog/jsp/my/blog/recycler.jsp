<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<sec:authentication property='principal.space' var="space" />
<title><sec:authentication property='principal.nickname' /></title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
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
			<div class="col-lg-8 col-md-8  col-sm-8 text">
				<c:set value="${page.datas }" var="blogs" />
				<c:choose>
					<c:when test="${not empty blogs }">
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>标题</th>
										<th>范围</th>
										<th>评论范围</th>
										<th>点击</th>
										<th>评论</th>
										<th><spring:message code="page.item.option" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="blog" items="${blogs}">
										<tr>
											<td><c:choose>
													<c:when test="${fn:length(blog.title) > 10}">
														<c:set var="title"
															value="${fn:substring(blog.title, 0, 10)}..." />
													</c:when>
													<c:otherwise>
														<c:set var="title" value="${blog.title }" />
													</c:otherwise>
												</c:choose> <c:out value="${title }" /> (<fmt:formatDate
													value="${blog.writeDate }" pattern="yyyy-MM-dd HH:mm" />)</td>
											<td><c:choose>
													<c:when test="${blog.scope == 'PUBLIC' }">
														<span title="公开" class="glyphicon glyphicon-eye-open"
															aria-hidden="true"></span>
													</c:when>
													<c:when test="${blog.scope == 'PRIVATE' }">
														<span title="私人" class="glyphicon glyphicon-eye-close"
															aria-hidden="true"></span>
													</c:when>
												</c:choose></td>
											<td><c:choose>
													<c:when test="${blog.commentScope == 'PUBLIC' }">
														<span title="公开" class="glyphicon glyphicon-eye-open"
															aria-hidden="true"></span>
													</c:when>
													<c:when test="${blog.commentScope == 'PRIVATE' }">
														<span title="私人" class="glyphicon glyphicon-eye-close"
															aria-hidden="true"></span>
													</c:when>
												</c:choose></td>
											<td>${blog.hits }</td>
											<td>${blog.comments }</td>
											<td><a href="javascript:void(0)"
												onclick="deleteBlog('${blog.id}')"><span
													class="glyphicon glyphicon-remove-sign"
													style="margin-right: 10px" aria-hidden="true"></span></a> <a
												href="javascript:void(0)" onclick="recover('${blog.id}')"><span
													class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
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
									<li><a href="${ctx }/my/blog/recycler/list/${i}">${i }</a>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 text"></div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
		$(document).ready(function(){
			$("#title-search-btn").click(function(){
				var title = $(this).parent().prev().val();
				if($.trim(title) != ""){
					window.location.href = "${ctx}/my/blog/recycler/list/1?title="+title;
				}else{
					window.location.href = "${ctx}/my/blog/recycler/list/1";
				}
			});
		})
		
		function recover(id){
			$.messager.confirm('<spring:message code="page.item.recover" />', '<spring:message code="page.blog.recover.tip" />', function() { 
				var url = '${ctx}/my/blog/recover';
				$.post(url,{id:id},function callBack(data){
					if(data.success){
						window.location.reload();
					}else{
						$.messager.popup(data.result);
					}
				});
		    });
		}
		function deleteBlog(id){
			$.messager.confirm('<spring:message code="page.item.delete" />', '<spring:message code="page.blog.delete.tip" />', function() { 
				var url = '${ctx}/my/blog/delete';
				$.post(url,{id:id},function callBack(data){
					if(data.success){
						window.location.reload();
					}else{
						$.messager.popup(data.result);
					}
				});
		    });
		}
	</script>
</body>
</html>
