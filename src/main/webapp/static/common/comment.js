var comment_container;
var scopeId;
(function ($) {
	
	$.fn.comment = function (options) {
		var opts = $.extend({}, $.fn.comment.defaults, options);    
		
		scopeId = (typeof opts.scopeId === 'function') ? opts.scopeId.call() : opts.scopeId;
		var editor_container = $("#"+opts.editor_container);
		comment_container = $("#"+opts.comment_container);
		return this.each(function(){
			editor_container.html(buildEditorHtml());
			$("#btn-load-editor").bind("click",function(){
				CKEDITOR.replace("comment-editor");
				CKEDITOR.config.enterMode=CKEDITOR.ENTER_BR;
				CKEDITOR.config.height=200;
				CKEDITOR.config.allowedContent = true;
				CKEDITOR.config.extraPlugins = 'codemirror';
				CKEDITOR.config.shiftEnterMode=CKEDITOR.ENTER_P;
				CKEDITOR.config.toolbar = [
				    [ 'Source']
				];
				$(this).hide();
				$("#btn-remove-editor").show();
			});
			$("#btn-remove-editor").bind("click",function(){
				var editor = CKEDITOR.instances["comment-editor"];
				if(editor){
					editor.destroy();
				}
				$(this).hide();
				$("#btn-load-editor").show();
			});
			var btn = $("#send-comment");
			btn.bind("click",function(){
				var data = {};
				data.title = $("#comment-title").val();
				data.content = getContent();
				data.blog={"id":scopeId};
				data.isAnonymous=$("#comment-anonymous").is(':checked');
				btn.button("loading");
				post(contextPath+"/my/comment/add",data,function(data){
					if(data.success){
						var html = getCommentHtml(data.result);
						$("#comment-container").prepend(html);
						clear();
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
	                if(request.status == "403"){
	                	$.messager.popup("没有权限进行这项操作");
	                };
	             },function(){
	            	 btn.button("reset");
	             });
			});
			writeComment(1);
		});
    };
    
    $.fn.comment.defaults = {    
   		scopeId: null,    
   		editor_container:null,
   		comment_container:null
   	};
    
	function getContent(){
		var editor = CKEDITOR.instances["comment-editor"];
		if(editor){
			return editor.getData();
		}
		return $("#comment-editor").val();
	}
    
    function buildEditorHtml(){
    	var html = '';
    	html += '<div class="form-group">';
    	html += '<label>标题</label> <input type="text" class="form-control" id="comment-title">';
    	html += '</div>';
    	html += '<div class="form-group">';
    	html += '<label>评论</label> <textarea id="comment-editor" class="form-control" style="height: 250px"></textarea>';
    	html += '</div>';
    	html += '<div class="form-group">';
    	html += '<label>匿名</label> <input type="checkbox" name="isAnonymous" id="comment-anonymous" value="true">';
    	html += '</div>';
    	html += '<div style="text-align: right; margin-top: 10px">';
    	html += '<button class="btn btn-primary" id="btn-remove-editor" style="margin-right:10px;display: none">移除编辑器</button>';
    	html += '<button class="btn btn-primary" style="margin-right:10px" id="btn-load-editor">加载编辑器</button>';
    	html += '<button class="btn btn-primary" id="send-comment" data-loading-text="处理中">提交</button>';
    	html += '</div>';
    	return html ;
    }
    
    function clear(){
    	$("#comment-title").val("");
    	var editor = CKEDITOR.instances["comment-editor"];
		if(editor){
			return editor.setData("");
		}else{
			$("#comment-editor").val("");
		}
		$("#comment-anonymous").prop("checked",false);
    }
    
})(jQuery);

function writeComment(currentPage){
	$.ajax({
		type: "GET",
		url: contextPath + "/blog/comment/list/"+currentPage,
		data:{"blog.id":scopeId},
		dataType: "json",
		success: function(data){
			if(data.success){
				var page = data.result;
				var comments = page.datas;
				var html = '';
				if(comments.length > 0){
					for(var i=0;i<comments.length;i++){
						html += getCommentHtml(comments[i]);
					}
				}
				if(page.totalPage > 1){
					 html += "<ul class='pagination'>";
					 for(var dp=page.listbegin;dp<=page.listend-1;dp++){
						 if(dp != page.currentPage){
							html += "<li class='comment-page-link'><a href='###' onclick='writeComment(\""+dp+"\")' >"+dp+"</a></li>";
						 }
					 }
					 html += "</ul>";
			 	}
				comment_container.html(html);
			}else{
				$.messager.popup(data.result);
			}
         },
         error:function (request) {
        	 comment_container.html("");
         }
	});
}

function writeReply(currentPage,pId,o){
	$.ajax({
		type: "GET",
		url: contextPath + "/comment/list/"+currentPage,
		data:{"blog.id":scopeId,"parent.id":pId},
		dataType: "json",
		success: function(data){
			if(data.success){
				var page = data.result;
				var replys = page.datas;
				var html = '';
				if(replys.length > 0){
					for(var i=0;i<replys.length;i++){
						html += getReplyHtml(replys[i]);
					}
				}
				if(page.totalPage > 1){
					 html += "<ul class='pagination'>";
					 for(var dp=page.listbegin;dp<=page.listend-1;dp++){
						 if(dp != page.currentPage){
							 html += "<li><a href='###' onclick='writeReply(\""+dp+"\",\""+pId+"\",$(\"#"+o.attr("id")+"\"))' >"+dp+"</a></li>";
						 }
					 }
					 html += "</ul>";
			 	}
				o.html(html);
			}else{
				$.messager.popup(data.result);
			}
         },
         error:function (request) {
         }
	});
}


function getCommentHtml(comment){
	var html = '<div class="media">';
	html += '<div class="media-left">';
	if(comment.isAnonymous){
		html += '<img class="media-object" src="'+contextPath+'/static/imgs/guest_64.png" style="width:64px;height:64px"/>';
	}else{
		var avatar = "";
		if(comment.user.avatar){
			avatar = '<img class="media-object" src="'+getResizeUrl(comment.user.avatar.url,64)+'" onerror="javascript:this.src=\''+contextPath+'/static/imgs/guest_64.png\'" style="width:64px;height:64px"/>';
		}else{
			avatar = '<img class="media-object" src="'+contextPath+'/static/imgs/guest_64.png" style="width:64px;height:64px"/>';
		}
		html += '<a href="'+getUrlByUser(comment.user, false)+'/index">'+avatar+'</a>';
	}
	html += '</div>';
	html += '<div class="media-body">';
	var name = comment.isAnonymous ? '匿名用户' : '<a href="'+getUrlByUser(comment.user, false)+'/index">'+comment.user.nickname+'</a>';
	html += '<h4 class="media-heading" id="media-heading"><strong>'+name+'</strong></h4>';
	html += '<h5><strong>'+comment.title+'</strong></h5>';
	html += comment.content;
	html += '<h6>'+new Date(comment.commentDate).pattern("yyyy-MM-dd HH:mm:ss")+'<a href="###" onclick="showReplyEditor('+comment.id+','+comment.id+')"><span style="margin-left:10px" class="glyphicon glyphicon-comment" aria-hidden="true"></span></a><a href="###" onclick="deleteComment('+comment.id+',true,$(this))"><span style="margin-left:10px" class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></h6>';
	html += '<div id="reply-container-'+comment.id+'">';
	var page = comment.page;
	if(page != null){
		var replys = page.datas;
		if(replys.length > 0){
			for(var i=0;i<replys.length;i++){
				html += getReplyHtml(replys[i]);
			}
		}
		if(page.totalPage > 1){
			 html += "<ul class='pagination'>";
			 for(var dp=page.listbegin;dp<=page.listend-1;dp++){
				 if(dp != page.currentPage){
					html += "<li><a href='###' onclick='writeReply(\""+dp+"\",\""+comment.id+"\",$(\"#reply-container-"+comment.id+"\"))' >"+dp+"</a></li>";
				 }
			 }
			 html += "</ul>";
	 	}
	}
	html += '</div>';
	html += '</div>';
	html += '</div>';
	return html ;
}

function getReplyHtml(reply){
	var html = '<div class="media">';
	html += '<div class="media-left">';
	if(reply.isAnonymous){
		html += '<img class="media-object" src="'+contextPath+'/static/imgs/guest_64.png" style="width:64px;height:64px"/>';
	}else{
		var avatar = "";
		if(reply.user.avatar){
			avatar = '<img class="media-object" src="'+getResizeUrl(reply.user.avatar.url,64)+'" onerror="javascript:this.src=\''+contextPath+'/static/imgs/guest_64.png\'" style="width:64px;height:64px"/>';
		}else{
			avatar = '<img class="media-object" src="'+contextPath+'/static/imgs/guest_64.png" style="width:64px;height:64px"/>';
		}
		html += '<a href="'+getUrlByUser(reply.user, false)+'/index">'+avatar+'</a>';
	}
	html += '</div>';
	html += '<div class="media-body">';
	var name = reply.isAnonymous ? '匿名用户' : '<a href="'+getUrlByUser(reply.user, false)+'/index">'+reply.user.nickname+'</a>';
	var replyTo = reply.replyTo == null ? '匿名用户' : '<a href="'+getUrlByUser(reply.replyTo, false)+'/index">'+reply.replyTo.nickname+'</a>';
	html += '<div class="media-heading" id="media-heading"><strong>'+name+'</strong>@<strong>'+replyTo+'</strong></div>';
	html += '<h5><strong>'+reply.title+'</strong></h5>';
	html += reply.content;
	html += '<h6>'+new Date(reply.commentDate).pattern("yyyy-MM-dd HH:mm:ss")+'<a href="###" onclick="showReplyEditor('+reply.id+','+reply.parent.id+')"><span style="margin-left:10px" class="glyphicon glyphicon-comment" aria-hidden="true"></span></a><a href="###" onclick="deleteComment('+reply.id+',false,$(this))"><span style="margin-left:10px" class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></h6>';
	html += '</div>';
	html += '</div>';
	return html ;
}

function deleteComment(id,isParent,o){
	var message = isParent ? "确定删除该评论吗？这将删除该条评论及该评论所有的回复" : "确定删除该回复吗";
	$.messager.confirm("删除", message, function() { 
		post(contextPath+"/my/comment/delete/"+id,{},function(data){
			if(data.success){
				o.parent().parent().parent().remove();
			}else{
				$.messager.popup(data.result);
			}
		},function (request) {  
            if(request.status == "403"){
            	$.messager.popup("没有权限进行这项操作");
            };
         },null);
	});
}

function showReplyEditor(replyId,parentId){
	
	if($("#replyModal").length == 0){
		var html = '';
		html += '<div class="modal fade" id="replyModal" tabindex="-1" role="dialog"';
		html += 'aria-labelledby="myModalLabel" aria-hidden="true"';
		html += 'data-backdrop="static">';
		html += '<div class="modal-dialog modal-lg">';
		html += '<div class="modal-content">';
		html += '<div class="form-group">';
		html += '<div class="modal-header">';
		html += '<button type="button" class="close" data-dismiss="modal">';
		html += '<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
		html += '</button>';
		html += '<h4 class="modal-title">';
		html += '回复评论';
		html += '</h4>';
		html += '</div>';
		html += '<div class="modal-body">';
		
		html += '<div class="form-group">';
		html += '<label>标题</label> <input type="text" class="form-control" id="reply-title">';
		html += '</div>';
		html += '<div class="form-group">';
		html += '<label>评论</label> <textarea id="reply-editor" class="form-control" style="height: 250px"></textarea>';
		html += '</div>';
		html += '<div class="form-group">';
		html += '<label>匿名</label> <input type="checkbox" name="isAnonymous" id="reply-anonymous" value="true">';
		html += '</div>';
		html += '</div>';
		html += '<div class="modal-footer">';
    	html += '<button class="btn btn-primary" id="reply-btn-remove-editor" style="display: none">移除编辑器</button>';
    	html += '<button class="btn btn-primary" id="reply-btn-load-editor">加载编辑器</button>';
		html += '<button class="btn btn-primary" id="send-reply" data-loading-text="处理中">提交</button>';
		html += '<button type="button" class="btn btn-default" data-dismiss="modal">';
		html += '关闭';
		html += '</button>';
		html += '</div>';
		html += '</div>';
		html += '</div>';
		html += '</div>';
		$(html).appendTo($('body')).modal('show');
	}else{
		$("#reply-title").val("");
		var editor = CKEDITOR.instances["reply-editor"];
		if(editor){
			editor.setData("");
		}else{
			$("#reply-editor").val("");
		}
		$("#reply-anonymous").prop("checked",false);
		$("#replyModal").modal("show");
	}
	var btn = $("#send-reply");
	btn.unbind("click");
	btn.bind("click",function(){
		var data = {};
		data.title = $("#reply-title").val();
		var editor = CKEDITOR.instances["reply-editor"];
		if(editor){
			data.content = editor.getData();
		}else{
			data.content = $("#reply-editor").val();
		}
		data.blog = {"id":scopeId};
		data.isAnonymous=$("#reply-anonymous").is(':checked');
		var parent = {};
		parent.id = parentId;
		var reply = {};
		reply.id = replyId;
		data.parent = parent;
		data.reply = reply;
		btn.button("loading...");
		post(contextPath+"/my/comment/add",data,function(data){
			if(data.success){
				$("#reply-container-"+parentId).append(getReplyHtml(data.result));
				$("#replyModal").modal("hide");
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
	        if(request.status == "403"){
	        	$.messager.popup("没有权限进行这项操作");
	        };
	     },function(){
	    	btn.button("reset");
	     });
	});
	$("#reply-btn-load-editor").unbind("click");
	$("#reply-btn-remove-editor").unbind("click");
	$("#reply-btn-load-editor").bind("click",function(){
		CKEDITOR.replace("reply-editor");
		CKEDITOR.config.enterMode=CKEDITOR.ENTER_BR;
		CKEDITOR.config.height=200;
		CKEDITOR.config.allowedContent = true;
		CKEDITOR.config.extraPlugins = 'codemirror';
		CKEDITOR.config.shiftEnterMode=CKEDITOR.ENTER_P;
		CKEDITOR.config.toolbar = [
		    [ 'Source']
		];
		$(this).hide();
		$("#reply-btn-remove-editor").show();
	});
	$("#reply-btn-remove-editor").bind("click",function(){
		var editor = CKEDITOR.instances["reply-editor"];
		if(editor){
			editor.destroy();
		}
		$(this).hide();
		$("#reply-btn-load-editor").show();
	});
}

