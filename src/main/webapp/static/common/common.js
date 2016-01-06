String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
};

Date.prototype.pattern=function(fmt) {         
    var o = {         
    "M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds() //毫秒         
    };         
    var week = {         
    "0" : "/u65e5",         
    "1" : "/u4e00",         
    "2" : "/u4e8c",         
    "3" : "/u4e09",         
    "4" : "/u56db",         
    "5" : "/u4e94",         
    "6" : "/u516d"        
    };         
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);         
    }         
    for(var k in o){         
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
};     

function getFirstDay(date){
	var _date = new Date(date.getFullYear(),date.getMonth(),1);
	return _date;
}

function getNextDay(date){
	var _date = new Date(date.getFullYear(),date.getMonth(),date.getDate()+1);
	return _date;
}

function getFirstDayOfNextMonth(date){
	var _date = new Date(date.getFullYear(),date.getMonth()+1,1);
	return _date;
}

var isLogin = $("#isLogin").val() == "true";

var isGetToReadMessageCount = isLogin;

function getToReadMessageCount(){
	if(isGetToReadMessageCount){
		$.ajax({
			type: "GET",
			url: contextPath + "/my/message/receive/getToReadMessageCount?sources=1",
			dataType: "json",
			success: function(data){
				if(data.success){
					$("#toReadMessageBadge").html(data.result);
				}else{
					isGetToReadMessageCount = false;
				}
	         },
	         error:function (request) {  
	        	isGetToReadMessageCount = false;
	         }
		});
	}
}

$(document).ready(function(){
	if(getCookie("WEBP_SUPPORT") == null){
		$('img').each(function(){
			var me = $(this);
			var src = me.attr("src");
			me.attr("data-webp-src",src);
			me.removeAttr("src").hide();
		});
		$("<img />").attr('src', contextPath + '/static/imgs/favicon.webp').on('load', function() {
		    if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
		    	setRootCookie("WEBP_SUPPORT", "true", 24*60*60*1000*30)
		    } else{
		    	setRootCookie("WEBP_SUPPORT", "false", 24*60*60*1000*30)
		    }
		    $('img').each(function(){
				var me = $(this);
				var src = me.attr("data-webp-src");
				me.attr("src",src);
				me.removeAttr("data-webp-src").show();
			});
		}).on("error",function(){
			setRootCookie("WEBP_SUPPORT", "false", 24*60*60*1000*30);
			$('img').each(function(){
				var me = $(this);
				var src = me.attr("data-webp-src");
				me.attr("src",src);
				me.removeAttr("data-webp-src").show();
			});
		});
	}
	getToReadMessageCount();
	if(isGetToReadMessageCount){
		setInterval(function(){
			getToReadMessageCount();
		}, 20000);
	}
	$("img.avatar").error(function(){
		var me = $(this);
		me.attr("src",contextPath + "/static/imgs/guest_160.png")
	});
	
	$(document).on("click","a[data-play]",function(){
		var me = $(this);
		var loading = $('<img src="'+contextPath+'/static/imgs/loading-64.gif" class="img-responsive"/>');
		me.parent().hide().after(loading);
		var img = $(me.find("img")[0]);
		img.attr("src",$(this).attr("data-play"));
		var span = $(me.find("span")[0]);
		img.bind("load",function(){
			span.hide();
			loading.remove();
			me.parent().show();
		});
	}); 
});

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
if(token != null && header != null && token != "" && header != ""){
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
    var r = window.location.search.substr(1).match(reg);  
    if (r != null) return unescape(r[2]); return null; 
}

function inArray(str,array){
	for(var i=0;i<array.length;i++){
		if(str == array[i]){
			return i;
		}
	}
	return -1;
}


function post(url,data,success_fn,error_fn,complete_fn){
	$.ajax({
        type: "post",
        url:  url,
        data: JSON.stringify(data),
        dataType: "json",
        contentType : 'application/json',
        success: success_fn,
        error:error_fn,
       	complete:complete_fn
    });
}

function get(url,data,success_fn,error_fn,complete_fn){
	$.ajax({
        type: "get",
        url:  url,
        data: JSON.stringify(data),
        dataType: "json",
        contentType : 'application/json',
        success: success_fn,
        error:error_fn,
       	complete:complete_fn
    });
}

function setRootCookie(name,value,time){
	var exp = new Date();
    exp.setTime(exp.getTime() +time);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+";path=/;domain="+getRootDomain();
}
//读取cookies
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
 
        return (arr[2]);
    else
        return null;
}