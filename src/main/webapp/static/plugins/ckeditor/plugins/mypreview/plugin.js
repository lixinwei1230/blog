CKEDITOR.plugins.add('mypreview', {
	init: function( editor ) {
		var me = this;
		editor.ui.addButton( 'Mypreview', {
			label: '超链接',
			command: 'mypreview',
			toolbar: 'insert',
			icon:this.path+"icons/preview.png"
		});
		editor.addCommand( 'mypreview', {
			exec: function() {
				if($("#ckeditor-preview-form").length == 0){
					$(me.model()).appendTo("body");
				}
				$("#ckeditor-preview-content").val(editor.getData());
				$("#ckeditor-preview-form").submit();
			}
		});
	},
	model:function(){
		var html = '';
		var token = $("meta[name='_csrf']").attr("content");
		html += '<form style="display: none" action="'+contextPath+'/my/blog/preview" method="post" id="ckeditor-preview-form" target="_blank">';
		html += '<input type="hidden" value="" id="ckeditor-preview-content" name="content"/>';
		html += '<input type="hidden" name="_csrf" value="'+token+'" />';
		html += '</form>';
		return html;
	}
});


