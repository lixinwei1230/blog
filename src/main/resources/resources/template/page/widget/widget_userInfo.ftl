<div style="margin-bottom:10px">
	<a href="${urlHelper.getUrlByUser(user,false)}/index">
		<#if user.avatar??>
			<img title="${user.nickname}" alt="${user.nickname}" src="${Resize.getUrl(user.avatar.url,160)}" class="img-thumbnail" >
		<#else>
			<img title="${user.nickname}" alt="${user.nickname}" src="${urlHelper.getStaticResourcePrefix(0)}/imgs/guest_160.png" class="img-thumbnail"/>
		</#if>
	</a>
	<#if blog??>
		<h5>博客:<a href="${urlHelper.getUrlByUser(user,false)}/blog/list/1">${blog}</a></h5>
	</#if>
</div>