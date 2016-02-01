CKEDITOR.plugins.add('mycode', {
	init: function( editor ) {
		var me = this;
		editor.ui.addButton( 'Mycode', {
			label: '代码',
			command: 'mycode',
			toolbar: 'insert',
			icon:this.path+"icons/code.png"
		});
		editor.addCommand( 'mycode', {
			exec: function() {
				var text = editor.getSelection().getSelectedText();
				if(!(!text || text == null || text.length == 0)){
					editor.insertHtml("<code>"+text+"</code>");
				}else{
					if($("#ckeditor-codeModal").length == 0){
						$(me.model()).appendTo("body").modal("show");
					}else{
						$("#ckeditor-code-input").val("");
						$("#ckeditor-code-container").html("");
						$("#ckeditor-code-select").val("please choose");
						$("#ckeditor-codeModal").modal("show");
					}
					var btn = $("#ckeditor-code-btn");
					var typer = $("#ckeditor-code-select");
					typer.off("change");
					var container = $("#ckeditor-code-container");
					typer.on("change",function(){
						var type = $(this).val();
						if(type == "please choose"){
							container.html("");
						}
						if(type == "codeTag"){
							container.html("<input type='text' class='form-control' id='ckeditor-code-input'/>");
						}
						if(type == "code"){
							var languages = me.languages;
							var html = "<select class='form-control' id='ckeditor-code-lan' style='margin-bottom:20px'>";
							for(var i=0;i<languages.length;i++){
								var lan = languages[i];
								html += "<option value='"+lan+"'>"+lan+"</option>";
							}
							html += "</select>";
							container.html(html+"<textarea class='form-control' id='ckeditor-code-textarea' style='height:300px'></textarea>");
						}
					});
					btn.off("click");
					btn.on("click",function(){
						var type = typer.val();
						if(type == "please choose"){
							$.messager.popup("请选择类别");
							return ;
						}
						if(type == "codeTag"){
							var code = $("#ckeditor-code-input").val();
							if(code == ""){
								$.messager.popup("请输入内容");
								return ;
							}
							editor.insertHtml("<code>"+code+"</code>");
						}
						if(type == "code"){
							var code = $("#ckeditor-code-textarea").val();
							if(code == ""){
								$.messager.popup("请输入内容");
								return ;
							}
							var lan = $("#ckeditor-code-lan").val();
							editor.insertHtml("<pre class='pre-scrollable'>");
							editor.insertHtml("<code class='"+lan+"'>");
							editor.insertText(""+code+"");
							editor.insertHtml("</code>");
							editor.insertHtml("</pre>")
						}
						$("#ckeditor-codeModal").modal("hide");
					});
				}
			}
		});
	},
	languages:["Apache","Bash","C#","C++","CSS","CoffeeScript","dts","Diff","HTML,XML","HTTP","ini","JSON","Java","JavaScript","MakeFile","Markdown","Nginx","Objective-C","PHP","Perl","Python","Ruby","SQL"],
	model:function(){
		var code_dialogHtml = '<div class="modal fade" id="ckeditor-codeModal" tabindex="-1" role="dialog"';
		code_dialogHtml += '	aria-labelledby="myModalLabel" aria-hidden="true"';
		code_dialogHtml += '	data-backdrop="static">';
		code_dialogHtml += '	<div class="modal-dialog modal-md">';
		code_dialogHtml += '		<div class="modal-content">';
		code_dialogHtml += '			<div class="modal-header">';
		code_dialogHtml += '				<button type="button" class="close" data-dismiss="modal">';
		code_dialogHtml += '					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
		code_dialogHtml += '				</button>';
		code_dialogHtml += '				<h4 class="modal-title" id="myModalLabel">代码</h4>';
		code_dialogHtml += '			</div>';
		code_dialogHtml += '			<div class="modal-body">';
		code_dialogHtml += '         		<select id="ckeditor-code-select" class="form-control" ><option value="please choose">请选择</option><option value="codeTag">code标签</option><option value="code">代码</option></select>';
		code_dialogHtml += '				<div id="ckeditor-code-container" style="margin-top:10px"></div>';
		code_dialogHtml += '			</div>';
		code_dialogHtml += '			<div class="modal-footer">';
		code_dialogHtml += '				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
		code_dialogHtml += '				<button type="button" class="btn btn-default" id="ckeditor-code-btn">确认</button>';
		code_dialogHtml += '			</div>';
		code_dialogHtml += '		</div>';
		code_dialogHtml += '	</div>';
		code_dialogHtml += '</div>';
		return code_dialogHtml;
	}
});

