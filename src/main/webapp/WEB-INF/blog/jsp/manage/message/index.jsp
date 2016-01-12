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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<sec:authentication property='principal.space' var="space" />
<title>message manage</title>
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8  text">
				<button class="btn btn-primary" id="new-message">GlobalMessage</button>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>

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
									<label><spring:message code="page.message.title" />
									</label>
								</div>
								<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
									<input type="text" name="title" class="form-control" />
								</div>
							</div>
						</div>
						<div class="row" style="margin-bottom: 15px">
							<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
								<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
									<label><spring:message code="page.message.content" />
									</label>
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
</body>
<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		$("#new-message").click(function(){
			$("#newMessageModal").modal("show");
		});
		var sendMessage = $("#send-message");
		sendMessage.click(function(){
			var url = contextPath + "/manage/message/send/global";
			var data = {};
			var form = $("#newMessageForm");
			var detail = {};
			detail.title = form.find("input[name='title']").val();
			detail.content = form.find("textarea[name='content']").val();
			var data = {};
			data.detail = detail;
			sendMessage.button("loading");
			post(url,data,function(_result){
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
		})
	})
</script>
</html>
