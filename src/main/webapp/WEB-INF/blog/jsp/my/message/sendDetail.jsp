<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title text" id="myModalLabel">
		<c:out value="${message.detail.title }" />
	</h4>
</div>
<div class="modal-body ">
	<div class="row" style="margin-bottom: 15px">
		<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
			<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
				<label><spring:message code="page.message.content" /></label>
			</div>
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12 text">
				${message.detail.content }</div>
		</div>
	</div>
	<div class="row" style="margin-bottom: 15px">
		<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
			<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
				<label><spring:message code="page.message.sendDate" /></label>
			</div>
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12 text">
				<fmt:formatDate value="${message.sendDate }"
					pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</div>
	</div>
	<div class="row" style="margin-bottom: 15px">
		<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
			<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
				<label><spring:message code="page.message.status" /></label>
			</div>
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12 text">
				<div class="alert alert-info">被删除的信息不会显示在这里</div>
				<div id="receives-container"></div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">
		<spring:message code="page.item.close" />
	</button>
</div>
<script type="text/javascript">
	function page(i){
		var url = contextPath + "/my/message/send/"+'${message.id}'+"/receives/list/"+i;
		$.get(url,function callBack(data){
			if(data.success){
				var page = data.result;
				var datas = page.datas;
				var html = '';
				if(datas.length == 0){
					html += '<div class="alert alert-info">当前没有任何接收人信息</div>';
				}else{
					html += '<div class="table-responsive">';
					html += '<table class="table">';
					html += '<thead>';
					html += '<tr>';
					html += '<th><spring:message code="page.message.receiver" /></th>';
					html += '<th><spring:message code="page.message.status" /></th>';
					html += '</tr>';
					html += '</thead>';
					html += '<tbody>';
					for(var i=0;i<datas.length;i++){
						var data = datas[i];
						html += '<tr>';
						html += '<td>';
						html += data.receiver.nickname;
						html += '</td>';
						if(data.isRead){
							html += '<td> <spring:message code="page.message.read" /></td>';
						}else{
							html += '<td> <spring:message code="page.message.unread" /></td>';
						}
						html += '</tr>';
						
					}
					html += '</tbody>';
					html += '</table>';
					html += '</div>';
				}
				if(page.totalPage > 1){
	    			html += '<div class="row">';
	    			html += '<div class="col-md-12">';
	    			html += '<ul class="pagination">';
	    			for(var i=page.listbegin;i<=page.listend-1;i++){
	    				html += '<li><a href="javascript:void(0)" onclick="page('+i+')">'+i+'</a></li>';
	    			}
	    			html += '</ul>';
	    			html += '</div>';
	    			html += '</div>';
	    		}
				$("#receives-container").html(html);
			}else{
				$.messager.popup(data.result);
			}
		})
	};
	page(1);
</script>