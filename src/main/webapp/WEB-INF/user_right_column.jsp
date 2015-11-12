<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:if test="${_page != null }">
	<c:forEach items="${_page.rows }" var="rc">
		<div class="row text" style="margin-bottom: 10px">
			<c:forEach items="${rc.columns }" var="cc">
				<div class="col-lg-${cc.width } col-md-${cc.width}">
					<div class="row">
						<c:forEach items="${cc.widgets }" var="lw">
							<c:if test="${!lw.widget.config.hidden }">
								<div
									class="col-lg-<fmt:parseNumber integerOnly="true" value="${12*lw.width/cc.widthP }" /> col-md-<fmt:parseNumber integerOnly="true" value="${12*lw.width/cc.widthP }" />">
									${lw.widget.html }</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</c:if>
