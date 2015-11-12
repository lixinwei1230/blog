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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
		<div class="row" style="margin-bottom: 20px">
			<div class="col-lg-12" style="text-align: right">
				<button class="btn btn-primary" onclick="showWidgets()">添加挂件</button>
			</div>
		</div>
		<c:forEach items="${page.rows }" var="rc">
			<div class="row text">
				<c:forEach items="${rc.columns }" var="cc">
					<div class="col-lg-${cc.width } col-md-${cc.width}">
						<div class="row">
							<c:forEach items="${cc.widgets }" var="lw">
								<div
									class="col-lg-<fmt:parseNumber integerOnly="true" value="${12*lw.width/cc.widthP }" /> col-md-<fmt:parseNumber integerOnly="true" value="${12*lw.width/cc.widthP }" />">
									<div style="text-align: right">
										<span>(${lw.r },${lw.x },${lw.y },${lw.width })</span> <a
											href="javascript:void(0)" onclick="deleteWidget('${lw.id}')">删除</a>
										<a href="javascript:void(0)" style="margin-left: 10px"
											onclick="showWidgetUpdateModal('${lw.r }','${lw.x }','${lw.y }','${lw.width }','${lw.id}','${lw.widget.id }','${lw.widget.type }')">更新</a>
										<a href="javascript:void(0)" style="margin-left: 10px"
											onclick="getWidgetConfig('${lw.id}','${lw.widget.id }','${lw.widget.type }')">配置</a>
									</div>
									${lw.widget.html }
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
</body>
<div class="modal" id="widgetsModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">挂件</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
	</div>
</div>
<div class="modal" role="dialog" id="previewModal"
	data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="gridSystemModalLabel">挂件预览</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row text" id="preview-container"></div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary preview-widgets">返回挂件列表</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<div class="modal fade" role="dialog" id="configModal"
	data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="gridSystemModalLabel">挂件配置</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<form id="configForm" class="bs-example form-horizontal"
						autocomplete="off"></form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" id="submit-config"
					data-loading-text="<spring:message code="page.item.processing" />">
					<spring:message code="page.item.submit" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<div class="modal" id="locationWidgetModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">挂件位置</div>
			<div class="modal-body">
				<form id="addLocationWidgetForm">
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>第几行？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" name="r" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>第几列？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" name="x" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>第几个？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" name="y" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>挂件宽度</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" name="width" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary preview-widgets">返回挂件列表</button>
				<button type="button" class="btn btn-primary" onclick="putWidget()">放置</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="updateLocationWidgetModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">挂件位置</div>
			<div class="modal-body">
				<form id="addLocationWidgetForm">
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>第几行？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" id="update-r" class="form-control" name="r" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>第几列？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" id="update-x" name="x" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>第几个？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" name="y" id="update-y" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>挂件宽度</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" class="form-control" name="width"
									id="update-width" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
							style="margin-bottom: 10px">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>如果目标挂件存在，是否交换位置？</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="checkbox" value="true" id="wrap"> 是
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="update-btn"
					data-loading-text="<spring:message code="page.item.processing" />">更新</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
<script type="text/javascript" src="${ctx }/static/common/widgets.js"></script>
<script type="text/javascript">
$(function() {
	return $(".modal").on("show.bs.modal", function() {
		var curModal;
	    curModal = this;
	    $(".modal").each(function() {
	      if (this !== curModal) {
	        $(this).modal("hide");
	      }
	    });
	});
});
var toPutWidth = {};
var root = '${ctx}';
$(document).ready(function(){
	$(".preview-widgets").on('click',function(){
		showWidgets();
	});
});

function deleteWidget(id){
	var url = "${ctx}/my/page/widget/delete";
	$.post(url,{"id":id},function callBack(data){
		if(data.success){
			$.messager.popup("删除成功");
			window.location.reload();
		}else{
			$.messager.popup(data.result);
		}
	});
}
function showWidgets(){
	var url = "${ctx}/my/page/widget/list";
	$.get(url,{},function callBack(data){
		if(data.success){
			$("#previewModal").modal("hide");
			$("#locationWidgetModal").modal('hide');
			var modal = $("#widgetsModal");
			var results = data.result;
			if(results.length == 0){
				modal.find(".modal-body").html('<div class="alert alert-info">当前没有任何可供放置的挂件</div>');
			}else{
				modal.find(".modal-content").find(".table-responsive").remove();
				var html = '<div class="table-responsive">';
				html += '<table class="table">';
				for(var i=0;i<results.length;i++){
					var result = results[i];
					var type = result.type == "SYSTEM" ? "系统":"个人";
					html += '<tr><td>'+result.name+'('+type+')</td><td><a href="javascript:void(0)" onclick="previewWidget('+result.id+',\''+result.type+'\')">预览</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="wantToPutWidget('+result.id+',\''+result.type+'\')">放置</a></td></tr>';
				}
				html += '</table>';
				html += '</div>';
				modal.find(".modal-body").after(html);
			}
			modal.modal("show");
		}else{
			$.messager.popup(data.result);
		}
	});
}

function wantToPutWidget(id,type){
	var _widget = {};
	_widget.id = id;
	_widget.type = type;
	toPutWidth = _widget;
	$("#locationWidgetModal").modal('show');
	$("#widgetsModal").modal("hide");
}

function putWidget(){
	var widget = {};
	widget.r = $("#addLocationWidgetForm").find("input[name='r']").val();
	widget.x = $("#addLocationWidgetForm").find("input[name='x']").val();
	widget.y = $("#addLocationWidgetForm").find("input[name='y']").val();
	widget.width = $("#addLocationWidgetForm").find("input[name='width']").val();
	widget.widget = toPutWidth;
	var page = {"id":'${page.id}'};
	widget.page = page;
	post("${ctx}/my/page/widget/put",widget,function callBack(data){
		if(data.success){
			$.messager.popup("放置成功");
			window.location.reload();
		}else{
			$.messager.popup(data.result);
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
        };
     },function(){
		
	});
}
function previewWidget(id,type){
	var url = '${ctx}/my/page/widget/preview/'+id;
	$.get(url,{"type":type},function callBack(data){
		if(data.success){
			$("#preview-container").html(data.result.html);
			$("#previewModal").modal("show");
		}else{
			$.messager.alert(data.result);
		}
	});
}
function getWidgetConfig(id,widgetId,type){
	$.get('${ctx}/my/page/widget/'+id+'/config',{},function callBack(data){
		if(data.success){
			processWidgetConfig(id,data.result,widgetId,type);
		}else{
			$.messager.popup(data.result);
		}
	});
};

function processWidgetConfig(id,config,widgetId,type){
	$("#configForm").html("");	
	var btn = $("#submit-config");
	handleWidgets(type,id,config,widgetId,btn);
}

function showWidgetUpdateModal(r,x,y,width,lwId,wId,type){
	$("#update-r").val(r);
	$("#update-x").val(x);
	$("#update-y").val(y);
	$("#update-width").val(width);
	var btn = $("#update-btn")
	btn.unbind("click");
	btn.bind("click",function(){
		var url = "${ctx}/my/page/widget/update?wrap="+$('#wrap').is(':checked');
		var data = {};
		var _widget = {};
		_widget.id = wId;
		_widget.type = type;
		data.widget = _widget;
		data.r = $("#update-r").val();
		data.x = $("#update-x").val();
		data.y = $("#update-y").val();
		data.width = $("#update-width").val();
		data.id = lwId;
		var page = {"id":'${page.id}'};
		data.page = page;
		btn.button("loading");
		post(url,data,function callBack(result){
			if(result.success){
				$("#updateLocationWidgetModal").modal("hide");
				$.messager.popup("更新成功");
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
	})
	$("#updateLocationWidgetModal").modal("show");
}
</script>

</html>