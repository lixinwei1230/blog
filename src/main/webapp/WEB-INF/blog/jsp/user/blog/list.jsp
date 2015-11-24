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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
<title>${user.nickname }</title>
</head>
<body>
	<jsp:include page="/WEB-INF/user_navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8  col-sm-8 text">
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
									<c:if test="${blog.level > 0 }">
						【置顶】
						</c:if>
									<a href="<u:url space="${space }"/>/blog/${blog.id}"><c:out
											value="${blog.title }" /></a>
								</h4>
								<c:set var="tags" value="${blog.tags }" scope="page" />
								<c:if test="${not empty tags }">
									<div style="margin-top: 5px; margin-bottom: 10px">
										<span class="glyphicon glyphicon-tag" aria-hidden="true"></span>
										<c:forEach var="tag" items="${tags }">
											<span style="margin-right: 10px"><a
												href="<u:url space="${space }"/>/blog/list/1?tagIds=${tag.id}">${tag.name }</a></span>
										</c:forEach>
									</div>
								</c:if>
								<div style="margin-top: 5px; margin-bottom: 10px">
									<span title="<spring:message code="page.blog.category" />"
										class="glyphicon glyphicon-book" aria-hidden="true"></span>&nbsp;<a
										href="<u:url space="${space }"/>/blog/list/1?category.id=${blog.category.id}">${blog.category.name }</a>
								</div>
								<div style="margin-top: 5px; margin-bottom: 10px">
									<span title="<spring:message code="page.blog.writeDate" />"
										class="glyphicon glyphicon-time" aria-hidden="true"></span>
									<fmt:formatDate value="${blog.writeDate }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</div>
								<div style="margin-top: 5px; margin-bottom: 10px">
									<span title="<spring:message code="page.blog.hitTimes" />"
										class="glyphicon glyphicon-fire" aria-hidden="true"></span>&nbsp;${blog.hits }
									&nbsp;&nbsp;<a
										href="<u:url space="${space }"/>/blog/${blog.id}#comment-container">
										<span class="glyphicon glyphicon-comment"
										title="<spring:message code="page.blog.comments" />"
										aria-hidden="true"></span>
									</a>&nbsp;${blog.comments }
								</div>
								<div>
									<c:out value="${blog.summary }..." />
								</div>
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
												<a href="<u:url space="${space }"/>/blog/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="<u:url space="${space }"/>/blog/list/${i }?${pageContext.request.queryString}">${i }</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 text">
				<jsp:include page="/WEB-INF/user_right_column.jsp" />
				<div>
					<div class="alert alert-info">
						<a href="<u:url space="${space }"/>/blog/rss"
							target="_blank">RSS订阅</a>
					</div>
					<c:set value="${not empty param['category.id']}" var="hasCategory" />
					<form action="<u:url space="${space }"/>/blog/list/1"
						id="blog-search-form">
						<div style="margin-bottom: 15px">
							<div class="form-group">
								<label>博客分类</label> <select class="form-control"
									name="category.id">
									<option value="">全部</option>
									<c:forEach var="cate" items="${categorys }">
										<c:choose>
											<c:when
												test="${hasCategory and param['category.id'] == cate.id }">
												<option value="${cate.id }" selected="selected">${cate.name }</option>
											</c:when>
											<c:otherwise>
												<option value="${cate.id }">${cate.name }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label>博客标题</label> <input type="text" class="form-control"
									name="title" value="<c:out value="${param['title'] }"/>" />
							</div>
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" id="tag-search-input"
										placeholder="标签搜索"> <span class="input-group-btn">
										<button class="btn btn-default" id="tag-search-btn"
											onclick="writeTag()" type="button">
											<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
										</button>
									</span>
								</div>
								<div id="tagsContainer" style="margin-top: 10px"></div>
							</div>
							<input type="hidden" name="tagIds" id="search-tagIds" />
							<div style="text-align: right">
								<button class="btn btn-primary" style="" type="button"
									id="search-blog-btn">查询</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		var maxTag = 5;
		var searchTags = [];
		$(document).ready(function(){
			var tagId = getUrlParam("tagIds");
			if(tagId != null){
				var tagIds = tagId.split(",");
				for(var i=0;i<tagIds.length;i++){
					searchTags.push(tagIds[i]);
				}
			}
			writeTag();
			$("#search-blog-btn").click(function(){
				if(searchTags.length > 0){
					$("#search-tagIds").val(searchTags.toString());
				}
				$("#blog-search-form").submit();
			});
		});
		function writeTag(){
			var tagSearchInput = $("#tag-search-input");
			var name = tagSearchInput.val();
			$.get("${ctx}/tag/user/list/1",{module:'BLOG',name:name,"user.id":'${user.id}'},function callBack(data){
				var datas = data.result.datas;
				var html = '<table class="table">';
				if(datas.length > 0 ){
					html += '<tbody>';
					for(var i=0;i<datas.length;i++){
						var tag = datas[i];
						var index = inArray(tag.tag.id,searchTags);
						var addOrRemove = (index == -1) ? '<a href="javascript:void(0)" onclick="addSearchTag('+tag.tag.id+',$(this))"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></a>' : '<a href="javascript:void(0)" onclick="removeSearchTag('+tag.tag.id+',$(this))"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></a>';
						html += '<tr><td>'+tag.name+'</td><td>'+addOrRemove+'</td></div>';
					}
					html += '</tbody>';
				}
				html += '</table>'
				$("#tagsContainer").html(html);
			});
		}
		function addSearchTag(id,o){
			if(searchTags.length >= maxTag){
				return ;
			}
			var index = inArray(id,searchTags);
			if(index == -1){
				searchTags.push(id);
				o.parent().html('<a href="javascript:void(0)" onclick="removeSearchTag('+id+',$(this))"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span>')
			}
		}
		function removeSearchTag(id,o){
			var index = inArray(id,searchTags);
			if(index != -1){
				searchTags.splice(index,1);
			}
			o.parent().html('<a href="javascript:void(0)" onclick="addSearchTag('+id+',$(this))"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></a>');
		}
	</script>
</body>
</html>
