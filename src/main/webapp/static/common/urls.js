var domainAndPort = $("#domainAndPort").val();
var domain = $("#domain").val();
var contextPath = $("#contextPath").val();
var enableSpaceDomain = $("#enableSpaceDomain").val() == "true";
var appendContextPath = contextPath != "" && contextPath != "/";
function getUrl(){
	return domainAndPort + (appendContextPath ? contextPath : "");
}
function getUrlBySpace(space){
	var url = "";
	if(enableSpaceDomain)
	{
		//start with www.
		if(domainAndPort.indexOf("www.") == 0){
			url = space.id + domainAndPort.substring(domainAndPort.indexOf("."));
		}else{
			url = space.id + "." + domainAndPort;
		}
		if(appendContextPath)
		{
			url += contextPath;
		}
		return url;
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
	return url;
}
function getUrlByUser(user,myMenu){
	var space = user.space;
	if(myMenu)
	{
		if(space && space != null && enableSpaceDomain)
		{
			return getUrlBySpace(space) + "/my";
		}
		else
		{
			return domainAndPort + "/my";
		}
	}
	else
	{
		if(space && space != null)
		{
			return getUrlBySpace(space);
		}
		else
		{
			return appendContextPath ? domainAndPort + contextPath +"/user/"+user.id
										: 	domainAndPort + "/user/"+user.id;
		}
	}
}
