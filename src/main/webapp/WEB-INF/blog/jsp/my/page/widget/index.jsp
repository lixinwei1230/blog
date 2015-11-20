<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<title><sec:authentication property='principal.nickname' /></title>
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8">
				<c:choose>
					<c:when test="${not empty widgets }">
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>挂件名</th>
										<th>创建日期</th>
										<th></th>
									</tr>
									<c:forEach items="${widgets }" var="widget">
										<tr>
											<td><c:out value="${widget.name }" /></td>
											<td><fmt:formatDate value="${widget.createDate }"
													pattern="yyyy-MM-dd HH:mm" /></td>
											<td><a href="javascript:void(0)"
												onclick="previewWidget('${widget.id}','USER')"
												style="margin-right: 10px"><span
													class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
												<a href="javascript:void(0)"
												onclick="showUpdateWidgetModal('${widget.id}')"
												style="margin-right: 10px"><span
													class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
												<a href="javascript:void(0)"
												onclick="deleteWidget('${widget.id}')"><span
													class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
											</td>
										</tr>
									</c:forEach>
								</thead>
							</table>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">当前没有任何挂件</div>
					</c:otherwise>
				</c:choose>
				<div align="right">
					<button type="button" class="btn btn-primary"
						onclick="showAddWidgetModal()" data-loading-text="Loading...">
						<spring:message code="page.item.createNew" />
					</button>
					<button type="button" class="btn btn-primary"
						onclick="window.location.reload()">
						<spring:message code="page.item.refresh" />
					</button>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
</body>
<div class="modal fade" role="dialog" id="previewModal"
	data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="gridSystemModalLabel">挂件预览</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row  text" id="preview-container"></div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>

<div class="modal fade" role="dialog" id="newWidgetModal"
	data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="gridSystemModalLabel">挂件</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<form id="addWidgetForm" class="bs-example form-horizontal"
							autocomplete="off">
							<div class="row" style="margin-bottom: 15px">
								<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
									<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
										<label>挂件名</label>
									</div>
									<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
										<input type="text" name="name" class="form-control" />
									</div>
								</div>
							</div>
							<div class="row" style="margin-bottom: 15px">
								<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
									<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
										<label>挂件内容</label>
									</div>
									<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
										<textarea name="html" rows="8" class="form-control"
											id="editor"></textarea>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" type="button" id="btn-remove-editor"
					style="margin-right: 10px; display: none">移除编辑器</button>
				<button class="btn btn-primary" type="button"
					style="margin-right: 10px" id="btn-load-editor">加载编辑器</button>
				<button class="btn btn-primary" id="add-widget" type="button"
					style="margin-right: 10px"
					data-loading-text="<spring:message code="page.item.processing" />">提交</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
<script type="text/javascript"
	src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
var action = "insert";
var currentId = "";
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
});
function previewWidget(id,type){
	var url = '${ctx}/my/page/widget/preview/'+id;
	$.get(url,{"type":type},function callBack(data){
		if(data.success){
			$("#widgetsModal").modal("hide");
			$("#preview-container").html(data.result.html);
			$("#previewModal").modal("show");
		}else{
			$.messager.alert(data.result);
		}
	});
}
function showAddWidgetModal(){
	action = "insert";
	currentId = "";
	$("#newWidgetModal").modal("show");
}
function showUpdateWidgetModal(id){
	$.get("${ctx}/my/widget/get/"+id,{},function callBack(data){
		if(data.success){
			var result = data.result;
			var form = $("#addWidgetForm");
			form.find("input[name='name']").val(result.name);
			setContent(form, result.html);
			action = "update";
			currentId = id;
			$("#newWidgetModal").modal("show");
		}else{
			$.messager.popup(data.result);
		}
	});
}
function deleteWidget(id){
	$.messager.confirm("删除", "确定要删除这个挂件吗?放置该挂件的页面也会删除该挂件", function() { 
		var url = "${ctx}/my/widget/delete?id="+id;
		$.post(url,{},function callBack(data){
			if(data.success){
				$.messager.popup("删除成功");
				window.location.reload();
			}else{
				$.messager.popup(data.result);
			}
		})
	});
}
$(document).ready(function(){
	$("#btn-load-editor").click(function(){
		CKEDITOR.replace("editor");
		CKEDITOR.config.enterMode=CKEDITOR.ENTER_BR;
		CKEDITOR.config.height=200;
		CKEDITOR.config.allowedContent=true;
		CKEDITOR.config.extraPlugins = 'codemirror';
		CKEDITOR.config.shiftEnterMode=CKEDITOR.ENTER_P;
		CKEDITOR.config.toolbar = [
		    [ 'Source'],
		];
		$(this).hide();
		$("#btn-remove-editor").show();
	});
	$("#btn-remove-editor").click(function(){
		var editor = CKEDITOR.instances["editor"];
		if(editor){
			editor.destroy();
		}
		$(this).hide();
		$("#btn-load-editor").show();
	});
	$("#add-widget").click(function(){
		var form = $("#addWidgetForm");
		var name = form.find("input[name='name']").val();
		var content = getContent(form);
		var btn = $(this);
		btn.button("loading");
		if(action == "insert"){
			post("${ctx}/my/widget/add",{"name":name,"html":content},function callBack(result){
				if(result.success){
					window.location.reload();
				}else{
					$.messager.popup(result.result);
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
	            	};
	            }
	            if(request.status == '403'){
	            	$.messager.popup("权限验证失败");
	            };
	         },function(){
				btn.button("reset");
			});
		}else{
			post("${ctx}/my/widget/update",{"name":name,"html":content,"id":currentId},function callBack(result){
				if(result.success){
					window.location.reload();
				}else{
					$.messager.popup(result.result);
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
	            	};
	            }
	            if(request.status == '403'){
	            	$.messager.popup("权限验证失败");
	            };
	         },function(){
				btn.button("reset");
			});
		}
	});
})
function getContent(form){
	var editor = CKEDITOR.instances["editor"];
	if(editor){
		content = editor.getData();
	}else{
		content = form.find("textarea[name='html']").val();
	}
	return content;
}
function setContent(form,content){
	var editor = CKEDITOR.instances["editor"];
	if(editor){
		editor.setData(content);
	}else{
		form.find("textarea[name='html']").val(content);
	}
}
</script>
</html>