<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">${widget.name}</h3>
  </div>
  <#if (loginInfos?size > 0)>
	<div class="table-responsive">
		<table class="table">
			<#list loginInfos as loginInfo>
			    <tr>
			    	<td><a href="http://ip.taobao.com/ipSearch.php?ipAddr=${loginInfo.remoteAddress}" target="_blank">${loginInfo.remoteAddress}</a></td>
			    	<td>${(loginInfo.loginDate?string("yyyy-MM-dd HH:mm"))!}</td>
			    </tr>
			</#list>
		</table>
	</div>
	<#else>
		<div class="panel-body">
			<div class="alert alert-info">当前没有任何登录信息</div>
		</div>
	</#if>
</div>