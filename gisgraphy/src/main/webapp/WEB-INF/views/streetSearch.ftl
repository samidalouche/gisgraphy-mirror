<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.street.title"/></title>
<meta name="Description" content="free webservices for street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.street.title"/>"/>
<meta name="keywords" content="street search java openstreetmap webservices postgis hibernate toponyms gazeteers"/>
<script src="/scripts/prototype.js" type="text/javascript"></script>
</head>
<body onunload="GUnload()">
<br/>
<noscript>
	<div class="tip yellowtip">
		<@s.text name="global.noscript"/>
	</div>
	<br/>
</noscript>
<br/>
<div class="clear"></div><div class="biggertext" style="line-height:1.5em;">The street webservice is Free and offer the possibility to search for street name all over the world. The data come from <span class="imgAlign"><a href="http://openstreetmap.org">openstreetMap <img src="/images/openstreetmap.png" alt="openstreetmap" class="imgAlign" style="width:30px"/></a> and are imported into a local database in order to be used by the gisgraphy webService. Here is an example of use of the street webservice</span>. You can find documentation on how to use the webservice <a href="http://www.gisgraphy.com/documentation/index.htm#streetservice">here</a> and see how to download and install gisgraphy : <a href="http://www.gisgraphy.com/documentation/installation/index.htm" alt="install gisgraphy">here</a> </div><br/><br/>
<div class="clear"></div>
<#if errorMessage!= ''>${errorMessage}</#if>
<#if message!= ''>${message}</#if>
	<div class="center">
	<@s.form action="" id="streetsearch">
		 <@breadcrumbs.searchNavBar/>
         <span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.country"/> : </span><@s.select label="country " listKey="iso3166Alpha2Code" listValue="name" name="countryCode" list="countries" headerValue="--select your country--" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" id="city"/> 
	<br/>
	</span>
	<@gisgraphysearch.citySelector  onCityFound="updateLatLng" />
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
			streetNameAutocompleter.serviceUrl="/street/streetsearch?format=json&lat="+$('lat').value+"&lng="+$('lng').value+"&from=1&to=10"
                        }
			</script>
		<div class="clear"></div>
		<br/>
		</span>
                <div class="clear"></div>
		<@gisgraphysearch.streetNameAutoCompleter/>
<@s.submit title="Search" value="Search" theme="simple"/>
</div>
</@s.form>
<div>
<div class="clear"></div>
<br/><br/>
<div class="clear"></div>
<@gisgraphysearch.googleStreetPanorama width="500" heigth="300" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />

<br/><br/>

<br/><br/><br/>

</div>

</body>
</html>