CKEDITOR.plugins.add('myfile', {
	files : [],
	editor:null,
	init: function( editor ) {
		var me = this;
		me.editor = editor;
		editor.ui.addButton( 'Myfile', {
			label: '文件管理',
			command: 'myfile',
			toolbar: 'insert',
			icon:this.path+"icons/file.png"
		});
		editor.addCommand( 'myfile', {
			exec: function() {
				if($("#ckeditor_myFileModal").length == 0){
					$(me.model()).appendTo("body").modal("show");
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
						me.writePage(1);
					});
				}else{
					$("#ckeditor_myFileModal").modal("show");
				}
				me.writePage(1);
			}
		});
	},
	model:function(){
		var me = this;
		var html = ''
		html+='﻿<div class="modal fade" id="ckeditor_myFileModal" tabindex="-1" role="dialog"'
		html+='	aria-labelledby="myModalLabel" aria-hidden="true"'
		html+='	data-backdrop="static">'
		html+='	<div class="modal-dialog  modal-lg">'
		html+='		<div class="modal-content">'
		html+='			<div class="modal-header">'
		html+='				<button type="button" class="close" data-dismiss="modal">'
		html+='					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>'
		html+='				</button>'
		html+='				<h4 class="modal-title" id="myModalLabel">文件管理</h4>'
		html+='			</div>'
		html+='			<div class="modal-body">'
		html+='				<ul class="nav nav-tabs" role="tablist">'
		html+='					<li role="presentation" class="active"><a href="#home"'
		html+='						aria-controls="home" role="tab" data-toggle="tab">文件</a></li>'
		html+='					<li role="presentation"><a href="#upload"'
		html+='						aria-controls="upload" role="tab" data-toggle="tab">文件上传</a></li>'
		html+='                  <li><a href="javascript:void(0)" id="ckeditor_file_manager_refresh" >刷新</a></li>'
		html+='				</ul>'
		html+='				<div class="tab-content" style="margin-top: 20px">'
		html+='					<div role="tabpanel" class="tab-pane active" id="home"></div>'
		html+='					<div role="tabpanel" class="tab-pane" id="upload">'
		html+='						<form id="ckeditor_fileupload" class="bs-example form-horizontal"'
		html+='							autocomplete="off"  method="POST" action="'+me.editor.config.uploadUrl+'"'
		html+='							enctype="multipart/form-data">'
		html+='							<div class="row fileupload-buttonbar">'
		html+='								<div class="col-lg-8 col-md-8 col-sm-12 col-xs-12">'
		html+='									<span class="btn btn-success fileinput-button"> <i'
		html+='										class="glyphicon glyphicon-plus"></i> <span>增加文件 </span> <input'
		html+='										type="file" name="files" multiple="">'
		html+='									</span>'
		html+='									<button type="submit" class="btn btn-primary start">'
		html+='										<i class="glyphicon glyphicon-upload"></i> <span>开始上传 </span>'
		html+='									</button>'
		html+='									<button type="reset" class="btn btn-warning cancel">'
		html+='										<i class="glyphicon glyphicon-ban-circle"></i> <span>取消</span>'
		html+='									</button>'
		html+='									<!-- The global file processing state -->'
		html+='									<span class="fileupload-process"></span>'
		html+='								</div>'
		html+='							</div>'
		html+='							<input type="hidden" name="album" />'
		html+='							<div class="row" style="margin-top: 10px">'
		html+='								<div'
		html+='									class="fileupload-progress fade col-lg-12 col-sm-12 col-md-12 col-xs-12">'
		html+='									<!-- The global progress bar -->'
		html+='									<div class="progress progress-striped active"'
		html+='										role="progressbar" aria-valuemin="0" aria-valuemax="100">'
		html+='										<div class="progress-bar progress-bar-success"'
		html+='											style="width: 0%;"></div>'
		html+='									</div>'
		html+='								</div>'
		html+='							</div>'
		html+='							<div class="row">'
		html+='								<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">'
		html+='									<div class="table-responsive">'
		html+='										<table role="presentation" class="table table-striped"'
		html+='											style="text-align: center">'
		html+='											<tbody class="files"></tbody>'
		html+='										</table>'
		html+='									</div>'
		html+='								</div>'
		html+='							</div>'
		html+='						</form>'
		html+='					</div>'
		html+='				</div>'
		html+='			</div>'
		html+='			<div class="modal-footer">'
		html+='				<button type="button" class="btn btn-default" data-dismiss="modal">'
		html+='					关闭'
		html+='				</button>'
		html+='			</div>'
		html+='		</div>'
		html+='	</div>'
		html+='</div>'
		return html;	
	},
	writePage: function(page){
		var me = this;
		$.get(contextPath + "/my/file/list/"+page,{},function callBack(data){
			if(data.success){
				me.files = [];
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
						me.files.push(file);
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
						me.writePage(page);
					});
				}
				$("#home a[class^='insert-']").on('click',function(){
					var _me = $(this);
					var clazz = _me.attr("class");
					var id = clazz.split("-")[1];
					for(var i=0;i< me.files.length;i++){
						var file = me.files[i];
						if(file.id == id){
							if(file.image){
								if(file.cover && file.cover != null){
									 var html = "";
									 html += '<div class="videos">';
									 html += '<a class="video" data-play="'+file.url+'"> <span>&nbsp;</span> <img class="img-responsive" src="'+file.cover.url+'"> </a> ';
									 html += '</div><div class="clearfix">&nbsp;</div>'; 
									me.editor.insertHtml(html);
								}else{
									me.editor.insertHtml('<img class="img-responsive" src="'+getResizeUrl(file.url,600)+'"/>');
								}
							}else{
								me.editor.insertHtml('<a href="'+file.url+'">'+file.originalFilename+'</a>');
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
});

