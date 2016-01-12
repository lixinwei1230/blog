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
<title><sec:authentication property='principal.nickname' /></title>

<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
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
										<th>置顶</th>
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
												</c:choose> <a href="<u:url space="${space }"/>/blog/${blog.id}"><c:out
														value="${title }" /></a> (<fmt:formatDate
													value="${blog.writeDate }" pattern="yyyy-MM-dd HH:mm" />)</td>
											<td><c:choose>
													<c:when test="${blog.level > 0 }">
												${blog.level }
											</c:when>
													<c:otherwise>
												否
											</c:otherwise>
												</c:choose></td>
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
												onclick="deleteBlogLogic('${blog.id}')"
												title="<spring:message code="page.item.delete" />"
												onclick="deleteBlogLogic('${blog.id}')"><span
													class="glyphicon glyphicon-remove-sign"
													style="margin-right: 10px" aria-hidden="true"></span></a> <a
												href="${ctx }/my/blog/update/${blog.id}"
												title="<spring:message code="page.item.update" />"><span
													class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
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
												<a href="${ctx }/my/blog/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/my/blog/list/${i }?${pageContext.request.queryString}">${i }</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 text">
				<c:choose>
					<c:when test="${not empty categorys }">
						<h5>我的分类</h5>
						<c:set value="${param['category.id']}" var="categoryId" />
						<select class="form-control" id="categorys"
							style="margin-bottom: 20px">
							<option value="">请选择</option>
							<c:forEach items="${categorys }" var="category">
								<c:choose>
									<c:when
										test="${categoryId != null and category.id == categoryId }">
										<option value="${category.id }" selected="selected">${category.name }</option>
									</c:when>
									<c:otherwise>
										<option value="${category.id }">${category.name }</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">当前没有任何分类</div>
					</c:otherwise>
				</c:choose>
				<h5>博客范围</h5>
				<c:set value="${param['scopes']}" var="currentScope" />
				<select class="form-control" id="scopes" style="margin-bottom: 20px">
					<option value="">请选择</option>
					<c:choose>
						<c:when
							test="${currentScope != null and currentScope == 'PRIVATE' }">
							<option value="PUBLIC"><spring:message
									code="page.scope.public" /></option>
							<option value="PRIVATE" selected="selected"><spring:message
									code="page.scope.private" /></option>
						</c:when>
						<c:when
							test="${currentScope != null and currentScope == 'PUBLIC' }">
							<option value="PUBLIC" selected="selected"><spring:message
									code="page.scope.public" /></option>
							<option value="PRIVATE"><spring:message
									code="page.scope.private" /></option>
						</c:when>
						<c:otherwise>
							<option value="PUBLIC"><spring:message
									code="page.scope.public" /></option>
							<option value="PRIVATE"><spring:message
									code="page.scope.private" /></option>
						</c:otherwise>
					</c:choose>
				</select>
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
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
		$(document).ready(function(){
			$("#categorys").change(function(){
				var category = $(this).val();
				if(category != ""){
					window.location.href = "${ctx}/my/blog/list/1?category.id="+category;
				}else{
					window.location.href = "${ctx}/my/blog/list/1";
				}
			});
			$("#scopes").change(function(){
				var scope = $(this).val();
				if(scope != ""){
					window.location.href = "${ctx}/my/blog/list/1?scopes.scopes="+scope;
				}else{
					window.location.href = "${ctx}/my/blog/list/1";
				}
			});
			$("#title-search-btn").click(function(){
				var title = $(this).parent().prev().val();
				if($.trim(title) != ""){
					window.location.href = "${ctx}/my/blog/list/1?title="+title;
				}else{
					window.location.href = "${ctx}/my/blog/list/1";
				}
			});
		})
		function deleteBlogLogic(id){
			$.messager.confirm('<spring:message code="page.item.delete" />', '<spring:message code="page.blog.deleteLogic.tip" />', function() { 
				var url = '${ctx}/my/blog/logicDelete';
				$.post(url,{id:id},function callBack(data){
					if(data.success){
						$.messager.popup('<spring:message code="page.option.success" />');
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
