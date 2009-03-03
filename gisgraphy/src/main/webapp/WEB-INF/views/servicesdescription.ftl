<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<html>
<head>
<title><@s.text name="search.servicesdescription.title"/></title>
<meta name="Description" content="Description of Gisgraphy services"/>
<meta name="keywords" content="description gisgraphy services geoloc fulltext"/>
</head>
<body>
<br/>
<div>
<h2 class="header"><@s.text name="search.servicesdescription.title"/></h2>
<ul>
<li><@s.text name="search.fulltext.breadcrumbs"/> : User interface that call the fulltext webservice.
The output will be displayed in the navigator as it is returned by the webservice, whatever the format. There are more options available than the fulltext human readable service (<@s.text name="search.fulltextDemo.breadcrumbs"/>).</li>
<li><@s.text name="search.fulltextDemo.breadcrumbs"/> : User interface that call the fulltext service and display the results in an human readable way
 (ajax popup when javascript is activate, or in an html page ). There are less options available than the fulltext webservice interface (<@s.text name="search.fulltext.breadcrumbs"/>) because '<@s.text name="search.language"/>', '<@s.text name="search.verbosity"/>', '<@s.text name="search.format"/>', '<@s.text name="search.indent"/>' and '<@s.text name="search.paginationSpecs"/>' are managed by the HTML interface </li>
<li><@s.text name="search.geoloc.breadcrumbs"/> : User interface that call the geoloc webservice.
The output will be displayed in the navigator as it is returned by the service, whatever the format. There are more options available than the geoloc human readable service (<@s.text name="search.geolocDemo.breadcrumbs"/>)</li>
<li><@s.text name="search.geolocDemo.breadcrumbs"/> : User interface that call the geoloc service and display the results in an human readable way
 (ajax popup when javascript is activate, or in an html page ). There are less options available than the geoloc Webservice interface (<@s.text name="search.geoloc.breadcrumbs"/>) because '<@s.text name="search.format"/>', '<@s.text name="search.indent"/>' and '<@s.text name="search.paginationSpecs"/>' are managed by the HTML interface </li>
<li><@s.text name="search.street.breadcrumbs"/> : Service / Webservice that will search for street name around a given GPS position, all over the world. This service is currently in developement</li>
</ul>
<br/><br/><br/>

</div>

</body>
</html>