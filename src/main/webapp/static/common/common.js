var img = $("<img />").attr('src', contextPath + '/static/imgs/favicon.webp')
	.on('load', function() {
	    if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
	    	setRootCookie("WEBP_SUPPORT", "true", 24*60*60*1000*30)
	    } else{
	    	setRootCookie("WEBP_SUPPORT", "false", 24*60*60*1000*30)
	    }
	}).on("error",function(){
		setRootCookie("WEBP_SUPPORT", "false", 24*60*60*1000*30)
	});

String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}

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
	
	getToReadMessageCount();
	if(isGetToReadMessageCount){
		setInterval(function(){
			getToReadMessageCount();
		}, 20000);
	}
	writerUserInTargetContainer($("body"));
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
		})
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

function getUserInfoBySpaces(ids,success_fn){
	var url = contextPath + "/user/info/";
	$.get(url,{"spaces":ids.toString()},function callBack(data){
		if(data.success){
			success_fn(data.result);
		}
	});
}


var usersCache = [];

function getUserInfoByIds(ids,success_fn){
	var url = contextPath + "/user/info/";
	$.get(url,{"ids":ids.toString()},function callBack(data){
		if(data.success){
			success_fn(data.result);
		}
	});
}

function getUser(id,users,type){
	for(var i=0;i<users.length;i++){
		var user = users[i];
		if(type == "space"){
			if(user.space.id == id){
				return user;
			}
		}
		if(type == "user"){
			if(user.id == id){
				return user;
			}
		}
		
	}
	return null;
}

function setRootCookie(name,value,time){
	var exp = new Date();
    exp.setTime(exp.getTime() +time);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+";path=/;domain="+getRootDomain();
}

function setCookie(name,value,time)
{
    var exp = new Date();
    exp.setTime(exp.getTime() +time);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
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

//删除cookies
function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}

function writerUserInTargetContainer(container){
	var spaceIds = [];
	var userIds = [];
	container.find(".user-info").each(function(){
		var _this = $(this);
		if(_this.attr("space-id")){
			spaceIds.push(_this.attr("space-id"));
		}
		if(_this.attr("user-id")){
			userIds.push(_this.attr("user-id"));
		}
	});	
	if(userIds.length > 0){
		getUserInfoByIds(userIds,function(users){
			$(".user-info").each(function(){
				var _this = $(this);
				writeUserInfo("user",_this,users);
			});	
		});
	}
	if(spaceIds.length > 0){
		getUserInfoBySpaces(spaceIds,function(users){
			$(".user-info").each(function(){
				var _this = $(this);
				writeUserInfo("space",_this,users);
			});	
		});
	}
}

function writeUserInfo(type,_this,users){
	var user = null;
	if(type == "space"){
		user = getUser(_this.attr("space-id"), users,type);
	}else{
		user = getUser(_this.attr("user-id"), users,type);
	}
	
	var mode = _this.attr("info-mode");
	var name = "";
	var format = _this.attr("info-format");
	if(format){
		
		if(format == "username"){
			name = user.username;
		}else if(format == "nickname"){
			name = user.nickname;
		}else if(format == "nickname(username)"){
			name = user.nickname(user.username);
		}
	}else{
		name = user.nickname;
	}
	if(mode && mode == 'simple'){
		_this.html('<a href="'+getUrlByUser(user, false)+'/index">' +name + '</a>');
	}else{
		var avatar = "";
		if(user.avatar){
			avatar = '<img alt="'+name+'" class="img-circle"  src="'+user.avatar.seekPrefixUrl+'?path='+user.avatar.seekPath+'&size=64" onerror="javascript:this.src=\''+contextPath+'/static/imgs/guest_64.png\'" title="'+name+'"/>';
		}else{
			avatar = '<img alt="'+name+'" src="'+contextPath+'/static/imgs/guest_64.png" title="'+name+'" class="img-circle"/>'
		}
		_this.html('<a href="'+getUrlByUser(user, false)+'/index">'+avatar+'</a>');
	}
}

