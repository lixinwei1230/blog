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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<sec:authentication property='principal.space' var="space" />
<title><sec:authentication property='principal.nickname' /></title>
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
		<div class="row">
			<div class="col-lg-8 col-md-8  text">
				<c:choose>
					<c:when test="${not empty oauths }">
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>绑定类型</th>
										<th>绑定时间</th>
										<th><spring:message code="page.item.option" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="oauth" items="${oauths}">
										<tr>
											<td><c:choose>
													<c:when test="${oauth.type == 'QQ' }">
												QQ
											</c:when>
												</c:choose></td>
											<td><fmt:formatDate value="${oauth.createDate }"
													pattern="yyyy-MM-dd HH:mm" /></td>
											<td><a href="javascript:void(0)"
												onclick="unbind('${oauth.type}')" title="解除绑定"><span
													class="glyphicon glyphicon-remove-sign"
													style="margin-right: 10px" aria-hidden="true"></span></a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-info">
							<spring:message code="page.oauth.none" />
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-lg-4 col-md-4 text"></div>
		</div>
		<input type="hidden" value="${param.begin }" id="beginDate" />
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		function unbind(type){
			$.messager.confirm("解除绑定", "是否要解除绑定，您可以下次再绑定", function() { 
				var flag = true;
				if(flag){
					flag = false;
					post(contextPath+"/my/oauth/unbind",{"type":type},function(data){
						if(data.success){
							$.messager.popup("解除绑定成功");
							window.location.reload();
						}else{
							$.messager.popup(data.result);
						}
					},function (request) {  
		                if(request.status == "403"){
		                	$.messager.popup("没有权限进行这项操作");
		                };
		             },function(){
		            	 flag = true;
		             });
				}
			});
		}
	</script>
</body>
</html>
