<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">${widget.name}</h3>
  </div>
  <#if (blogs?size > 0)>
	<#if config.mode.name() == 'PREVIEW'>
		<div class="panel-body">
			<#list blogs as blog>
				<div class="media">
			      <div class="media-body">
			        <h4 class="media-heading">
			      	  	<a href="${urlHelper.getUrlBySpace(blog.space)}/blog/${blog.id?c}" target="_blank">
				        	<#if blog.title?length gt 15>
								<#escape x as x?html>
								 ${blog.title?substring(0,15)}...
								</#escape>
							    <#else>
							   <#escape x as x?html>
								 ${blog.title}
								</#escape>
							</#if>
						</a>
			        </h4>
			        <p>
			        	${blog.summary}
			        </p>
			        <h5><strong>${(blog.writeDate?string("yyyy-MM-dd HH:mm"))!}</strong></h5>
			      </div>
			    </div>
			</#list>
			<div class="alert alert-info"><#if config.space ?exists><a href="${urlHelper.getUrlBySpace(config.space)}/blog/list/1"><#else><a href="${urlHelper.getUrl()}/blog/list/1"></#if>查看更多</a></div>
		</div>
	</#if>
  	<#if config.mode.name() == 'LIST'>
		<div class="table-responsive">
			<table class="table">
				<tbody>
					<tr>
						<th>博客标题</th>
						<th>日期</th>
					</tr>
					<#list blogs as blog>
					    <tr>
					    	<td><a href="${urlHelper.getUrlBySpace(blog.space)}/blog/${blog.id?c}" target="_blank">
					    	<#if blog.title?length gt 15>
							   <#escape x as x?html>
								 ${blog.title?substring(0,15)}...
								</#escape>
							    <#else>
							   <#escape x as x?html>
								 ${blog.title}
								</#escape>
							</#if>
					    	</a></td>
					    	<td>
					    		${(blog.writeDate?string("yyyy-MM-dd HH:mm"))!}
					    	</td>
					    </tr>
					</#list>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="2"><#if config.space ?exists><a href="${urlHelper.getUrlBySpace(config.space)}/blog/list/1"><#else><a href="${urlHelper.getUrl()}/blog/list/1"></#if>查看更多</a></td>
					</tr>
				</tfoot>
			</table>
		</div>
	</#if>
	<#else>
		<div class="panel-body">
			<div class="alert alert-info">当前没有任何博客</div>
		</div>
	</#if>
</div>

