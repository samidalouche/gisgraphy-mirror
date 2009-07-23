<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<html>
<head>
<title><@s.text name="search.street.title"/></title>
<meta name="Description" content="free webservices for street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="keywords" content="street search java openstreetmap webservices postgis hibernate toponyms gazeteers"/>
<script src="/scripts/prototype.js" type="text/javascript"></script>
<script src="/scripts/autocomplete/autocomplete.js"></script>
<link href="/scripts/autocomplete/styles.css" rel="stylesheet" type="text/css" />

</head>
<body>
<br/>
<noscript>
	<div class="tip yellowtip">
<@s.text name="global.noscript"/>
	</div>
	<br/>
</noscript>
<div class="clear"></div>
<h2 class="header"><@s.text name="search.street.title"/></h2>
<div class="clear"></div>
<#if errorMessage!= ''>
${errorMessage}
</#if>
<#if message!= ''>
${message}
</#if>
<@s.form action="" id="streetsearch">
<@breadcrumbs.searchNavBar/>
        <span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.country"/> : </span><@s.select label="country " listKey="iso3166Alpha2Code" listValue="name" name="countryCode" list="countries" headerValue="--select your country--" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" id="city"/> 
			<br/>
		</span>
		<#if (ambiguousCities?? &&  ambiguousCities.size() > 1 )>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.city.ambiguous"/> : </span><@s.select listKey="featureId" listValue="name" name="ambiguouscity" list="ambiguousCities" headerValue="--select a city--" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" onchange="updateLatLng();" id="ambiguouscity" /> 
			<script type="text/javascript">
			latlngArray = eval('${latLongJson}')
			updateLatLng = function(){
			var indexDropDown = ($('ambiguouscity').selectedIndex);
				if (indexDropDown == 0){
					$('lat').value='',
					$('lng').value='';
					$('streetname').disable();
					return
				}
			$('lat').value = latlngArray[indexDropDown-1].lat;
			$('lng').value = latlngArray[indexDropDown-1].lng;
			$('streetname').enable();
			streetnameAutocomplete.serviceUrl="/static/feed.txt?lat"+$('lat').value+"&lng="+$('lng').value
                        }
			</script>
			<br/>
		</span>
		
		<#else>
		<span class="searchfield">
			<span class="searchfieldlabel">city : </span><@s.textfield size="5" name="city" required="false"  theme="simple"/>
			<br/>
		</span>
		</#if>
		<div class="clear"></div>
		<@s.hidden size="5" name="lat" required="false" id="lat"  theme="simple"/><@s.hidden size="5" name="lng" required="false" id="lng" theme="simple"/>
			<br/>
		</span>
<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel">streetname : </span><@s.textfield size="50" name="streetname" required="false" id="streetname"  theme="simple"/>
			<br/>
<script type="text/javascript">

 streetnameAutocomplete = new Autocomplete('streetname', { serviceUrl: '/static/feed.txt', width: 500 });
  
</script>

		</span>

<@s.submit title="Search" value="Search" theme="simple"/>
</@s.form>
<div>
<div class="clear"></div>
<div class="biggertext">The street search will offer the possibility to search for street name all over the world. It is currently in developpement in the <a href="http://www.gisgraphy.com">Gisgraphy Project</a> it should be available in the second quarter of 2009</div>
<br/><br/><br/>
</div>

</body>
</html>