<%@ include file="/common/taglibs.jsp" %>

<title>Import failure</title>

<head>
    <meta name="heading" content="Import failure"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<p>
    <c:out value="${requestScope.exception.message}"/>
</p>
<br/>
<strong><fmt:message key="import.failure.see.status"><fmt:param>/admin/import!status.html</fmt:param></fmt:message></strong>
<br/><br/>

<div class="tip greentip">
<fmt:message key="gisgraphy.ask.for.dump"><fmt:param>davidmasclet@gisgraphy.com</fmt:param></fmt:message>
</div>
<br/><br/>
<fmt:message key="import.failure.instructions"><fmt:param>http://localhost:8080/admin/resetimport.html</fmt:param></fmt:message>

<% 
Exception ex = (Exception) request.getAttribute("exception");
if (ex != null){
	ex.printStackTrace(new java.io.PrintWriter(out)); 
}
%>

