<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="u" uri="/url"%>
<%@taglib prefix="r" uri="/resize"%>
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
<link
	href="${staticSourcePrefix }/plugins/jupload/9.5.7/css/jquery.fileupload.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8  text">
				<c:set value="${page.datas }" var="files" />
				<c:choose>
					<c:when test="${not empty files }">
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>文件名</th>
										<th>上传日期</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="file" items="${files }">
										<tr>
											<td><c:choose>
													<c:when test="${file.image }">
														<c:choose>
															<c:when test="${file.cover != null }">
																<div class="videos">
																	<a
																		data-play="${file.url}"
																		class="video"> <span></span> <img
																		src="${file.cover.url}"
																		class="img-responsive"
																		onerror="javascript:this.src='${staticSourcePrefix }/imgs/img-missing.png'" />
																	</a>
																</div>
															</c:when>
															<c:otherwise>
																<img
																	src='<r:resize url="${file.url }" size="200"/>'
																	class="img-responsive"
																	style="max-width: 200px; max-height: 200px"
																	onerror="javascript:this.src='${staticSourcePrefix }/imgs/img-missing.png'" />
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when
																test="${fn:length(file.originalFilenameWithoutExtension) > 10}">
														${fn:substring(file.originalFilenameWithoutExtension, 0, 10)}... .${file.extension }
													</c:when>
															<c:otherwise>
														${file.originalFilename }
													</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose></td>
											<td><fmt:formatDate value="${file.uploadDate }"
													pattern="yyyy-MM-dd HH:mm" /></td>
											<td><c:choose>
													<c:when test="${file.image }">
														<a href="${file.url}"
															title="原始图片" target="_blank"><span
															class="glyphicon glyphicon-picture" aria-hidden="true"></span>
														</a>
													</c:when>
													<c:otherwise>
														<a href="${file.url}"
															title="文件下载"><span
															class="glyphicon glyphicon-download-alt"
															aria-hidden="true"></span> </a>
													</c:otherwise>
												</c:choose> &nbsp;&nbsp; <a href="javascript:void(0)"
												onclick="deleteFile('${file.id}',$(this))" title="删除"> <span
													class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
											</a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">当前没有任何文件</div>
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
												<a href="${ctx }/my/file/list/${i }">${i }</a>
											</c:when>
											<c:otherwise>
												<a
													href="${ctx }/my/file/list/${i }?${pageContext.request.queryString}">${i
													}</a>
											</c:otherwise>
										</c:choose></li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 text">
				<button type="button" class="btn btn-primary btn-lg btn-block"
					id="file-upload-btn">文件上传</button>
				<div class="alert alert-warning" style="margin-top: 5px">文件上传成功后请手动刷新</div>
				<a href="${ctx }/my/file/list/1"
					class="btn btn-primary btn-lg btn-block" style="margin-top: 20px">刷新</a>
				<c:if test="${not empty indexs }">
					<div class="panel panel-default" style="margin-top: 20px">
						<div class="panel-heading">
							<h3>文件索引</h3>
						</div>
						<div class="panel-body">
							<div class="panel-group" id="blogDateFileWidget_accordion"
								role="tablist" aria-multiselectable="true">
								<c:forEach var="file" items="${indexs }" varStatus="s">
									<div class="panel panel-default">
										<div class="panel-heading" role="tab">
											<h4 class="panel-title">
												<a role="button" data-toggle="collapse"
													href="#myfileIndex_${s.index+1}" aria-expanded="false"
													aria-controls="#myfileIndex_${s.index+1}" class="collapsed">
													<fmt:formatDate value="${file.begin }" pattern="yyyy" />
													(${file.count})
												</a>
											</h4>
										</div>
										<div id="myfileIndex_${s.index+1}"
											class="panel-collapse collapse " role="tabpanel">
											<table class="table" style="margin-bottom: 0px">
												<c:forEach items="${file.subfiles }" var="_file">
													<tr>
														<td><a
															href="${ctx }/my/file/list/1?begin=<fmt:formatDate value="${_file.begin }" pattern="yyyy-MM-dd HH:mm:ss"/>&end=<fmt:formatDate value="${_file.end }" pattern="yyyy-MM-dd HH:mm:ss"/>">
															<fmt:formatDate value="${_file.begin }" pattern="yyyy-MM" />
															(${_file.count}) </a></td>
													</tr>
												</c:forEach>
											</table>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
				</c:if>
				<div class="panel panel-default" style="margin-top: 20px">
					<div class="panel-heading">
						<h3>文件查找</h3>
					</div>
					<div class="panel-body">
						<form action="${ctx }/my/file/list/1" method="get">
							<div class="form-group">
								<label>文件名</label> <input type="text" name="name"
									class="form-control" placeholder="文件名"
									value="<c:out value="${param.name }"/>">
							</div>
							<div class="form-group">
								<label>开始日期</label> <input type="text" name="begin"
									class="form-control" placeholder="yyyy-MM-dd HH:mm:ss"
									value="<c:out value="${param.begin }"/>">
							</div>
							<div class="form-group">
								<label>结束日期</label> <input type="text" name="end"
									class="form-control" placeholder="yyyy-MM-dd HH:mm:ss"
									value="<c:out value="${param.end }"/>">
							</div>
							<div class="alert alert-warning">开始日期和结束日期必须同时存在</div>
							<button type="submit" class="btn btn-default">查找</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>

	<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">文件上传</h4>
				</div>
				<div class="modal-body">
					<form id="fileupload" class="bs-example form-horizontal"
						autocomplete="off" action="${ctx }/upload" method="POST"
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
									<i class="glyphicon glyphicon-ban-circle"></i> <span><spring:message
											code="page.item.cancel" /> </span>
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
								<div class="progress progress-striped active" role="progressbar"
									aria-valuemin="0" aria-valuemax="100">
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
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="page.item.close" />
					</button>
				</div>
			</div>
		</div>
	</div>
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
            {% if (file.error) { %}
                <div><span class="label label-danger"><spring:message code="global.error"/></span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.error) { %}
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
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/load-image.min.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/tmpl.min.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/canvas-to-blob.min.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/jquery.fileupload.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/jquery.fileupload-process.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/jquery.fileupload-image.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/jquery.fileupload-validate.js"></script>
	<script type="text/javascript"
		src="${staticSourcePrefix }/plugins/jupload/9.5.7/js/jquery.fileupload-ui.js"></script>
<script type="text/javascript">
		$(document).ready(function(){
			$("#file-upload-btn").click(function(){
				$("#uploadModal").modal('show');
			});
		});
		$('#fileupload').fileupload({
				dataType : 'json',
				maxFileSize : 2048576,
				autoUpload : false,
				maxNumberOfFiles : 5,
				singleFileUploads : false,
				limitMultiFileUploads : 5,
				limitConcurrentUploads : 1,
				acceptFileTypes : /(\.|\/)(jpg|jpeg|png|zip|rar|gif|doc|docx|java|htm|html|js|xml|xls)$/i
		});
		function deleteFile(id, o) {
			$.messager.confirm('<spring:message code="page.item.delete" />',
					'确定要删除这个文件，文件被删除后，在一段时间内仍可以被访问', function() {
						$.post("${ctx}/my/file/delete", {
							id : id
						}, function callBack(data) {
							if (data.success) {
								$.messager.popup('删除成功');
								o.parent().parent().remove();
							} else {
								$.messager.popup(data.result);
							}
						});
					});
		}
		
	</script>
</body>
</html>
