<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="头像上传">
<meta name="author" content="梦海澜心">
<meta name="keywords" content="头像上传">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title><sec:authentication property='principal.nickname' /></title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<link href="${ctx}/static/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${ctx}/static/plugins/jupload/9.5.7/css/jquery.fileupload.css"
	rel="stylesheet">
<link href="${ctx}/static/plugins/crop/cropper.min.css" rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${pageContext.request.contextPath}/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
	      <script src="${pageContext.request.contextPath}/plugins/respond/1.3.0/respond.min.js"></script>
	    <![endif]-->
<style type="text/css">
.img-container, .img-preview {
	background-color: #f7f7f7;
	overflow: hidden;
	width: 100%;
	text-align: center;
}

.img-container {
	box-shadow: inset 0 0 5px #eee;
	height: 360px;
}

.img-container>img {
	max-width: 100%;
	max-height: inherit;
}

.img-preview {
	margin-bottom: 10px;
}

@media ( min-width : 992px) {
	.img-container {
		max-height: 450px;
	}
}

.img-preview-sm {
	height: 160px;
	width: 160px;
}

.img-preview-xs {
	height: 64px;
	width: 64px;
}
</style>
</head>

<body>
	<jsp:include page="/WEB-INF/my_navbar.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
				<span class="btn btn-success fileinput-button"> <i
					class="glyphicon glyphicon-plus"></i> <span>选择头像</span> <input
					id="fileupload" type="file" name="file"
					data-url="${ctx }/my/avatar/upload" multiple>
				</span>
				<div id="progress" style="margin-top: 5px" class="progress">
					<div class="progress-bar progress-bar-success"></div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
				<div class="img-container"></div>
			</div>
			<div class="col-lg-4 col-sm-12 col-md-4 col-xs-12">
				<div class="row">
					<div class="col-md-8">
						<div class="img-preview img-preview-sm" onclick="crop()"></div>
					</div>
					<div class="col-md-4">
						<div class="img-preview img-preview-xs" onclick="crop()"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/foot.jsp"></jsp:include>
	<input type="hidden" id="x" value="0" />
	<input type="hidden" id="y" value="0" />
	<input type="hidden" id="w" value="0" />
	<input type="hidden" id="h" value="0" />
	<jsp:include page="/WEB-INF/scripts.jsp"></jsp:include>
	<script type="text/javascript"
		src="${ctx}/static/plugins/jupload/canvas-to-blob.min.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/plugins/jupload/9.5.7/js/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/plugins/jupload/9.5.7/js/jquery.iframe-transport.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/plugins/jupload/9.5.7/js/jquery.fileupload.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/plugins/crop/cropper.min.js"></script>
	<script type="text/javascript">
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var toUp = null;
	$(function () {
	    $('#fileupload').fileupload({
	    	dataType : 'json',
	    	progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        },
	        add: function (e, data) {
	        	toUp = data.files[0];
	        	var name = toUp.name.toLowerCase();
	        	if(!name.endWith(".jpg") 
	        			&& !name.endWith(".png")
	        			&& !name.endWith(".jpeg")){
	        		$.messager.popup("只能上传图片文件");
	        		return ;
	        	}
	        	if(data.files[0].size > 524288){
	        		$.messager.popup("文件最大为500k");
	        		return ;
	        	}
	        	data.submit();
	        },
	        done: function (e, returnData) {
				var data = returnData.result;
	        	if(data.success){
					var url = "${ctx}/my/avatar/drew?time="+new Date().getTime();
					loadImage(url, function callBack(){
						var image = this;
						$(".img-container").html(image);
						var $image = $(".img-container img");
						$image.cropper({
						    aspectRatio: 1/1,
						    data: {
						        x: 480,
						        y: 60,
						        width: 640,
						        height: 360
						    },
						    preview: ".img-preview",
						    done: function(data) {
						    	$("#x").val(data.x);
						    	$("#y").val(data.y);
						    	$("#w").val(data.width);
						    	$("#h").val(data.height);
						    }
						});
						
						relativePath = url;
					});
				}else{
					$.messager.popup(data.result);
				}
	        }
	    });
	});
	var relativePath ;
	function loadImage(url, callback) {
	    var img = new Image(); //创建一个Image对象，实现图片的预下载
	    img.src = url;
	    if(img.complete) { // 如果图片已经存在于浏览器缓存，直接调用回调函数
	        callback.call(img);
	        return ; // 直接返回，不用再处理onload事件
	    }
	    img.onload = function () { //图片下载完毕时异步调用callback函数。
	        callback.call(img);//将回调函数的this替换为Image对象
	    };
	};
	
	function crop(){
		var w = $("#w").val();
		var h = $("#h").val();
		var x = $("#x").val();
		var y = $("#y").val();
		if(isNaN(w) || isNaN(h) || isNaN(x) || isNaN(y)){
			$.messager.popup("请先上传图片后裁剪");
			return ;
		}
		if(h == 0 || w == 0){
			$.messager.popup("请先上传图片后裁剪");
			return ;
		}
		var param = {"x":x,"y":y,"w":w,"h":h};
		
		$.ajax({
			url : "${ctx}/my/avatar/confirm",
			data : JSON.stringify(param),
			type : 'post',
			dataType : 'json',
			contentType : 'application/json',
			success : function(data){
				if(data.success){
					$.messager.popup("保存头像成功");
				}else{
					$.messager.popup(data.result);
				}
			}
		});
	}
	</script>
</body>
</html>

