<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">${widget.name}</h3>
  </div>
  <#if (files?size > 0)>
	<div class="table-responsive">
		<table class="table">
			<#list files as file>
			    <tr>
			    	<td><a href="http://${urlHelper.getUrlByUser(user,false)}/blog/list/1?category.id=${file.category.id }">${file.category.name }</a></td>
			    	<td>${file.count}</td>
			    </tr>
			</#list>
		</table>
	</div>
	<#else>
		<div class="panel-body">
			<div class="alert alert-info">当前没有任何博客归档</div>
		</div>
	</#if>
</div>

