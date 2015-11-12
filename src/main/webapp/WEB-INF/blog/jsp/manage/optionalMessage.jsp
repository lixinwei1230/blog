<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="modal fade" id="optionalMessageModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">向TA说明为什么要进行这项操作</h4>
			</div>
			<div class="modal-body">
				<form id="optionalMessageForm" class="bs-example form-horizontal"
					autocomplete="off">
					<div class="row" style="margin-bottom: 15px">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>标题</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<input type="text" name="title" class="form-control" />
							</div>
						</div>
					</div>
					<div class="row" style="margin-bottom: 15px">
						<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
							<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
								<label>内容</label>
							</div>
							<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12">
								<textarea name="content" class="form-control"
									style="height: 300px"></textarea>
							</div>
						</div>
					</div>
					<input type="hidden" id="optional-id" name="id.id" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default"
					id="optionalMessage-confirm"
					data-loading-text="<spring:message code="page.item.processing" />"
					id="add-folder">确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:message code="page.item.close" />
				</button>
			</div>
		</div>
	</div>
</div>