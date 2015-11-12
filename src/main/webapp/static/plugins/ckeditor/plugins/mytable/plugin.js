CKEDITOR.plugins.add('mytable', {
	init: function( editor ) {
		var me = this;
		editor.ui.addButton( 'Mytable', {
			label: '增加表格',
			command: 'mytable',
			toolbar: 'insert',
			icon:this.path+"icons/table.png"
		});
		editor.addCommand( 'mytable', {
			exec: function() {
				if($("#ckeditor_tableModal").length == 0){
					$(me.model()).appendTo("body").modal("show");
				}else{
					$("#ckeditor-table-template").find("td").each(function(){
						var _this = $(this);
						_this.css("background-color","#d9edf7");
					});
					$('input[name="ckeditor-table-class"]').prop("checked",false);
					$("#ckeditor_tableModal").modal("show");
				}
				$("#ckeditor-table-template").find("td").each(function(){
					var _this = $(this);
					_this.off("click");
					_this.on("click",function(){
						me.writeTable(_this,editor);
					});
					_this.hover(function(){
						var cell = _this[0].cellIndex;
						var row = _this[0].parentNode.rowIndex;
						$("#ckeditor-table-template").find("td").each(function(){
							var _cell = $(this)[0].cellIndex;
							var _row = $(this)[0].parentNode.rowIndex;
							if(_cell <= cell && _row <= row){
								$(this).css("background-color","#f2dede");
							}if(_cell > cell || _row > row){
								$(this).css("background-color","#d9edf7");
							}
						});
					});
				});
			}
		});
	},
	model : function(){
		var me = this;
		var table_dialogHtml = '<div class="modal fade" id="ckeditor_tableModal" tabindex="-1" role="dialog"';
		table_dialogHtml += '	aria-labelledby="myModalLabel" aria-hidden="true"';
		table_dialogHtml += '	data-backdrop="static">';
		table_dialogHtml += '	<div class="modal-dialog modal-md">';
		table_dialogHtml += '		<div class="modal-content">';
		table_dialogHtml += '			<div class="modal-header">';
		table_dialogHtml += '				<button type="button" class="close" data-dismiss="modal">';
		table_dialogHtml += '					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
		table_dialogHtml += '				</button>';
		table_dialogHtml += '				<h4 class="modal-title" id="myModalLabel">增加表格</h4>';
		table_dialogHtml += '			</div>';
		table_dialogHtml += '			<div class="modal-body">';
		table_dialogHtml += '				<div class="row">';
		table_dialogHtml += '					<div class="col-lg-6 col-sm-6 col-md-12 col-xs-12">';
		table_dialogHtml += 						me.generateTable(5,5);
		table_dialogHtml += '					</div>';
		table_dialogHtml += '					<div class="col-lg-6 col-sm-6 col-md-12 col-xs-12">';
		table_dialogHtml += '                   	<div>表格样式</div>';
		table_dialogHtml += '						<input type="checkbox" name="ckeditor-table-class" value="table"/>&nbsp;&nbsp;table<br/>';
		table_dialogHtml += '						<input type="checkbox" name="ckeditor-table-class" value="table-striped"/>&nbsp;&nbsp;table-striped<br/>';
		table_dialogHtml += '						<input type="checkbox" name="ckeditor-table-class" value="table-bordered"/>&nbsp;&nbsp;table-bordered<br/>';
		table_dialogHtml += '						<input type="checkbox" name="ckeditor-table-class" value="table-condensed"/>&nbsp;&nbsp;table-condensed<br/>';
		table_dialogHtml += '						<input type="checkbox" name="ckeditor-table-class" value="table-hover"/>&nbsp;&nbsp;table-hover<br/>';
		table_dialogHtml += '					</div>';
		table_dialogHtml += '				</div>';
		table_dialogHtml += '			</div>';
		table_dialogHtml += '			<div class="modal-footer">';
		table_dialogHtml += '				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
		table_dialogHtml += '			</div>';
		table_dialogHtml += '		</div>';
		table_dialogHtml += '	</div>';
		table_dialogHtml += '</div>';
		return table_dialogHtml;
	},
	writeTable : function (o,editor){
		var classes  = "";
		$('input[name="ckeditor-table-class"]:checked').each(function(){ 
			classes += $(this).val()+" ";
		}); 
		var col = Number(o[0].cellIndex)+1;
		var row = Number(o[0].parentNode.rowIndex)+1;
		var html = "<div class='table table-responsive'><table class='"+classes+"'>";
		html += "<thead></thead><tbody>";
		for(var i=0;i<row;i++){
			html += "<tr>";
			for(var j=0;j<col;j++){
				html += "<td>&nbsp;</td>";
			}
			html += "</tr>";
		}
		html += "</tbody><tfoot></tfoot>";
		html += "</table></div>";
		editor.insertHtml(html);
		$("#ckeditor_tableModal").modal("hide");
	},
	generateTable:function(row,col){
		var html = "<div class='table-responsive'><table cellspacing='1' cellpadding='1' border='1' id='ckeditor-table-template'>";
		html += "<thead></thead><tbody>";
		for(var i=0;i<row;i++){
			html += "<tr>";
			for(var j=0;j<col;j++){
				html += "<td style='background:#d9edf7;width:50px;height:50px;border:1px solid #ccc'>&nbsp;</td>";
			}
			html += "</tr>";
		}
		html += "</tbody><tfoot></tfoot>";
		html += "</table></div>";
		return html ;
	}

});



