CKEDITOR.plugins.add('mydiv', {
	init: function( editor ) {
		var me = this;
		editor.ui.addButton( 'Mydiv', {
			label: '警告框',
			command: 'mydiv',
			toolbar: 'insert',
			icon:this.path+"icons/alert.png"
		});
		editor.addCommand( 'mydiv', {
			exec: function() {
				if($("#ckeditor_divModal").length == 0){
					$(me.model()).appendTo("body").modal("show");
				}else{
					$("#ckeditor_divModal").modal("show");
				}
				$("#ckeditor_divModal div.alert").off('click').on('click',function(){
					var me = $(this);
					editor.insertHtml(me.prop("outerHTML"));
					$("#ckeditor_divModal").modal("hide");
				});
			}
		});
	},
	model:function(){
		var div_dialogHtml = '<div class="modal fade" id="ckeditor_divModal" tabindex="-1" role="dialog"';
		div_dialogHtml += '	aria-labelledby="myModalLabel" aria-hidden="true"';
		div_dialogHtml += '	data-backdrop="static">';
		div_dialogHtml += '	<div class="modal-dialog modal-md">';
		div_dialogHtml += '		<div class="modal-content">';
		div_dialogHtml += '			<div class="modal-header">';
		div_dialogHtml += '				<button type="button" class="close" data-dismiss="modal">';
		div_dialogHtml += '					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
		div_dialogHtml += '				</button>';
		div_dialogHtml += '				<h4 class="modal-title" id="myModalLabel">警告框</h4>';
		div_dialogHtml += '			</div>';
		div_dialogHtml += '			<div class="modal-body">';
		div_dialogHtml += '				<div class="row"><div class="col-lg-12 col-md-12 "><p class="text-info">点击警告框即可插入</p>';
		div_dialogHtml += '				<div class="alert alert-info" >&nbsp; ';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="alert alert-warning" > &nbsp;';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="alert alert-danger" > &nbsp;';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				<div class="alert alert-success" > &nbsp;';
		div_dialogHtml += '				</div>';
		div_dialogHtml += '				</div></div>';
		div_dialogHtml += '			</div>';
		div_dialogHtml += '			<div class="modal-footer">';
		div_dialogHtml += '				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
		div_dialogHtml += '			</div>';
		div_dialogHtml += '		</div>';
		div_dialogHtml += '	</div>';
		div_dialogHtml += '</div>';
		return div_dialogHtml;
	}
});

