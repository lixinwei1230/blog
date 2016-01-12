<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<%@taglib prefix="r" uri="/resize" %>
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
<title>用户管理</title>
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8  text">
				<c:set value="${page.datas }" var="users" />
				<c:choose>
					<c:when test="${not empty users }">
						<c:forEach var="user" items="${users}">
							<div class="media well">
								<div class="media-left">
									<c:choose>
										<c:when test="${user.avatar!=null}">
											<a href="<u:url user="${user }"/>/index" target="_blank"><img src="<r:resize url="${user.avatar.url }" size="64"/>" class="img-circle"/></a>
										</c:when>
										<c:otherwise>
											<a href="<u:url user="${user }"/>/index" target="_blank"><img src="${staticSourcePrefix }/imgs/guest_64.png" class="img-circle"/></a>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="media-body">
									<h4 class="media-heading">
										<strong>用户名</strong>:${user.username }
									</h4>
									<p>
										<strong>邮箱</strong>:
										<c:if test="${fn:indexOf(user.email,'@')!=-1 }">
								${user.email }
								</c:if>
									</p>
									<p>
										<strong>昵称</strong>:${user.nickname }
									</p>
									<p>
										<strong>注册日期</strong>:
										<fmt:formatDate value="${user.registerDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
									</p>
									<p>
										<strong>激活日期</strong>:
										<fmt:formatDate value="${user.activateDate }"
											pattern="yyyy-MM-dd HH:mm:ss" />
									</p>
									<div style="text-align: right">
										<c:if
											test="${user.space != null and user.space.status != 'CLOSED'}">
											<a href="javascript:void(0)"
												onclick="toggleSpaceAbled('${user.space.id}')"
												title="禁用|解禁用户" style="margin-right: 10px"> <c:choose>
													<c:when test="${user.space.status == 'DISABLED' }">
							      			解禁空间
							      		</c:when>
													<c:otherwise>
							      			禁用空间
							      		</c:otherwise>
												</c:choose>
											</a>
										</c:if>
										<a href="javascript:void(0)"
											onclick="toggleUserAbled('${user.id}')" title="禁用|解禁用户"
											style="margin-right: 10px"> <c:choose>
												<c:when test="${!user.enabled }">
					      			解禁用户
					      		</c:when>
												<c:otherwise>
					      			禁用用户
					      		</c:otherwise>
											</c:choose>
										</a> <a href="javascript:void(0)"
											onclick="wantToSendMessageTo('${user.username}')">站内信</a>

									</div>
								</div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">当前没有任何用户</div>
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
												<a href="${ctx }/my/user/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/my/user/list/${i }?${pageContext.request.queryString}">${i }</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 text"></div>
		</div>
	</div>
	<div class="modal fade" id="newMessageModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">
						<spring:message code="page.item.createNew" />
					</h4>
				</div>
				<div class="modal-body">
					<form id="newMessageForm" class="bs-example form-horizontal"
						autocomplete="off">
						<div class="row" style="margin-bottom: 15px">
							<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
								<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
									<label><spring:message code="page.message.receiver" /></label>
								</div>
								<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
									<input type="text" name="receivers" readonly="readonly"
										id="receivers" class="form-control" />
								</div>
							</div>
						</div>
						<div class="row" style="margin-bottom: 15px">
							<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
								<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
									<label><spring:message code="page.message.title" /></label>
								</div>
								<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
									<input type="text" name="title" class="form-control" />
								</div>
							</div>
						</div>
						<div class="row" style="margin-bottom: 15px">
							<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
								<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
									<label><spring:message code="page.message.content" /></label>
								</div>
								<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
									<textarea name="content" class="form-control"
										style="height: 200px"></textarea>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="send-message"
						data-loading-text="<spring:message code="page.item.processing" />">
						<spring:message code="page.item.add" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="page.item.close" />
					</button>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../optionalMessage.jsp" />
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
	$(document).ready(function(){
		var sendMessage = $("#send-message");
		sendMessage.bind("click",function(){
			var data = {};
			var form = $("#newMessageForm");
			var detail = {};
			detail.title = form.find("input[name='title']").val();
			detail.content = form.find("textarea[name='content']").val();
			var receivers = form.find("input[name='receivers']").val();
			var receivers_array = [];
			var receivers_split = receivers.split(",");
			for(var i=0;i<receivers_split.length;i++){
				receivers_array.push(receivers_split[i]);
			}
			data.receivers=receivers_array;
			data.detail=detail;
			sendMessage.button("loading");
			post("${ctx}/manage/user/sendMessage",data,function(_result){
            	if (_result.success) {
					$.messager.popup("发送成功");
					$("#newMessageModal").modal("hide");
				} else {
					var result = _result.result;
					$.messager.popup(result);
				}
            },function (request) {  
                if(request.status == "400"){
                	var res = $.parseJSON(request.responseText);
                	if(res.result){
                		var html = "";
                		for(var i=0;i<res.result.length;i++){
	                		var ef = res.result[i];
	                		html += ef.error+"<br/>";
	                	}
                		$.messager.popup(html);
                	}
                }

             },function(){
            	 sendMessage.button("reset");
             });
		});
	});
	function wantToSendMessageTo(username){
		$("#receivers").val(username);
		$("#newMessageModal").modal("show");
	}
	function toggleUserAbled(id){
		$.messager.confirm("禁用|解禁", '确定要这么做吗？如果用户被禁用，那么下次登录的时候用户将不能登录', function() {
			$("#optionalMessageModal").modal("show");
			$("#optional-id").val(id);
			$("#optionalMessage-confirm").unbind().bind('click',function(){
				var btn = $(this);
				var data = {};
				var form = $("#optionalMessageForm");
				data.title = form.find("input[name='title']").val();
				data.content = form.find("textarea[name='content']").val();
				btn.button("loading")
				post(contextPath + "/manage/user/toggle/abled?id="+id, data, function callBack(data){
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
		});
	}
	
	function toggleSpaceAbled(id){
		$.messager.confirm("禁用|解禁", '确定要这么做吗？如果空间被禁用，那么用户的空间将不能被访问', function() {
			$("#optionalMessageModal").modal("show");
			$("#optional-id").val(id);
			$("#optionalMessage-confirm").unbind().bind('click',function(){
				var btn = $(this);
				var data = {};
				var form = $("#optionalMessageForm");
				data.title = form.find("input[name='title']").val();
				data.content = form.find("textarea[name='content']").val();
				btn.button("loading")
				post(contextPath + "/manage/user/space/toggle/abled?id="+id, data, function callBack(data){
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
		});
	}
	
	</script>
</body>
</html>
