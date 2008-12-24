<%@ include file="/common/taglibs.jsp" %>

<title>Data Access Error</title>

<head>
    <meta name="heading" content="Data Access Failure"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<p>
    <c:out value="${requestScope.exception.message}"/>
</p>

An error occured during import. You have to:
<ul>
<li>Find and repair the error</li>
<li>reset the database, because some data are already in the database and you'll have duplicate key/ constraint exceptions.</li>
<li>Restart the web application, in order to flush the cache and to reset the configuration. importers keep informations of what have been imported. So you must restart the web application in order to clear those informations</li>
<li>Re-Run the import</li> 
</ul>
<!--
<% 
Exception ex = (Exception) request.getAttribute("exception");
ex.printStackTrace(new java.io.PrintWriter(out)); 
%>
-->

<a href="mainMenu.html" onclick="history.back();return false">&#171; Back</a>
