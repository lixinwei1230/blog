<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">${widget.name}</h3>
  </div>
  <#if (files?size > 0)>
	<div class="panel-body">
		<div class="panel-group" id="blogDateFileWidget_accordion" role="tablist"
			aria-multiselectable="true">
			<#list files as file>
			    <div class="panel panel-default">
					<div class="panel-heading" role="tab" >
						<h4 class="panel-title">
							<a role="button" data-toggle="collapse"  href="#blogDateFileWidget_${file_index+1}"
								aria-expanded="false" aria-controls="blogDateFileWidget_${file_index+1}" class="collapsed">
								${(file.begin?string("yyyy"))!}
					    			(${file.count})
							</a>
						</h4>
					</div>
					<div id="blogDateFileWidget_${file_index+1}" class="panel-collapse collapse "
						role="tabpanel" >
						<table class="table" style="margin-bottom:0px">
							<#list file.subfiles as _file>
								<tr>
									<td>
										<a href="${urlHelper.getUrlByUser(user,false)}/blog/list/1?begin=${(_file.begin?string("yyyy-MM-dd HH:mm:ss"))!}&end=${(_file.end?string("yyyy-MM-dd HH:mm:ss"))!}">
							    			${(_file.begin?string("yyyy-MM"))!}
							    			(${_file.count})
							    		</a>
									</td>
								</tr>	
							</#list>
						</table>
					</div>
				</div>
			</#list>
		</table>
		</div>
	</div>
	<#else>
		<div class="panel-body">
			<div class="alert alert-info">当前没有任何博客归档</div>
		</div>
	</#if>
</div>

