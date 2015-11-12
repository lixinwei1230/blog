var userWidget = {handler:function handleConfig(lwId,config,widgetId,btn){
	var html = '<div class="row" style="margin-bottom: 15px">'
	html += '<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12" style="margin-bottom:10px">';
	html += '<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4"><label>是否隐藏</label></div>';
	html += '<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">';
	html += '<select class="form-control" name="hidden">';
	if(config.hidden){
		html += '<option value="true" selected=selected>当前:是</option>';
	}else{
		html += '<option value="false" selected=selected>当前:否</option>';
	}
	html += '<option value="true" >是</option><option value="false" >否</option></select>';			
	html += '</div></div></div>';
	btn.unbind("click");
	btn.bind("click",function(){
		var data = {};
		data.id = config.id;
		data.widget = {"id":lwId};
		data.hidden=$("#configForm").find("select[name='hidden']").val();
		btn.button("loading");
		post(root+"/my/page/config/update?widgetSign=default",data,function callBack(data){
			if(data.success){
				$.messager.popup("配置成功");
				window.location.reload();
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
	    	 btn.button("reset");
		});
	});
	$("#configForm").html(html);	
	$("#configModal").modal("show");
}}

var blogWidget = {id:"1",handler:function handleConfig(lwId,config,widgetId,btn){
	var html = '<div class="row" style="margin-bottom: 15px">';
	html += '<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12" style="margin-bottom:10px">';
	html += '<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4"><label>是否隐藏</label></div>';
	html += '<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">';
	html += '<select class="form-control" name="hidden">';
	if(config.hidden){
		html += '<option value="true" selected=selected>当前:是</option>';
	}else{
		html += '<option value="false" selected=selected>当前:否</option>';
	}
	html += '<option value="true" >是</option><option value="false" >否</option></select>';			
	html += '</div></div>';
	html += '<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">';
	html += '<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4"><label>展现模式</label></div>';
	html += '<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">';
	html += '<select class="form-control" name="mode">';
	if(config.mode == 'PREVIEW'){
		html += '<option value="PREVIEW" selected=selected>当前:预览模式</option>';
	}
	if(config.mode == 'LIST'){
		html += '<option value="LIST" selected=selected>当前:列表模式</option>';
	}
	html += '<option value="PREVIEW">预览模式</option><option value="LIST">列表模式</option></select>';			
	html += '</div></div></div>';			
	$("#configForm").html(html);	
	btn.unbind("click");
	btn.bind("click",function(){
		var data = {};
		data.id = config.id;
		data.mode = $("#configForm").find("select[name='mode']").val();
		data.widget = {"id":lwId};
		data.hidden=$("#configForm").find("select[name='hidden']").val();
		btn.button("loading");
		post(root+"/my/page/config/update?widgetSign=blog",data,function callBack(data){
			if(data.success){
				$.messager.popup("配置成功");
				window.location.reload();
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
	    	 btn.button("reset");
		});
	});
	$("#configModal").modal("show");
}}

var widgets = [blogWidget,{id:2,handler:userWidget.handler},{id:3,handler:userWidget.handler},{id:4,handler:userWidget.handler}];

function handleWidget(lwId,config,widgetId,btn){
	for(var i=0;i<widgets.length;i++){
		var widget = widgets[i];
		if(widget.id == widgetId){
			widget.handler(lwId, config, widgetId, btn);
			break;
		}
	}
}

function handleWidgets(type,lwId,config,widgetId,btn){
	if(type == "USER"){
		userWidget.handler(lwId, config, widgetId, btn)
	}
	if(type == "SYSTEM"){
		handleWidget(lwId,config,widgetId,btn);
	}
}