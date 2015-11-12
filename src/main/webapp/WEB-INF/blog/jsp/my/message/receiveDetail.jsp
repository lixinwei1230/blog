<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title text" id="myModalLabel">
		<c:out value="${receive.message.detail.title }" />
	</h4>
</div>
<div class="modal-body ">
	<div class="row" style="margin-bottom: 15px">
		<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
			<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
				<label><spring:message code="page.message.content" /></label>
			</div>
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12 text">
				${receive.message.detail.content }</div>
		</div>
	</div>
	<div class="row" style="margin-bottom: 15px">
		<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12">
			<div class="col-lg-4 col-sm-12 col-xs-12 col-md-4">
				<label><spring:message code="page.message.sendDate" /></label>
			</div>
			<div class="col-lg-8 col-sm-12 col-md-8 col-xs-12 text">
				<fmt:formatDate value="${receive.message.sendDate }"
					pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">
		<spring:message code="page.item.close" />
	</button>
</div>
