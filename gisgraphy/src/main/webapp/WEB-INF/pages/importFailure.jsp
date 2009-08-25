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
<strong>You can see the status of the Import and the error message here <a href="/admin/import!status.html">Here</a></strong>
<br/><br/>

An error occured during import. You have to:
<ul>
<li>Find and repair the error</li>
<li>Reset the database (A script is provided in the sql directory), because some data are already been imported and you'll have duplicate key / constraints exceptions. (it may take a while, if you've already import a lot of data).</li>
<br/>usage :
<ul>
<li> psql -UYOURUSER -f /path/to/file/resetdb.sql</li>
<li> psql -UYOURUSER -f /path/to/file/create_tables.txt</li>
</ul>
<br/>
<li>delete the downloaded files and the AdmXcodes.txt files.</li>
<li>Restart the web application, in order to flush the cache and to reset the configuration : importers keep informations of what have been imported. So you must restart the web application in order to clear those informations</li>
<li>Re-run the import</li> 
</ul>

<% 
Exception ex = (Exception) request.getAttribute("exception");
if (ex != null){
	ex.printStackTrace(new java.io.PrintWriter(out)); 
}
%>

<a href="mainMenu.html" onclick="history.back();return false">&#171; Back</a>