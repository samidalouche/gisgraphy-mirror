<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<html>
<head>
<title><@s.text name="search.street.breadcrumbs"/></title>
</head>
<body>
<br/>
<noscript>
	<div class="tip yellowtip">
<@s.text name="global.noscript"/>
	</div>
	<br/>
</noscript>
<@s.form action="" id="streetsearch">
<@breadcrumbs.searchNavBar/>
</@s.form>
<div>
<h2 class="header"><@s.text name="search.street.breadcrumbs"/></h2>
<div class="biggertext">The street search will offer the possibility to search for street name all over the world. It is currently in developpement in the <a href="http://www.gisgraphy.com">Gisgraphy Project</a> it should be available in the second quarter of 2009</div>
<br/><br/><br/>
</div>

</body>
</html>