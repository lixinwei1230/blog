var domainAndPort = $("#domainAndPort").val();
var domain = $("#domain").val();
var contextPath = $("#contextPath").val();
var enableSpaceDomain = $("#enableSpaceDomain").val() == "true";
var protocol = $("#protocol").val();
var appendContextPath = contextPath != "" && contextPath != "/";
function getUrl(){
	return _getUrl(protocol);
}
function _getUrl(protocol){
	return protocol + "://" + domainAndPort + (appendContextPath ? contextPath : "");
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
	return _getUrlBySpace(space, protocol);
}
function _getUrlBySpace(space,protocol){
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
		return protocol +"://" +url;
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
	return protocol +"://" +url;
}
function getUrlByUser(user,myMenu){
	return _getUrlByUser(user, myMenu, protocol);
}
function _getUrlByUser(user,myMenu,protocol){
	var space = user.space;
	if(myMenu)
	{
		if(space && space != null && enableSpaceDomain)
		{
			return getUrlBySpace(space,protocol) + "/my";
		}
		else
		{
			return protocol + "://"+domainAndPort + "/my";
		}
	}
	else
	{
		if(space && space != null)
		{
			return getUrlBySpace(space,protocol);
		}
		else
		{
			return protocol + "://" +(appendContextPath ? domainAndPort + contextPath +"/user/"+user.id
										: 	domainAndPort + "/user/"+user.id);
		}
	}
}
