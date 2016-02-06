<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<jsp:include page="/WEB-INF/head_source.jsp"></jsp:include>
<link href="${staticSourcePrefix }/plugins/highlight/styles/github.min.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${staticSourcePrefix }/plugins/jquery/1.11/jquery.min.js"></script>
<script type="text/javascript"
	src="${staticSourcePrefix }/plugins/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${staticSourcePrefix }/common/common.js"></script>
<script type="text/javascript"
	src="${staticSourcePrefix }/plugins/highlight/highlight.pack.js"></script>
<title>预览</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8 text">
				<div id="blog-content" style="margin-bottom: 20px">${preview }</div>
				<div class="clearfix">&nbsp;</div>
				<button class="btn btn-primary" onclick="window.close()" style="margin-top:10px">关闭</button>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$('pre code').each(function(i, block) {
		hljs.highlightBlock(block);
	  });
});
</script>
</html>