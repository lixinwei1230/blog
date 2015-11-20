<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
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
						<table class="table">
							<thead>
								<tr>
									<th><spring:message code="page.message.title" /></th>
									<th><spring:message code="page.message.sendDate" /></th>
									<th><spring:message code="page.item.option" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="message" items="${messages }">
									<tr>
										<td><c:out value="${message.detail.title }" /></td>
										<td><fmt:formatDate value="${message.sendDate }"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td><a href="${ctx}/my/message/send/${message.id}"
											style="margin-right: 10px" data-toggle="modal"
											data-target="#myModal"><span
												class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
											<a href="javascript:void(0)"
											onclick="deleteMessage('${message.id}')"><span
												class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
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
												<a href="${ctx }/my/message/send/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/my/message/send/list/${i }?${pageContext.request.queryString}">${i }</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<div align="right">
					<button type="button" class="btn btn-primary"
						onclick="showNewMessageModal()">
						<spring:message code="page.item.createNew" />
					</button>
					<button type="button" class="btn btn-primary"
						style="margin-left: 10px" onclick="window.location.reload()">
						<spring:message code="page.item.refresh" />
					</button>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
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
									<input type="text" name="receivers" class="form-control" />
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
						<div class="row" style="margin-bottom: 15px">
							<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
								<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
									<label><spring:message code="page.item.validateCode" /></label>
								</div>
								<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
									<img src="${ctx }/captcha/" class="img-responsive"
										onclick="this.src='${ctx}/captcha/'+new Date().getTime()" /><br />
									<input type="text" class="form-control" name="validateCode"
										placeholder="<spring:message code="global.pleaseInput"/>" />
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

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content"></div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
	
	$("#myModal").on("hidden.bs.modal", function(e) {
	   $(this).removeData();
	});
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	function showNewMessageModal(){
		$("#newMessageModal").modal("show");
	}
	
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
			var validateCode = form.find("input[name='validateCode']").val();
			sendMessage.button("loading");
			post("${ctx}/my/message/send/send?validateCode="+validateCode,data,function(_result){
            	if (_result.success) {
					window.location.reload();
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
	
	function deleteMessage(id){
		$.messager.confirm("删除", "确定要删除该信息？", function() { 
			var url = "${ctx}/my/message/send/delete";
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
	</script>

</body>
</html>