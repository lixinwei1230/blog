<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">${widget.name}</h3>
  </div>
  <#if (blogs?size > 0)>
	<#if config.mode.name() == 'PREVIEW'>
		<div class="panel-body">
			<#list blogs as blog>
				<div class="media">
			      <div class="media-left user-info"  space-id="${blog.space.id }">
			      </div>
			      <div class="media-body">
			        <h4 class="media-heading">
			      	  	<a href="http://${urlHelper.getUrlBySpace(blog.space)}/blog/${blog.id}" target="_blank">
				        	<#if blog.title?length gt 15>
							    ${blog.title?substring(0,15)}...
							    <#else>
							    ${blog.title}
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
		</div>
	</#if>
  	<#if config.mode.name() == 'LIST'>
		<div class="table-responsive">
			<table class="table">
				<tr>
					<th>博客标题</th>
					<th>日期</th>
					<th>作者</th>
				</tr>
				<#list blogs as blog>
				    <tr>
				    	<td><a href="http://${urlHelper.getUrlBySpace(blog.space)}/blog/${blog.id}" target="_blank">
				    	<#if blog.title?length gt 15>
						    ${blog.title?substring(0,15)}...
						    <#else>
						    ${blog.title}
						</#if>
				    	</a></td>
				    	<td>
				    		${(blog.writeDate?string("yyyy-MM-dd HH:mm"))!}
				    	</td>
				    	<td class="user-info" info-mode="simple" space-id="${blog.space.id }" ></td>
				    </tr>
				</#list>
			</table>
		</div>
	</#if>
	<#else>
		<div class="panel-body">
			<div class="alert alert-info">当前没有任何博客</div>
		</div>
	</#if>
</div>

