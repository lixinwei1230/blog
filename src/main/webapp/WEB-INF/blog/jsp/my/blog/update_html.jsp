<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<!DOCTYPE html>
<html lang="zh-cn">
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
	<link
	href="${staticSourcePrefix }/plugins/jupload/9.5.7/css/jquery.fileupload.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp" />
	<div class="container">
		<div class="row">
			<c:if test="${not empty blog.tags }">
				<c:forEach var="tag" items="${blog.tags }">
					<input type="hidden" class="oldTag"
						value="<c:out value='${tag.name }'/>" />
				</c:forEach>
			</c:if>
			<input type="hidden" value="<c:out value='${blog.category.name }'/>"
				id="categoryName" />
			<div class="col-lg-10 col-md-10 col-xs-12">
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.title" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" name="title"
								value="<c:out value='${blog.title }'/>" />
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.content" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<textarea class="form-control" style="height: 600px" id="editor"><c:out
									value="${blog.content }" /></textarea>
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.from" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<select class="form-control" name="from">
								<c:if test="${blog.from == 'ORIGINAL' }">
									<option value="${blog.from }"><spring:message
											code="page.item.current" />:
										<spring:message code="page.blog.from.original" /></option>
								</c:if>
								<c:if test="${blog.from == 'COPIED' }">
									<option value="${blog.from }"><spring:message
											code="page.item.current" />:
										<spring:message code="page.blog.from.copied" /></option>
								</c:if>
								<option value="ORIGINAL"><spring:message
										code="page.blog.from.original" /></option>
								<option value="COPIED"><spring:message
										code="page.blog.from.copied" /></option>
							</select>
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.category" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<select id="categorys" class="form-control"></select>
						</div>
					</div>
				</div>
				<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
					style="margin-bottom: 15px">
					<button class="btn btn-primary" style="float: right"
						onclick="showManageBlogCategoryModal($(this))"
						data-loading-text="处理中">博客分类管理</button>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.tag" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" id="tag-input"
								placeholder="最多5个标签，每个标签最多15个字符,使用回车确认标签" />
						</div>
					</div>
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2"></div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<div class="well" id="tag-container"></div>
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.scope" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<select class="form-control" name="scope">
								<c:if test="${blog.scope == 'PUBLIC' }">
									<option value="${blog.scope }"><spring:message
											code="page.item.current" />:
										<spring:message code="page.scope.public" /></option>
								</c:if>
								<c:if test="${blog.scope == 'PRIVATE' }">
									<option value="${blog.scope }"><spring:message
											code="page.item.current" />:
										<spring:message code="page.scope.private" /></option>
								</c:if>
								<option value="PUBLIC"><spring:message
										code="page.scope.public" /></option>
								<option value="PRIVATE"><spring:message
										code="page.scope.private" /></option>
							</select>
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.comment.scope" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<select class="form-control" name="commentScope">
								<c:if test="${blog.commentScope == 'PUBLIC' }">
									<option value="PUBLIC"><spring:message
											code="page.item.current" />:
										<spring:message code="page.scope.public" /></option>
								</c:if>
								<c:if test="${blog.commentScope == 'PRIVATE' }">
									<option value="PRIVATE"><spring:message
											code="page.item.current" />:
										<spring:message code="page.scope.private" /></option>
								</c:if>
								<option value="PUBLIC"><spring:message
										code="page.scope.public" /></option>
								<option value="PRIVATE"><spring:message
										code="page.scope.private" /></option>
							</select>
						</div>
					</div>
				</div>

				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label>排序值(0~100之间，越大越靠前)</label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" name="level"
								value="${blog.level }" />
						</div>
					</div>
				</div>
				
					<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label>定时发布</label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<c:choose>
								<c:when test="${blog.scheduled }">
									<input type="checkbox" id="scheduled" checked="checked">
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="scheduled">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
				<c:choose>
					<c:when test="${blog.scheduled }">
						<c:set value="" var="style"/>
					</c:when>
					<c:otherwise>
						<c:set value="display:none" var="style"/>
					</c:otherwise>
				</c:choose>
				<div class="row" style="margin-bottom: 15px;${style}">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label>发布日期</label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" id="scheduled-time" value="<fmt:formatDate value="${blog.writeDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
						</div>
					</div>
				</div>
				
				<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12"
					style="margin-bottom: 15px">
					<button class="btn btn-primary" style="float: right"
						id="btn-submit"
						data-loading-text="<spring:message code="page.item.processing" />">
						<spring:message code="page.item.submit" />
					</button>
				</div>
			</div>
			<div class="col-lg-2 col-md-2  col-xs-12">
				<div class="alert alert-warning">
					<spring:message code="page.blog.temporary.update.tip" />
				</div>
				<div id="temporarySave-tip"></div>
			</div>
			<div class="hide">
				<div id="categoryWrap">
					<div class="alert alert-info">
						<spring:message code="page.blog.category.order.tip" />
					</div>
					<div class="table-responsive">
						<table id="categoryTable" class="table table-hover">

						</table>
					</div>
				</div>
			</div>

		</div>
	</div>

	<c:if test="${tBlog != null }">
		<div class="modal fade" id="temporaryBlogModal" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
			data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">临时博客</div>
					<div class="modal-body">
						您在 <strong><fmt:formatDate value="${tBlog.saveDate }"
								pattern="yyyy-MM-dd HH:mm:ss" /></strong>临时保存博客，是否载入？
					</div>
					<div class="modal-footer">
						<a href="${ctx }/my/blog/temporary/${tBlog.dummyId}"
							class="btn btn-primary ">确定</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="page.item.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</c:if>
	<u:url space="${space }" var="spaceLinkPrefix" />
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td>
            <p class="size">Processing...</p>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
        </td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start" disabled>
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>开始上传</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span><spring:message code="page.item.cancel"/></span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <p class="name">
                <span>{%=file.originalFilename%}</span>
            </p>
            {% if (file.errorMessage) { %}
                <div><span class="label label-danger"><spring:message code="global.error"/></span> {%=file.errorMessage%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.errorMessage) { %}
 			<button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span><spring:message code="page.item.cancel"/></span>
                </button>
               
            {% } else { %}
                <button class="btn btn-success cancel">
                    <span><spring:message code="global.success"/></span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/ckeditor/ckeditor.js"></script>
		<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(1) }/plugins/jupload/9.5.7/js/load-image.min.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(1) }/plugins/jupload/tmpl.min.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(1) }/plugins/jupload/canvas-to-blob.min.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(1) }/plugins/jupload/9.5.7/js/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(1) }/plugins/jupload/9.5.7/js/jquery.fileupload.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(2) }/plugins/jupload/9.5.7/js/jquery.fileupload-process.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(2) }/plugins/jupload/9.5.7/js/jquery.fileupload-image.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(2) }/plugins/jupload/9.5.7/js/jquery.fileupload-validate.js"></script>
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(2) }/plugins/jupload/9.5.7/js/jquery.fileupload-ui.js"></script>
	<script type="text/javascript">
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var categoryIndex;
	function showManageBlogCategoryModal(o){
		var btn = o;
		btn.button("loading");
		var url = "${ctx}/my/blog/category/all";
		$.get(url,{},function callBack(data){
			if(data.success){
				var rows = data.result;
				rows.push({"name":"","order":0,"id":null});
				var $editTable = $('#categoryTable');
				$editTable.datagrid({
				    columns:[[
				            {title: '<spring:message code="page.blog.category.name" />', field: "name"}
				            , {title: '<spring:message code="page.blog.category.order" />', field: "order"}
				          ]]
				          , edit: true
				          , singleSelect  : true //false allow multi select
				          , selectedClass : 'success'
				          , selectChange  : function(selected, rowIndex, rowData, $row) {
				              categoryIndex = rowIndex;
				            }
				        }).datagrid("loadData", {rows: rows});

				$("#categoryWrap").dialog({
					dialogClass : "modal-md",
					backdrop: "static",
					title:'<spring:message code="page.blog.category.manage" />',
					 buttons: [
			            {
			                text: '<spring:message code="page.item.close" />'
			              , 'class': "btn-primary"
			              , click: function() {
			                  $(this).dialog("close");
			                }
			            },
			            {
			                text: '<spring:message code="page.item.delete" />'
			              , 'class': "btn-primary"
			              ,id : 'deleteCategory'
			              , click: function() {
			            	  var selections = $('#categoryTable').datagrid("getSelections");
				            	if(selections.length == 0){
				            		$.messager.popup('<spring:message code="page.option.selectOne" />');
				            	}else{
				            		var select = selections[0];
			            			var btn = $("#deleteCategory");
			            			btn.prop("disabled",true);
			            			post("${ctx}/my/blog/category/delete?id="+select["id"],{},function(_result){
				    	            	if (_result.success) {
				    	            		$('#categoryTable').datagrid("deleteRow", categoryIndex);
				    	            		writeCategorys();
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
				    	                	};
				    	                };
				    	             },function(){
				    	            	 btn.prop("disabled",false);
				    	             });
				            	}
			                }
			            },
			            {
			                text: '<spring:message code="page.item.sure" />',
			                id : 'addCategory',
			               'class': "btn-success"
			              , click: function(e) {
				            	var selections = $('#categoryTable').datagrid("getSelections");
				            	if(selections.length == 0){
				            		$.messager.popup('<spring:message code="page.option.selectOne" />');
				            	}else{
				            		var btn = $('#addCategory');
				            		btn.prop("disabled",true);
				            		var select = selections[0];
				            		var isUpdate = select["id"] != null;
				            		post("${ctx}/my/blog/category/addOrUpdate",select,function(_result){
				    	            	if (_result.success) {
				    	            		$('#categoryTable').datagrid("deleteRow", categoryIndex);
				    	            		var record = _result.result;
				    	            		$('#categoryTable').datagrid("insertRow", {index : categoryIndex,row: record});
				    	            		$.messager.popup('<spring:message code="page.option.success" />');
				    	            		if(!isUpdate){
				    	            			$('#categoryTable').datagrid("insertRow", {index : categoryIndex+1,row: {"name":"","order":0,"id":null}});
				    	            		}
				    	            		writeCategorys();
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
				    	                	};
				    	                };
				    	             },function(){
				    	            	 btn.prop("disabled",false);
				    	             });
				            	}
			                }
			            }
			          ]
				});
			}else{
				$.message.popup(data.result);
			}
			btn.button("reset");
		});
	}
	function writeCategorys(){
		var url = "${ctx}/my/blog/category/all";
		$.get(url,{},function callBack(data){
			if(data.success){
				var rows = data.result;
				$("#categorys").empty();
				$("#categorys").append('<option value="${blog.category.id}">当前:'+$("#categoryName").val()+'</option>');
				for(var i=0;i<rows.length;i++){
					var row = rows[i];
					$("#categorys").append('<option value="'+row.id+'">'+row.name+'</option>');
				}
			}
		});
	}
	var editor ;
	$(document).ready(function(){
		writeCategorys();
		editor = CKEDITOR.replace("editor");
		CKEDITOR.config.enterMode=CKEDITOR.ENTER_BR;
		CKEDITOR.config.height=600;
		CKEDITOR.config.allowedContent=true;
		CKEDITOR.config.extraPlugins = 'codemirror,mytable,mycode,mydiv,myfile,mylink,mypreview';
		CKEDITOR.config.shiftEnterMode=CKEDITOR.ENTER_P;
		CKEDITOR.config.uploadUrl = contextPath+'/upload';
		CKEDITOR.config.toolbar = [
		    [ 'Source'],
		    [ 'Undo', 'Redo','Bold' ],
		    ['Mytable','Mycode','Mydiv','Myfile','Mylink','Mypreview']
		];
		$(this).hide();
		$("#tag-input").keydown(function(e){
			if(e.keyCode==13){
				var value = $(this).val();
				if($.trim(value) == ""){
					$.messager.popup("标签不能为空");
					return;
				}
				if(value.length > 15){
					$.messager.popup("标签长度不能超过15个字符");
					return;
				}
				if(addTag(value))
					$(this).val("");
			}
		});
		$("#btn-submit").click(function(){
			var btn = $(this);
			var data = {};
			var webTags = [];
			for(var i=0;i<tags.length;i++){
				webTags.push({"name":tags[i]});
			}
			data.id = '${blog.id}';
			data.tags = webTags;
			data.title = $("input[name='title']").val();
			data.content = getContent();
			data.category={"id":$("#categorys").val()};
			data.from = $("select[name='from']").val();
			data.scope = $("select[name='scope']").val();
			data.commentScope = $("select[name='commentScope']").val();
			var level = $("input[name='level']").val();
			if(level == "" || isNaN(level)){
				level = 0;
			}
			data.level = level;
			if($("#scheduled").is(':checked')){
				data.status = 'SCHEDULED';
				data.writeDate = $("#scheduled-time").val();
			}else{
				data.status = 'NORMAL';
			}
			btn.button("loading");
			var url = "${ctx}/my/blog/update";
			temporarySaveFlag = false;
			post(url,data,function(result){
				if(result.success){
					window.location.href = '${spaceLinkPrefix}/blog/${blog.id}';
				}else{
					$.messager.popup(result.result);
					temporarySaveFlag = true;
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
	            	$.messager.popup("没有权限进行这个操作");
	            };
	         },function(){
				btn.button("reset");
				temporarySaveFlag = true;
			});
		});
		var oldTagsO = $(".oldTag");
		if(oldTagsO.length > 0){
			oldTagsO.each(function(){
				addTag($(this).val());
			});
		}
		
		$("#scheduled").click(function(){
			var me = $(this);
			if(me.is(':checked')){
				$("#scheduled-time").parent().parent().parent().show();
			}else{
				$("#scheduled-time").parent().parent().parent().hide();
			}
		});
		
		$("#temporaryBlogModal").on("hidden.bs.modal",function(){
			temporarySaveFlag = true;
		});
		if($("#temporaryBlogModal").length > 0){
			$("#temporaryBlogModal").modal("show");
		}
	});
	var tags = [];
	var tagContainer = $("#tag-container");
	function addTag(tag){
		if(tags.length == 5){
			$.messager.popup("最多只能添加5个标签，请删除其他标签再尝试");
			return false;
		}
		var index = $.inArray(tag, tags);
		if(index == -1){
			tags.push(tag);
			var _index = $.inArray(tag, tags);
			tagContainer.append('<span id="tag-'+_index+'" style="margin-right:10px"><span class="glyphicon glyphicon-tag" aria-hidden="true"><a href="javascript:void(0)" onclick="removeTag('+_index+')">'+tag+'</a></span><span>');
		}
		return true;
	}
	function removeTag(index){
		$("#tag-"+index).remove();
		tags.splice(index,1);
	}
	function getContent(){
		if(editor){
			content = editor.getData();
		}else{
			content = $("#editor").val();
		}
		return content;
	}
	var temporarySaveFlag = false;
	
	if($("#temporaryBlogModal").length == 0){
		temporarySaveFlag = true;
	}
	
	var lastTemporaryContent = "";
	function temporarySave(){
		if(temporarySaveFlag){
			var now = new Date();
			temporarySaveFlag = false;
			var data = {};
			var webTags = [];
			for(var i=0;i<tags.length;i++){
				webTags.push({"name":tags[i]});
			}
			data.tags = webTags;
			data.id = '${blog.id}';
			var title = $("input[name='title']").val();
			if($.trim(title) == ""){
				title = new Date().pattern("yyyy-MM-dd HH:mm:ss");
			}
			data.title = title;
			data.content = getContent();
			if($.trim(data.content) == "" || data.content == lastTemporaryContent){
				temporarySaveFlag = true;
				return ;
			}
			var categoryId = $("#categorys").val();
			if(categoryId == ""){
				categoryId = -1;
			}
			data.category={"id":categoryId};
			var from = $("select[name='from']").val();
			if(from == ""){
				from = "ORIGINAL"
			}
			data.from = from;
			var scope = $("select[name='scope']").val();
			if(scope == ""){
				scope = "PRIVATE";
			}
			data.scope = scope;
			var commentScope = $("select[name='commentScope']").val();
			if(commentScope == ""){
				commentScope = "PUBLIC";
			}
			data.commentScope = commentScope;
			var level = $("input[name='level']").val();
			if(level == "" || isNaN(level) || (level < 0) || (level > 100)){
				level = 0;
			}
			data.level = level;
			if($("#scheduled").is(':checked')){
				data.status = 'SCHEDULED';
				data.writeDate = $("#scheduled-time").val();
			}else{
				data.status = 'NORMAL';
			}
			var url = '${ctx}/my/blog/temporary/save';
			post(url,data,function(result){
				if(result.success){
					$("#temporarySave-tip").addClass("alert alert-success").html("临时博客于"+now.pattern("yyyy-MM-dd HH:mm:ss")+"保存成功");
					lastTemporaryContent = data.content;
				}
			},function (request) {  
	            //ignore any error
	         },function(){
	        	 temporarySaveFlag = true;
			});
		}
	}
	
	setInterval("temporarySave()",15000);

	</script>
</body>
</html>
