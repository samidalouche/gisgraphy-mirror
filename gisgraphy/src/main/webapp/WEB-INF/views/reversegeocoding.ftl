<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.geocoding.reverse.title"/></title>
<meta name="Description" content="Worldwide Reverse geocoding free webservices and street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.geocoding.reverse.title"/>"/>
<meta name="keywords" content=" reverse geocoding world worldwide street search free java openstreetmap webservices "/>
</head>
<body>
<br/>
<div id="gissearch">
<noscript>
	<div class="tip yellowtip">
	<@s.text name="global.noscript.required"/>
	</div>
	<br/>
</noscript>
	
			<@breadcrumbs.searchNavBar/>

<div class="clear"></div><div class="biggertext" style="line-height:1.5em;">The worldwide reverse geocoding webservice is totally FREE and allow to find a street from a Lat/lng pair. it uses (free) data from <span class="imgAlign"><a href="http://openstreetmap.org">openstreetMap <img src="/images/openstreetmap.png" alt="openstreetmap" class="imgAlign" style="width:30px"/></a> that are imported into a local database. you can see an example of use bellow</span>. You can find documentation on <a href="http://www.gisgraphy.com/documentation/index.htm#streetservice">how to use the webservice</a> and see how to <a href="http://www.gisgraphy.com/documentation/installation/index.htm" alt="install gisgraphy">download and install</a> Gisgraphy</div><br/><br/>
<div class="clear"></div>


	<@s.form action="/street/search" method="get" id="reversegeocoding">
		<div id="simplesearch">
			<@gisgraphysearch.latlongsearchbox/>
			<@breadcrumbs.geolocSearchTooltip advancedSearchURLParam="ajaxgeolocsearch"/>
<@s.hidden size="1" name="from" id="from"  value="1" theme="simple" />
<@s.hidden size="1" name="to"  id="to" value="1" theme="simple"/>
<@s.hidden size="1" name="name" id="name"  value="%" theme="simple" />
	</div>
	<div class="clear"><br/></div>
	</@s.form>
</div>
<div id="popupResults"></div>

<script src="/scripts/prototype.js" type="text/javascript"></script>
<script src="/scripts/gisgraphyapi.js" type="text/javascript"></script>

<script type="text/javascript" >

doSearch = function(){
	if (checkParameters("reversegeocoding")== false){
		return false;
	}
	query = new GisgraphyQuery("reversegeocoding",function(response){
		var data = response.evalJSON(true);
		var results = data.result
		var resultsSize = results.length
		if (resultsSize == 0){
			alert('no result found');
		} else if (resultsSize == 1){
			selectedStreetInformation = results[0];
			viewStreet(selectedStreetInformation.lat,selectedStreetInformation.lng);
			viewStreetPanorama(selectedStreetInformation.lat,selectedStreetInformation.lng);
		}
	}
);
query.execute();
return false;
}

</script>

<@gisgraphysearch.googleStreetView width="700" heigth="400" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />
<br/><br/>
<@gisgraphysearch.googleStreetPanorama width="700" heigth="300" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />

</body>
</html>