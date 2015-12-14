CKEDITOR.plugins.add('mylink', {
	init: function( editor ) {
		var me = this;
		editor.ui.addButton( 'Mylink', {
			label: '超链接',
			command: 'mylink',
			toolbar: 'insert',
			icon:this.path+"icons/link.png"
		});
		editor.addCommand( 'mylink', {
			exec: function() {
				var text = editor.getSelection().getSelectedText();
				if(!(!text || text == null || text.length == 0)){
					editor.insertHtml('<a target="_blank" href="'+text+'">'+text+'</a>');
				}else{
					if($("#ckeditor_linkModal").length == 0){
						$(me.model()).appendTo("body").modal("show");
						var btn = $("#ckeditor_linkModal").find(".btn-primary");
						btn.click(function(e){
							var url = $($("#ckeditor_linkModal").find("input[type='text']")[0]).val();
							if($.trim(url) == ""){
								$.messager.popup("超链接地址不能为空");
								return ;
							}
							var target = $($("#ckeditor_linkModal").find("select")[0]).val();
							var alt = $($("#ckeditor_linkModal").find("input[type='text']")[1]).val();
							var title = $($("#ckeditor_linkModal").find("input[type='text']")[2]).val();
							var wrap = $("#ckeditor_linkModal").find("textarea").val();
							var targetAtt = target == "" ? "" : 'target="'+target+'"';
							var altAtt = $.trim(alt) == "" ? "" : 'alt="'+alt+'"';
							var titleAtt = $.trim(title) == "" ? "" : 'title="'+title+'"';
							var wrapContent = $.trim(wrap) == "" ? url : wrap;
							var html = '<a href="'+url+'" '+altAtt+' '+titleAtt+'  '+targetAtt+'>'+wrapContent+'</a>';
							editor.insertHtml(html);
							$("#ckeditor_linkModal").modal("hide");
						});
					}else{
						$("#ckeditor_linkModal").modal("show");
						$("#ckeditor_linkModal").find("input").val("");
						$("#ckeditor_linkModal").find("textarea").val("");
					}
				}
			}
		});
	},
	model:function(){
		var div_dialogHtml = '<div class="modal fade" id="ckeditor_linkModal" tabindex="-1" role="dialog"';
		div_dialogHtml += '	aria-labelledby="myModalLabel" aria-hidden="true"';
		div_dialogHtml += '	data-backdrop="static">';
		div_dialogHtml += '	<div class="modal-dialog modal-md">';
		div_dialogHtml += '		<div class="modal-content">';
		div_dialogHtml += '			<div class="modal-header">';
		div_dialogHtml += '				<button type="button" class="close" data-dismiss="modal">';
		div_dialogHtml += '					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
		div_dialogHtml += '				</button>';
		div_dialogHtml += '				<h4 class="modal-title" id="myModalLabel">超链接</h4>';
		div_dialogHtml += '			</div>';
		div_dialogHtml += '			<div class="modal-body">';
		div_dialogHtml += '				<div class="form-group">';
		div_dialogHtml += '					<label>链接指向</label> ';
		div_dialogHtml += '					<select class="form-control">';
		div_dialogHtml += '						<option value="_blank" selected="selected">_blank</option>';
		div_dialogHtml += '						<option value="">当前页</option>';
		div_dialogHtml += '					</select>';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="form-group">';
		div_dialogHtml += '					<label>目标地址</label> ';
		div_dialogHtml += '					<input type="text" class="form-control"/>';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="form-group">';
		div_dialogHtml += '					<label>alt</label> ';
		div_dialogHtml += '					<input type="text" class="form-control"/>';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="form-group">';
		div_dialogHtml += '					<label>title</label> ';
		div_dialogHtml += '					<input type="text" class="form-control"/>';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="form-group">';
		div_dialogHtml += '					<label>包裹内容</label> ';
		div_dialogHtml += '					<textarea style="height:100px" class="form-control"></textarea>';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '			</div>';
		div_dialogHtml += '			<div class="modal-footer">';
		div_dialogHtml += '				<button type="button" class="btn btn-primary">创建</button>';
		div_dialogHtml += '				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
		div_dialogHtml += '			</div>';
		div_dialogHtml += '		</div>';
		div_dialogHtml += '	</div>';
		div_dialogHtml += '</div>';
		return div_dialogHtml;
	}
});

