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
									}
									&nbsp;&nbsp;<a
										href="<u:url space="${blog.space }"/>/blog/${blog.id}#comment-container">
										<span class="glyphicon glyphicon-comment"
										title="<spring:message code="page.blog.comments" />"
										aria-hidden="true"></span>
									</a>&nbsp;${blog.comments }
								</div>
								<div>
									<c:out value="${blog.summary }..." />
								</div>
								<div style="float: right" class="user-info"
									space-id="${blog.space.id }"></div>
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
					<div>
						<ul class="pagination">
							<c:forEach begin="${page.listbegin }" end="${page.listend-1 }"
								var="i">
								<c:if test="${page.currentPage != i }">
									<li><c:choose>
											<c:when test="${pageContext.request.queryString == null}">
												<a href="${ctx }/blog/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/blog/list/${i }?${pageContext.request.queryString}">${i
													}</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 text">
				<form action="${ctx }/blog/list/1"
					id="blog-search-form">
					<div style="margin-bottom: 15px">
						<div class="form-group">
							<label>博客标题</label> <input type="text" class="form-control"
								name="title" value="<c:out value="${param['title'] }"/>" />
						</div>
						
						<div class="form-group">
							<label>推荐博客</label> 
							<c:choose>
								<c:when test="${not empty param.recommend }">
									<select name="recommend" class="form-control">
										<option value="">全部</option>
										<c:choose>
											<c:when test="${param.recommend == 'true' }">
												<option value="true" selected="selected">是</option>
											</c:when>
											<c:otherwise>
												<option value="true">是</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${param.recommend == 'false' }">
												<option value="false" selected="selected">否</option>
											</c:when>
											<c:otherwise>
												<option value="false">否</option>
											</c:otherwise>
										</c:choose>
									</select>
								</c:when>
								<c:otherwise>
									<select name="recommend" class="form-control">
										<option value="">全部</option>
										<option value="true">是</option>
										<option value="false">否</option>
									</select>
								</c:otherwise>
							</c:choose>
						</div>
						
						<div class="form-group">
							<label>博客来源</label> 
							<c:choose>
								<c:when test="${not empty param.from }">
									<select name="from" class="form-control">
										<option value="">全部</option>
										<c:choose>
											<c:when test="${param.from == 'ORIGINAL' }">
												<option value="ORIGINAL" selected="selected">原创</option>
											</c:when>
											<c:otherwise>
												<option value="ORIGINAL">原创</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${param.from == 'COPIED' }">
												<option value="COPIED" selected="selected">转载</option>
											</c:when>
											<c:otherwise>
												<option value="COPIED">转载</option>
											</c:otherwise>
										</c:choose>
									</select>
								</c:when>
								<c:otherwise>
									<select name="from" class="form-control">
										<option value="">全部</option>
										<option value="ORIGINAL">原创</option>
										<option value="COPIED">转载</option>
									</select>
								</c:otherwise>
							</c:choose>
						</div>
						
						<div class="form-group">
							<label>开始日期</label> <input type="text" class="form-control"
								name="begin" value="<c:out value="${param['begin'] }"/>" />
						</div>
						
						<div class="form-group">
							<label>结束日期</label> <input type="text" class="form-control"
								name="end" value="<c:out value="${param['end'] }"/>" />
						</div>
						<div style="text-align: right">
							<button class="btn btn-primary" style="" type="button"
								id="search-blog-btn">查询</button>
						</div>
					</div>
					
					
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
</body>
</html>
