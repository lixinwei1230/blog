<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<title><sec:authentication property='principal.nickname' /></title>
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
		<c:set var="messages" value="${page.datas }" />
		<div class="row">
			<div class="col-lg-8 col-sm-8 col-md-8">
				<c:choose>
					<c:when test="${empty messages }">
						<div class="alert alert-info">
							<spring:message code="page.message.none" />
						</div>
					</c:when>
					<c:otherwise>
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th><spring:message code="page.message.title" /></th>
										<th><spring:message code="page.message.sender" /></th>
										<th><spring:message code="page.message.sendDate" /></th>
										<th><spring:message code="page.message.status" /></th>
										<th><spring:message code="page.item.option" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="message" items="${messages }">
										<tr>
											<td><c:choose>
													<c:when test="${message.status == 'COMMON' }">
														<span class="glyphicon glyphicon-star-empty"
															aria-hidden="true"></span>
													</c:when>
													<c:when test="${message.status == 'FAVORITES' }">
														<span class="glyphicon glyphicon-star" aria-hidden="true"></span>
													</c:when>
												</c:choose> <c:out value="${message.message.detail.title }" /></td>
											<td><a href="<u:url user="${message.message.sender}"/>/index" target="_blank">${message.message.sender.nickname }</a> </td>
											<td><fmt:formatDate value="${message.message.sendDate }"
													pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td><c:choose>
													<c:when test="${message.isRead }">
														<spring:message code="page.message.read" />
													</c:when>
													<c:otherwise>
														<spring:message code="page.message.unread" />
													</c:otherwise>
												</c:choose></td>
											<td><a href="${ctx}/my/message/receive/${message.id}"
												style="margin-right: 10px" data-toggle="modal"
												data-target="#myModal"><span
													class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
												<c:choose>
													<c:when test="${message.status == 'COMMON' }">
														<a href="javascript:void(0)" style="margin-right: 10px"
															onclick="updateStatus('${message.id}','FAVORITES')"><span
															class="glyphicon glyphicon-star" aria-hidden="true"></span></a>
													</c:when>
													<c:when test="${message.status == 'FAVORITES' }">
														<a href="javascript:void(0)" style="margin-right: 10px"
															onclick="updateStatus('${message.id}','COMMON')"><span
															class="glyphicon glyphicon-star-empty" aria-hidden="true"></span></a>
													</c:when>
												</c:choose> <a href="javascript:void(0)"
												onclick="deleteMessage('${message.id}')"><span
													class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
									<tr>
										<td colspan="5" align="right">
											<button type="button" class="btn btn-primary"
												style="margin-left: 10px" onclick="window.location.reload()">
												<spring:message code="page.item.refresh" />
											</button>
										</td>
									</tr>
								<tfoot>
							</table>
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
												<a href="${ctx }/my/message/receive/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/my/message/receive/list/${i }?${pageContext.request.queryString}">${i }</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-sm-4 col-md-4">
				<div class="alert alert-info">普通信息会保存30天</div>
				<form action="${ctx}/my/message/receive/list/1" method="GET" id="">
					<div class="form-group">
						<label>是否已读</label>
						<c:set value="${param['isRead']}" var="isRead" />
						<select class="form-control" id="isRead" name="isRead">
							<c:choose>
								<c:when test="${isRead != null and isRead == 'true' }">
									<option value="">全部</option>
									<option value="false">未读</option>
									<option value="true" selected="selected">已读</option>
								</c:when>
								<c:when test="${isRead != null and isRead == 'false' }">
									<option value="">全部</option>
									<option value="false" selected="selected">未读</option>
									<option value="true">已读</option>
								</c:when>
								<c:otherwise>
									<option value="" selected="selected">全部</option>
									<option value="false">未读</option>
									<option value="true">已读</option>
								</c:otherwise>
							</c:choose>
						</select>
					</div>
					<div class="form-group">
						<label>状态</label>
						<c:set value="${param['status']}" var="status" />
						<select class="form-control" id="status" name="status">
							<c:choose>
								<c:when test="${status != null and status == 'COMMON' }">
									<option value="">全部</option>
									<option value="COMMON" selected="selected">普通</option>
									<option value="FAVORITES">收藏</option>
								</c:when>
								<c:when test="${status != null and status == 'FAVORITES' }">
									<option value="">全部</option>
									<option value="COMMON">普通</option>
									<option value="FAVORITES" selected="selected">收藏</option>
								</c:when>
								<c:otherwise>
									<option value="">全部</option>
									<option value="COMMON">普通</option>
									<option value="FAVORITES">收藏</option>
								</c:otherwise>
							</c:choose>
						</select>
					</div>
					<div style="text-align: right">
						<button type="submit" class="btn btn-primary">查询</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content"></div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
	
	$(document).ready(function(){
		$("#myModal").on("shown.bs.modal", function(e) {
			var link = $(e.relatedTarget);
			var href = link.attr("href");
			var splits = href.split("/");
			$.post("${ctx}/my/message/receive/read",{"ids":splits[splits.length-1]});
		});
		
		$("#myModal").on("hidden.bs.modal", function(e) {
			   $(this).removeData();
			   window.location.reload();
			});
	})
	
	function updateStatus(id,status){
		var url = '${ctx}/my/message/receive/status/change';
		$.post(url,{ids:id,status:status},function callBack(data){
			if(data.success){
				$.messager.popup("收藏成功");
				window.location.reload();
			}else{
				$.messager.popup(data.result);
			}
		});
	}
	
	function deleteMessage(id){
		$.messager.confirm("删除", "确定要删除该信息？", function() { 
			var url = "${ctx}/my/message/receive/delete";
			$.post(url,{ids:id},function callBack(data){
				if(data.success){
					$.messager.popup("删除成功");
					window.location.reload();
				}else{
					$.messager.popup(data.result);
				}
			});
		});
	}
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	function showNewMessageModal(){
		$("#newMessageModal").modal("show");
	}
	</script>

</body>
</html>