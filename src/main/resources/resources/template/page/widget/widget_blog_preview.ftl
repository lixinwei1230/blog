<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">${widget.name}(预览)</h3>
  </div>
  <#if (blogs?size > 0)>
  	<div class="panel-body">
		<div class="alert alert-warning">预览模式中，博客作者信息无法显示,放置挂件并且刷新页面后方可显示</div>
		<#if config.mode.name() == 'PREVIEW'>
			<#list blogs as blog>
				<div class="media">
			      <div class="media-left">
			        <a href="http://${urlHelper.getUrlBySpace(blog.space)}/blog/list/1">
			          <img class="media-object" data-src="holder.js/64x64" alt="64x64" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+PCEtLQpTb3VyY2UgVVJMOiBob2xkZXIuanMvNjR4NjQKQ3JlYXRlZCB3aXRoIEhvbGRlci5qcyAyLjYuMC4KTGVhcm4gbW9yZSBhdCBodHRwOi8vaG9sZGVyanMuY29tCihjKSAyMDEyLTIwMTUgSXZhbiBNYWxvcGluc2t5IC0gaHR0cDovL2ltc2t5LmNvCi0tPjxkZWZzPjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+PCFbQ0RBVEFbI2hvbGRlcl8xNGU4Njk1NGMyZiB0ZXh0IHsgZmlsbDojQUFBQUFBO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1mYW1pbHk6QXJpYWwsIEhlbHZldGljYSwgT3BlbiBTYW5zLCBzYW5zLXNlcmlmLCBtb25vc3BhY2U7Zm9udC1zaXplOjEwcHQgfSBdXT48L3N0eWxlPjwvZGVmcz48ZyBpZD0iaG9sZGVyXzE0ZTg2OTU0YzJmIj48cmVjdCB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIGZpbGw9IiNFRUVFRUUiLz48Zz48dGV4dCB4PSIxNC41IiB5PSIzNi41Ij42NHg2NDwvdGV4dD48L2c+PC9nPjwvc3ZnPg==" data-holder-rendered="true" style="width: 64px; height: 64px;">
			        </a>
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
			      </div>
			    </div>
			</#list>
  		</#if>
  	</div>
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
				    	<td></td>
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

