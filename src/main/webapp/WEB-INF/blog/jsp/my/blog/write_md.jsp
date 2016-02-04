<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-lg-10 col-md-10 col-xs-12">
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.title" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" name="title" value="123"/>
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.content" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<pre><textarea class="form-control" style="height: 600px" id="editor"></textarea></pre>
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
						data-loading-text="<spring:message code="page.item.processing" />">
						<spring:message code="page.blog.category.manage" />
					</button>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label><spring:message code="page.blog.tag" /></label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" id="tag-input"
								placeholder="<spring:message code="page.blog.tag.tip" />" />
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
							<input type="text" class="form-control" name="level" value="0" />
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label>定时发布</label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="checkbox" id="scheduled">
						</div>
					</div>
				</div>
				<div class="row" style="margin-bottom: 15px;display:none">
					<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
						<div class="col-lg-2 col-sm-12 col-xs-12 col-md-2">
							<label>发布日期</label>
						</div>
						<div class="col-lg-10 col-sm-12 col-md-10 col-xs-12">
							<input type="text" class="form-control" id="scheduled-time" value="" />
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
					<spring:message code="page.blog.temporary.add.tip" />
				</div>
				<div id="temporarySave-tip"></div>
			</div>
			<div class="col-lg-2 col-md-2  col-xs-12">
				<button id="file-manager-btn" class="btn btn-lg btn-primary btn-block">文件管理</button>
			</div>
			<div class="col-lg-2 col-md-2  col-xs-12" style="margin-top:10px">
				<button id="preview-btn" class="btn btn-lg btn-primary btn-block">预览</button>
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
	<u:url space="${space }" var="spaceLinkPrefix" />
	﻿<div class="modal fade" id="ckeditor_myFileModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
			<div class="modal-dialog  modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">文件管理</h4>
					</div>
					<div class="modal-body">
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" class="active"><a href="#home"
								aria-controls="home" role="tab" data-toggle="tab">文件</a></li>
							<li role="presentation"><a href="#upload"
								aria-controls="upload" role="tab" data-toggle="tab">文件上传</a></li>
		                  <li><a href="javascript:void(0)" id="ckeditor_file_manager_refresh" >刷新</a></li>
						</ul>
						<div class="tab-content" style="margin-top: 20px">
						<div role="tabpanel" class="tab-pane active" id="home"></div>
							<div role="tabpanel" class="tab-pane" id="upload">
								<form id="ckeditor_fileupload" class="bs-example form-horizontal"
								autocomplete="off"  method="POST" action="${ctx }/upload"
									enctype="multipart/form-data">
									<div class="row fileupload-buttonbar">
										<div class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
											<span class="btn btn-success fileinput-button"> <i
												class="glyphicon glyphicon-plus"></i> <span>增加文件 </span> <input
												type="file" name="files" multiple="">
											</span>
											<button type="submit" class="btn btn-primary start">
												<i class="glyphicon glyphicon-upload"></i> <span>开始上传 </span>
											</button>
											<button type="reset" class="btn btn-warning cancel">
											<i class="glyphicon glyphicon-ban-circle"></i> <span>取消</span>
											</button>
											<!-- The global file processing state -->
											<span class="fileupload-process"></span>
										</div>
									</div>
									<input type="hidden" name="album" />
									<div class="row" style="margin-top: 10px">
										<div
											class="fileupload-progress fade col-lg-12 col-sm-12 col-md-12 col-xs-12">
											<!-- The global progress bar -->
											<div class="progress progress-striped active"
												role="progressbar" aria-valuemin="0" aria-valuemax="100">
												<div class="progress-bar progress-bar-success"
													style="width: 0%;"></div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
											<div class="table-responsive">
												<table role="presentation" class="table table-striped"
													style="text-align: center">
													<tbody class="files"></tbody>
												</table>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							关闭
						</button>
					</div>
				</div>
			</div>
		</div>
		<form style="display: none" action='${ctx }/my/blog/preview' method="post" id="preview-form" target="_blank">
			<input type="hidden" value="" id="preview-content" name="content"/>
			<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
		</form>
		
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
	<script type="text/javascript"
		src="${urlHelper.getStaticResourcePrefix(2) }/plugins/md/marked.js"></script>	
	<script type="text/javascript">
	var categoryIndex;

	function showManageBlogCategoryModal(o) {
	    var btn = o;
	    btn.button("loading");
	    var url = "${ctx}/my/blog/category/all";
	    $.get(url, {}, function callBack(data) {
	        if (data.success) {
	            var rows = data.result;
	            rows.push({
	                "name": "",
	                "order": 0,
	                "id": null
	            });
	            var $editTable = $('#categoryTable');
	            $editTable.datagrid({
	                columns: [
	                    [{
	                        title: '<spring:message code="page.blog.category.name" />',
	                        field: "name"
	                    }, {
	                        title: '<spring:message code="page.blog.category.order" />',
	                        field: "order"
	                    }]
	                ],
	                edit: true,
	                singleSelect: true //false allow multi select
	                    ,
	                selectedClass: 'success',
	                selectChange: function(selected, rowIndex, rowData, $row) {
	                    categoryIndex = rowIndex;
	                }
	            }).datagrid("loadData", {
	                rows: rows
	            });

	            $("#categoryWrap").dialog({
	                dialogClass: "modal-md",
	                backdrop: "static",
	                title: '<spring:message code="page.blog.category.manage" />',
	                buttons: [{
	                    text: '<spring:message code="page.item.close" />',
	                    'class': "btn-primary",
	                    click: function() {
	                        $(this).dialog("close");
	                    }
	                }, {
	                    text: '<spring:message code="page.item.delete" />',
	                    'class': "btn-primary",
	                    id: 'deleteCategory',
	                    click: function() {
	                        var selections = $('#categoryTable').datagrid("getSelections");
	                        if (selections.length == 0) {
	                            $.messager.popup('<spring:message code="page.option.selectOne" />');
	                        } else {
	                            var select = selections[0];
	                            var btn = $("#deleteCategory");
	                            btn.prop("disabled", true);
	                            post("${ctx}/my/blog/category/delete?id=" + select["id"], {}, function(_result) {
	                                if (_result.success) {
	                                    $('#categoryTable').datagrid("deleteRow", categoryIndex);
	                                    writeCategorys();
	                                } else {
	                                    var result = _result.result;
	                                    $.messager.popup(result);
	                                }
	                            }, function(request) {
	                                if (request.status == "400") {
	                                    var res = $.parseJSON(request.responseText);
	                                    if (res.result) {
	                                        var html = "";
	                                        for (var i = 0; i < res.result.length; i++) {
	                                            var ef = res.result[i];
	                                            html += ef.error + "<br/>";
	                                        }
	                                        $.messager.popup(html);
	                                    };
	                                };
	                            }, function() {
	                                btn.prop("disabled", false);
	                            });
	                        }
	                    }
	                }, {
	                    text: '<spring:message code="page.item.sure" />',
	                    id: 'addCategory',
	                    'class': "btn-success",
	                    click: function(e) {
	                        var selections = $('#categoryTable').datagrid("getSelections");
	                        if (selections.length == 0) {
	                            $.messager.popup('<spring:message code="page.option.selectOne" />');
	                        } else {
	                            var btn = $('#addCategory');
	                            btn.prop("disabled", true);
	                            var select = selections[0];
	                            var isUpdate = select["id"] != null;
	                            post("${ctx}/my/blog/category/addOrUpdate", select, function(_result) {
	                                if (_result.success) {
	                                    $('#categoryTable').datagrid("deleteRow", categoryIndex);
	                                    var record = _result.result;
	                                    $('#categoryTable').datagrid("insertRow", {
	                                        index: categoryIndex,
	                                        row: record
	                                    });
	                                    $.messager.popup('<spring:message code="page.option.success" />');
	                                    if (!isUpdate) {
	                                        $('#categoryTable').datagrid("insertRow", {
	                                            index: categoryIndex + 1,
	                                            row: {
	                                                "name": "",
	                                                "order": 0,
	                                                "id": null
	                                            }
	                                        });
	                                    }
	                                    writeCategorys();
	                                } else {
	                                    var result = _result.result;
	                                    $.messager.popup(result);
	                                }
	                            }, function(request) {
	                                if (request.status == "400") {
	                                    var res = $.parseJSON(request.responseText);
	                                    if (res.result) {
	                                        var html = "";
	                                        for (var i = 0; i < res.result.length; i++) {
	                                            var ef = res.result[i];
	                                            html += ef.error + "<br/>";
	                                        }
	                                        $.messager.popup(html);
	                                    };
	                                };
	                            }, function() {
	                                btn.prop("disabled", false);
	                            });
	                        }
	                    }
	                }]
	            });
	        } else {
	            $.message.popup(data.result);
	        }
	        btn.button("reset");
	    });
	}

	function writeCategorys() {
	    var url = "${ctx}/my/blog/category/all";
	    $.get(url, {}, function callBack(data) {
	        if (data.success) {
	            var rows = data.result;
	            $("#categorys").empty();
	            for (var i = 0; i < rows.length; i++) {
	                var row = rows[i];
	                $("#categorys").append('<option value="' + row.id + '">' + row.name + '</option>');
	            }
	        }
	    });
	}
	var editor;
	$(document).ready(function() {
		$("#preview-btn").click(function(){
			$("#preview-content").val(marked(getContent()));
			$("#preview-form").submit();
		});
		$('#ckeditor_fileupload').fileupload({
			dataType : 'json',
			maxFileSize : 2048576,
			autoUpload : false,
			maxNumberOfFiles : 5,
			singleFileUploads : false,
			limitMultiFileUploads : 5,
			limitConcurrentUploads : 1,
			acceptFileTypes : /(\.|\/)(jpg|jpeg|png|zip|rar|gif|doc|docx|java|htm|html|js|xml|xls)$/i
		});
		$("#ckeditor_file_manager_refresh").click(function(){
			writePage(1);
		});
		$("#file-manager-btn").click(function(){
			writePage(1);
			$("#ckeditor_myFileModal").modal("show");
		});
	    writeCategorys();
	    $(this).hide();
	    $("#tag-input").keydown(function(e) {
	        if (e.keyCode == 13) {
	            var value = $(this).val();
	            if ($.trim(value) == "") {
	                $.messager.popup("标签不能为空");
	                return;
	            }
	            if (value.length > 15) {
	                $.messager.popup("标签长度不能超过15个字符");
	                return;
	            }
	            if (addTag(value))
	                $(this).val("");
	        }
	    });
	    $("#btn-submit").click(function() {
	        var btn = $(this);
	        var data = {};
	        var webTags = [];
	        for (var i = 0; i < tags.length; i++) {
	            webTags.push({
	                "name": tags[i]
	            });
	        }
	        data.tags = webTags;
	        data.title = $("input[name='title']").val();
	        data.content = getContent();
	        data.category = {
	            "id": $("#categorys").val()
	        };
	        data.from = $("select[name='from']").val();
	        data.scope = $("select[name='scope']").val();
	        data.commentScope = $("select[name='commentScope']").val();
	        var level = $("input[name='level']").val();
	        if (level == "" || isNaN(level)) {
	            level = 0;
	        }
	        data.level = level;
	        if ($("#scheduled").is(':checked')) {
	            data.status = 'SCHEDULED';
	            data.writeDate = $("#scheduled-time").val();
	        } else {
	            data.status = 'NORMAL';
	        }
	        data.display = marked(data.content);
	        data.editor = 'MD';
	        btn.button("loading");
	        var url = "${ctx}/my/blog/write";
	        temporarySaveFlag = false;
	        post(url, data, function(result) {
	            if (result.success) {
	                window.location.href = '${spaceLinkPrefix}/blog/' + result.result;
	            } else {
	                $.messager.popup(result.result);
	                temporarySaveFlag = true;
	            }
	        }, function(request) {
	            if (request.status == "400") {
	                var res = $.parseJSON(request.responseText);
	                if (res.result) {
	                    var html = "";
	                    for (var i = 0; i < res.result.length; i++) {
	                        var ef = res.result[i];
	                        html += ef.error + "<br/>";
	                    }
	                    $.messager.popup(html);
	                };
	            }
	            if (request.status == '403') {
	                $.messager.popup("权限验证失败");
	            };
	            temporarySaveFlag = true;
	        }, function() {
	            btn.button("reset");
	        });
	    });

	    var dd = new Date();
	    dd.setDate(dd.getDate() + 1); //获取AddDayCount天后的日期   
	    var tm = dd.pattern("yyyy-MM-dd");
	    $("#scheduled-time").val(tm + " 00:00:00");
	    $("#scheduled").click(function() {
	        var me = $(this);
	        if (me.is(':checked')) {
	            $("#scheduled-time").parent().parent().parent().show();
	        } else {
	            $("#scheduled-time").parent().parent().parent().hide();
	        }
	    });

	});
	var tags = [];
	var tagContainer = $("#tag-container");

	function addTag(tag) {
	    if (tags.length == 5) {
	        $.messager.popup("最多只能添加5个标签，请删除其他标签再尝试");
	        return false;
	    }
	    var index = $.inArray(tag, tags);
	    if (index == -1) {
	        tags.push(tag);
	        var _index = $.inArray(tag, tags);
	        tagContainer.append('<span id="tag-' + _index + '" style="margin-right:10px"><span class="glyphicon glyphicon-tag" aria-hidden="true"><a href="javascript:void(0)" onclick="removeTag(' + _index + ')">' + tag + '</a></span><span>');
	    }
	    return true;
	}

	function removeTag(index) {
	    $("#tag-" + index).remove();
	    tags.splice(index, 1);
	}

	function getContent() {
	    return $("#editor").val();
	}

	var temporarySaveFlag = true;

	var lastTemporaryContent = "";

	function temporarySave() {
	    if (temporarySaveFlag) {
	        var now = new Date();
	        temporarySaveFlag = false;
	        var data = {};
	        var webTags = [];
	        for (var i = 0; i < tags.length; i++) {
	            webTags.push({
	                "name": tags[i]
	            });
	        }
	        data.tags = webTags;
	        var title = $("input[name='title']").val();
	        if ($.trim(title) == "") {
	            title = new Date().pattern("yyyy-MM-dd HH:mm:ss");
	        }
	        data.title = title;
	        data.content = getContent();
	        if ($.trim(data.content) == "" || data.content == lastTemporaryContent) {
	            temporarySaveFlag = true;
	            return;
	        }
	        var categoryId = $("#categorys").val();
	        if (categoryId == "") {
	            categoryId = -1;
	        }
	        data.category = {
	            "id": categoryId
	        };
	        var from = $("select[name='from']").val();
	        if (from == "") {
	            from = "ORIGINAL";
	        }
	        data.from = from;
	        var scope = $("select[name='scope']").val();
	        if (scope == "") {
	            scope = "PRIVATE";
	        }
	        data.scope = scope;
	        var commentScope = $("select[name='commentScope']").val();
	        if (commentScope == "") {
	            commentScope = "PUBLIC";
	        }
	        data.commentScope = commentScope;
	        var level = $("input[name='level']").val();
	        if (level == "" || isNaN(level) || (level < 0) || (level > 100)) {
	            level = 0;
	        }
	        data.level = level;
	        if ($("#scheduled").is(':checked')) {
	            data.status = 'SCHEDULED';
	            data.writeDate = $("#scheduled-time").val();
	        } else {
	            data.status = 'NORMAL';
	        }
	        data.display = marked(data.content);
	        data.editor = 'MD';
	        var url = '${ctx}/my/blog/temporary/save';
	        post(url, data, function(result) {
	            if (result.success) {
	                $("#temporarySave-tip").addClass("alert alert-success").html("临时博客于" + now.pattern("yyyy-MM-dd HH:mm:ss") + "保存成功");
	                lastTemporaryContent = data.content;
	            }
	        }, function(request) {
	            //ignore any error
	        }, function() {
	            temporarySaveFlag = true;
	        });
	    }
	}

	setInterval("temporarySave()", 15000);
	
	var files = [];
	
	function writePage(page){
		$.get(contextPath + "/my/file/list/"+page,{},function callBack(data){
			if(data.success){
				var page = data.result;
				var html = "";
				var datas = page.datas;
				if(datas.length == 0){
					html = '<div class="alert alert-info">当前没有任何文件</div>';
				}else{
					html += '<div class="table-responsive">';
					html += '<table class="table">';
					html += '<thead><tr><th>文件名</th><th>上传日期</th><th>操作</th></tr></thead>';
					html += '<tbody>';
					for(var i=0;i<datas.length;i++){
						var file = datas[i];
						files.push(file);
						html += '<tr>';
						if(file.image){
							if(file.cover && file.cover != null){
								 html += '<td><div class="videos">';
								 html += '<a class="video" data-play="'+file.url+'"> <span>&nbsp;</span> <img class="img-responsive" src="'+file.cover.url+'"> </a> ';
							     html += '<div class="clearfix">&nbsp;</div>'
								 html += '</div></td>'; 
							}else{
								html += '<td><img src="'+getResizeUrl(file.url,200)+'"/></td>';
							}
						}else{
							var name = file.originalFilenameWithoutExtension;
							if(name.length > 10){
								name = name.substring(0,10)+"...";
							}
							html += '<td>'+name+'.'+file.extension+'</td>';
						}
						html += '<td>'+new Date(file.uploadDate).pattern("yyyy-MM-dd HH:mm")+'</td>';
						html += '<td><a href="javascript:void(0)" class="insert-'+file.id+'">插入</a>';
						if(file.image){
							html += '&nbsp;&nbsp;<a href="'+file.url+'" target="_blank">原图</a>';
						}
						html += '</td></tr>';
					}
					html += '</tbody>';
					html += '</table>';
					html += '</div>';
				}
				if(page.totalPage > 1){
	    			html += '<div class="row"  id="ckeditor_file_manager_paging">';
	    			html += '<div class="col-md-12">';
	    			html += '<ul class="pagination">';
	    			for(var i=page.listbegin;i<=page.listend-1;i++){
	    				html += '<li><a href="javascript:void(0)" class="page-'+i+'">'+i+'</a></li>';
	    			}
	    			html += '</ul>';
	    			html += '</div>';
	    			html += '</div>';
	    		}
				$("#home").html(html);
				if(page.totalPage > 1){
					$("#ckeditor_file_manager_paging a[class^='page-']").on('click',function(){
						var _me = $(this);
						var clazz = _me.attr("class");
						var page = clazz.split("-")[1];
						writePage(page);
					});
				}
				$("#home a[class^='insert-']").on('click',function(){
					var _me = $(this);
					var clazz = _me.attr("class");
					var id = clazz.split("-")[1];
					for(var i=0;i< files.length;i++){
						var file = files[i];
						if(file.id == id){
							if(file.image){
								if(file.cover && file.cover != null){
									 $("#editor").insertAtCaret(' [!['+file.originalFilename+']('+file.cover.url+')]('+file.url+')');
								}else{
									 $("#editor").insertAtCaret('!['+file.originalFilename+']('+getResizeUrl(file.url,600)+')');
									//me.editor.insertHtml('<img class="img-responsive" src="'+getResizeUrl(file.url,600)+'"/>');
								}
							}else{
								$("#editor").insertAtCaret('['+file.originalFilename+']('+file.url+')');
							}
							
							return ;
						}
					}
				});
			}else{
				$.messager.popup(data.result);
			}
		});
	}
	
	//

	</script>
</body>
</html>
