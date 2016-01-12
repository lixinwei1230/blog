<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="staticSourcePrefix" value="${urlHelper.getStaticResourcePrefix(0) }" scope="request" />
<c:set var="staticSourcePrefixs" value="${urlHelper.getStaticResourcePrefixs() }" scope="request" />
<link href="${staticSourcePrefix }/plugins/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
     <script src="${staticSourcePrefix }/plugins/html5shiv/3.7.0/html5shiv.min.js"></script>
     <script src="${staticSourcePrefix }/plugins/respond/1.3.0/respond.min.js"></script>
   <![endif]-->
