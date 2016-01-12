<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="t" uri="/token"%>
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
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container ">
		<div class="row">
			<div class="col-lg-8 col-md-8">
				<c:if test="${error != null }">
					<div style="margin-top: 10px; margin-bottom: 10px"
						class="alert alert-danger errorField" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign"
							aria-hidden="true"></span> <span class="sr-only"><spring:message
								code="global.error" />:</span>
						<spring:message code="${error.code }" arguments="${error.params }" />
					</div>
				</c:if>
				<c:if test="${success != null }">
					<div style="margin-top: 10px; margin-bottom: 10px"
						class="alert alert-success" role="alert">
						<spring:message code="${success.code }" arguments="${success.params }" />
					</div>
				</c:if>
				<div class="alert alert-info">
					为什么要完善信息？
					完善信息后可以开通空间、然后使用系统登录您的账号。
				</div>
				<form action="${ctx }/my/oauth/completeEmail" method="post">
					<div class="row" style="margin-bottom: 20px">
						<div class="col-lg-8 col-md-8 col-sm-8"
							style="margin-bottom: 10px">
							<input type="text" placeholder="请输入邮箱" name="email" id="mail"
								class="form-control" />
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4">
							<button class="btn btn-primary" type="button" id="mail-send">发送业务码</button>
						</div>
					</div>
					<div class="row" id="code-input-container"
						style="margin-bottom: 20px">
						<div class="col-lg-8 col-md-8 col-sm-8"
							style="margin-bottom: 10px">
							<input type="text" placeholder="请输入业务码" name="code"
								class="form-control" />
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4">
							<button class="btn btn-primary" type="button" onclick="submitForm()">发送邮件</button>
						</div>
					</div>
					<t:token />
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#mail-send").click(function(){
				var btn  = $(this);
				btn.prop("disabled",true);
				var url = contextPath + "/my/oauth/authorizeEmail?email="+$("#mail").val();
				post(url,{},function(data){
		        	if(data.success){
		        		$.messager.popup("系统已经向您的邮箱发送了邮件，请注意查收，请将邮件中的业务码填写到下面的输入框中");
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
		        	 btn.prop("disabled",false);
		         });
				
			})
		});
		function submitForm(){
			$("form").submit();
		}
	</script>
</body>
</html>