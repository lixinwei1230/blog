var domainAndPort = $("#domainAndPort").val();
var domain = $("#domain").val();
var contextPath = $("#contextPath").val();
var enableSpaceDomain = $("#enableSpaceDomain").val() == "true";
var protocal = $("#protocal").val();
var appendContextPath = contextPath != "" && contextPath != "/";
function getUrl(){
	return _getUrl(protocal);
}
function _getUrl(protocal){
	return protocal + "://" + domainAndPort + (appendContextPath ? contextPath : "");
}
function getRootDomain(){
	if(domain.indexOf('.') != -1){
		var domains = domain.split('.');
		var len = domains.length;
		if(len > 2){
			return '.'+domains[len-2] + '.' + domains[len-1];
		}
	}
	return "";
}
function getUrlBySpace(space){
	return _getUrlBySpace(space, protocal);
}
function _getUrlBySpace(space,protocal){
	var url = "";
	if(enableSpaceDomain)
	{
		if(domainAndPort.indexOf("www.") == 0){
			url = space.id + domainAndPort.substring(domainAndPort.indexOf("."));
		}else{
			url = space.id + "." + domainAndPort;
		}
		if(appendContextPath)
		{
			url += contextPath;
		}
		return protocal +"://" +url;
	}
	url = domainAndPort;
	if(appendContextPath)
	{
		url += contextPath + "/space/"+space.id;
	}
	else
	{
		url += "/space/"+space.id;
	}
	return protocal +"://" +url;
}
function getUrlByUser(user,myMenu){
	return _getUrlByUser(user, myMenu, protocal);
}
function _getUrlByUser(user,myMenu,protocal){
	var space = user.space;
	if(myMenu)
	{
		if(space && space != null && enableSpaceDomain)
		{
			return getUrlBySpace(space,protocal) + "/my";
		}
		else
		{
			return protocal + "://"+domainAndPort + "/my";
		}
	}
	else
	{
		if(space && space != null)
		{
			return getUrlBySpace(space,protocal);
		}
		else
		{
			return protocal + "://" +(appendContextPath ? domainAndPort + contextPath +"/user/"+user.id
										: 	domainAndPort + "/user/"+user.id);
		}
	}
}
