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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<sec:authentication property='principal.space' var="space" />
<title><sec:authentication property='principal.nickname' />
</title>
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${ctx}/static/plugins/jupload/9.5.7/css/jquery.fileupload.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
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
																		onerror="javascript:this.src='${ctx}/static/imgs/img-missing.png'" />
																	</a>
																</div>
															</c:when>
															<c:otherwise>
																<img
																	src="${file.url}/200"
																	class="img-responsive"
																	style="max-width: 200px; max-height: 200px"
																	onerror="javascript:this.src='${ctx}/static/imgs/img-missing.png'" />
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
														<a href="${file.url }"
															title="原始图片" target="_blank"><span
															class="glyphicon glyphicon-picture" aria-hidden="true"></span>
														</a>
													</c:when>
													<c:otherwise>
														<a href="${file.url}"
															title="文件下载" ><span
															class="glyphicon glyphicon-download-alt"
															aria-hidden="true"></span>
														</a>
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
										</c:choose>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="col-lg-4 col-md-4 text">
				<div class="panel panel-default" style="margin-top:20px">
					<div class="panel-heading">
						<h3>文件查找</h3>
					</div>
					<div class="panel-body">
						<form action="${ctx }/my/file/list/1" method="get">
							 <div class="form-group">
							    <label >文件名</label>
							    <input type="text" name="name" class="form-control" placeholder="文件名" value="<c:out value="${param.name }"/>">
							  </div>
							  <div class="form-group">
							    <label >开始日期</label>
							    <input type="text" name="begin" class="form-control" placeholder="yyyy-MM-dd HH:mm:ss" value="<c:out value="${param.begin }"/>">
							  </div>
							  <div class="form-group">
							    <label >结束日期</label>
							    <input type="text" name="end" class="form-control" placeholder="yyyy-MM-dd HH:mm:ss" value="<c:out value="${param.end }"/>">
							  </div>
							  <div class="alert alert-warning">
							  		开始日期和结束日期必须同时存在
							  </div>
							  <button type="submit" class="btn btn-default">查找</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<jsp:include page="../optionalMessage.jsp" />
	<script type="text/javascript">
		function deleteFile(id, o) {
			$("#optionalMessageModal").modal("show");
				$("#optionalMessage-confirm").unbind().bind('click',function(){
					var btn = $(this);
					var data = {};
					var form = $("#optionalMessageForm");
					data.title = form.find("input[name='title']").val();
					data.content = form.find("textarea[name='content']").val();
					btn.button("loading")
					post(contextPath + "/manage/file/delete?id="+id, data, function callBack(data){
						if(data.success){
							$.messager.popup('操作成功');
							o.parent().parent().remove();
							$("#optionalMessageModal").modal("hide");
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
		}
		
	</script>
</body>
</html>
